
public class Spline {

    public static class Point {
        private double x;
        private double y;
        private double heading;

        public Point(double x, double y, double heading) {
            this.x = x;
            this.y = y;
            this.heading = heading;
        }

        public Point(double x, double y) {
            this(x, y, 999999);
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getHeading() {
            return this.heading;
        }
    }

    private Point[] points;

    private Polynomial[] xPolynomials;
    private Polynomial[] yPolynomials;

    private Polynomial[] xFirstDerivatives;
    private Polynomial[] yFirstDerivatives;

    private Polynomial[] xSecondDerivatives;
    private Polynomial[] ySecondDerivatives;

    public Spline(Point[] points, Polynomial[] xPolynomials, Polynomial[] yPolynomials) {

        assert xPolynomials.length == yPolynomials.length : "xPolynomials and yPolynomials must be the same length.";
        this.points = points;
        this.xPolynomials = xPolynomials;
        this.yPolynomials = yPolynomials;

        this.xFirstDerivatives = new Polynomial[xPolynomials.length];
        this.yFirstDerivatives = new Polynomial[yPolynomials.length];

        this.xSecondDerivatives = new Polynomial[xPolynomials.length];
        this.ySecondDerivatives = new Polynomial[yPolynomials.length];

        for (int i = 0; i < xPolynomials.length; i++) {
            this.xFirstDerivatives[i] = Polynomial.differentiate(this.xPolynomials[i]);
            this.yFirstDerivatives[i] = Polynomial.differentiate(this.yPolynomials[i]);
            this.xSecondDerivatives[i] = Polynomial.differentiate(this.xFirstDerivatives[i]);
            this.ySecondDerivatives[i] = Polynomial.differentiate(this.yFirstDerivatives[i]);
        }
    }

    public Point evaluate(double t) {
        if (t == this.xPolynomials.length) {
            return new Point(this.xPolynomials[this.xPolynomials.length - 1].evaluate(1.0),
                    this.yPolynomials[this.yPolynomials.length - 1].evaluate(1.0));
        } else {
            int index = (int) t;
            return new Point(xPolynomials[index].evaluate(t - index), yPolynomials[index].evaluate(t - index));
        }
    }

    public Point evaluateFirstDerivatives(double t) {
        if (t == this.xFirstDerivatives.length) {
            return new Point(this.xFirstDerivatives[this.xFirstDerivatives.length - 1].evaluate(1.0),
                    this.yFirstDerivatives[this.yFirstDerivatives.length - 1].evaluate(1.0));
        } else {
            int index = (int) t;
            return new Point(xFirstDerivatives[index].evaluate(t - index),
                    yFirstDerivatives[index].evaluate(t - index));
        }
    }

    public Point evaluateSecondDerivatives(double t) {
        if (t == this.xSecondDerivatives.length) {
            return new Point(this.xSecondDerivatives[this.xSecondDerivatives.length - 1].evaluate(1.0),
                    this.ySecondDerivatives[this.ySecondDerivatives.length - 1].evaluate(1.0));
        } else {
            int index = (int) t;
            return new Point(xSecondDerivatives[index].evaluate(t - index),
                    ySecondDerivatives[index].evaluate(t - index));
        }
    }

    public double evaluateHeading(double t) {
//        double y = evaluate(0).y;
//        double x = evaluate(0).x;
//        double initialHeading = Math.toDegrees(Math.atan2(y, x));
        double xDistance = evaluateFirstDerivatives(t).x;
        double yDistance = evaluateFirstDerivatives(t).y;

        double heading = Math.toDegrees(Math.atan2(yDistance, xDistance));

        if (heading < 0) {
            heading += 360.0;
        }
//        double correctedHeading = (heading - initialHeading) > 0.0 ? heading - initialHeading
//                : heading - initialHeading + 360.0;

        return heading;
    }


    public double evaluateCurvature(double t) {
        Point firstDerivative = evaluateFirstDerivatives(t);
        Point secondDerivative = evaluateSecondDerivatives(t);

        return (firstDerivative.getX() * secondDerivative.getY() - firstDerivative.getY() * secondDerivative.getX()) // (x'y'-
                // y'x'')
                / Math.pow(Math.pow(firstDerivative.getX(), 2) + Math.pow(firstDerivative.getY(), 2), 1.5); // / (x'^2)
        // y'^2)^1.5
    }

    public Point[] getPoints() {
        return this.points;
    }

    public double getDomainMax() {
        return this.xPolynomials.length;
    }

    public String pointsToString() {
        String pointsString = "";
        for (Point point : this.points) {
            pointsString += "(" + point.getX() + "," + point.getY() + "),";
        }

        return pointsString.substring(0, pointsString.length() - 1);
    }

    public String curvatureToDesmos() {
        String equation = "\\left\\{";
        for (int i = 0; i < this.xPolynomials.length; i++) {
            String parameterString = "(x-" + i + ")";
            String curvatureEquation = "((" + this.xFirstDerivatives[i].toString(parameterString) + ")*("
                    + this.ySecondDerivatives[i].toString(parameterString) + ")-("
                    + this.yFirstDerivatives[i].toString(parameterString) + ")*("
                    + this.xSecondDerivatives[i].toString(parameterString) + "))/((("
                    + this.xFirstDerivatives[i].toString(parameterString) + "))^2+(("
                    + this.yFirstDerivatives[i].toString(parameterString) + "))^2)^{\\frac{3}{2}}";

            equation += i + "<=x<" + (i + 1) + ":" + curvatureEquation + ",";
        }

        return equation.substring(0, equation.length() - 1) + "\\right\\}";
    }

    public String toString() {
        return "(" + polynomialListToString(this.xPolynomials) + ", " + polynomialListToString(this.yPolynomials) + ")";
    }

    public String firstDerivativeToString() {
        return "(" + polynomialListToString(this.xFirstDerivatives) + ", "
                + polynomialListToString(this.yFirstDerivatives) + ")";
    }

    public String secondDerivativeToString() {
        return "(" + polynomialListToString(this.xSecondDerivatives) + ", "
                + polynomialListToString(this.ySecondDerivatives) + ")";
    }

    private String polynomialListToString(Polynomial[] polynomials) {
        String equation = "\\left\\{";
        for (int i = 0; i < polynomials.length; i++) {
            equation += i + "<=t<" + (i + 1) + ":" + polynomials[i].toString("(t-" + i + ")") + ",";
        }

        return equation.substring(0, equation.length() - 1) + "\\right\\}";
    }

    public static Spline quinticHermiteSplineFor(double velocityWeight, double accelerationWeight, Point... points) {

        Polynomial[] xPolynomials = QuinticHermite.splineFor(velocityWeight, accelerationWeight, points, true);
        Polynomial[] yPolynomials = QuinticHermite.splineFor(velocityWeight, accelerationWeight, points, false);

        return new Spline(points, xPolynomials, yPolynomials);

    }
}
