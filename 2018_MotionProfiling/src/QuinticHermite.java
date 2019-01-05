
public class QuinticHermite {

    private static final Polynomial H50 = new Polynomial(1.0, 0.0, 0.0, -10.0, 15.0, -6.0);
    private static final Polynomial H51 = new Polynomial(0.0, 1.0, 0.0, -6.0, 8.0, -3.0);
    private static final Polynomial H52 = new Polynomial(0.0, 0.0, 0.5, -1.5, 1.5, -0.5);
    private static final Polynomial H53 = new Polynomial(0.0, 0.0, 0.0, 0.5, -1.0, 0.5);
    private static final Polynomial H54 = new Polynomial(0.0, 0.0, 0.0, -4.0, 7.0, -3.0);
    private static final Polynomial H55 = new Polynomial(0.0, 0.0, 0.0, 10.0, -15.0, 6.0);

    public static Polynomial from(double p0, double p1, double v0, double v1, double a0, double a1) {
        return Polynomial.sum(Polynomial.multiply(H50, p0), Polynomial.multiply(H51, v0), Polynomial.multiply(H52, a0),
                Polynomial.multiply(H53, a1), Polynomial.multiply(H54, v1), Polynomial.multiply(H55, p1));
    }

    public static Polynomial[] splineFor(double velocityWeight, double accelerationWeight, Spline.Point[] points, boolean isX) {

        double[] xValues = new double[points.length];
        double[] yValues = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            xValues[i] = points[i].getX();
            yValues[i] = points[i].getY();

        }


        double[] vValues = new double[points.length];
        for (int i = 0; i < points.length - 1; i++) {

            double distance = Math.sqrt(Math.pow(xValues[i + 1] - xValues[i], 2) + Math.pow(yValues[i + 1] - yValues[i], 2));

            double heading = Math.toRadians(points[i].getHeading());

            if (heading == 999999) {
                if (isX) {
                    vValues[i] = velocityWeight * (points[i + 1].getX() - points[i - 1].getX());
                } else {
                    vValues[i] = velocityWeight * (points[i + 1].getY() - points[i - 1].getY());
                }
            } else {
                if (isX) {
                    vValues[i] = velocityWeight * Math.cos(heading) * distance;
                } else {
                    vValues[i] = velocityWeight * Math.sin(heading) * distance;
                }
            }
        }

        double endHeading = Math.toRadians(points[points.length - 1].getHeading());
        double endDistance = Math.sqrt(Math.pow(xValues[points.length - 1] - xValues[points.length - 2], 2) + Math.pow(yValues[points.length - 1] - yValues[points.length - 2], 2));

        if (isX) {
            vValues[points.length - 1] = velocityWeight * Math.cos(endHeading) * endDistance;
        } else {
            vValues[points.length - 1] = velocityWeight * Math.sin(endHeading) * endDistance;
        }


        double[] aValues = new double[vValues.length];
        aValues[0] = 0.0;
        aValues[vValues.length - 1] = 0.0;

        for (int i = 1; i < vValues.length - 1; i++) {
            aValues[i] = accelerationWeight * (vValues[i + 1] - vValues[i - 1]);
        }

        Polynomial[] spline = new Polynomial[vValues.length - 1];

        for (int i = 0; i < spline.length; i++) {
            if (isX) {
                spline[i] = QuinticHermite.from(points[i].getX(), points[i + 1].getX(), vValues[i], vValues[i + 1], aValues[i],
                        aValues[i + 1]);
            } else {
                spline[i] = QuinticHermite.from(points[i].getY(), points[i + 1].getY(), vValues[i], vValues[i + 1], aValues[i],
                        aValues[i + 1]);
            }
        }

        return spline;
    }

    private static void print(int offset, Polynomial... polynomials) {
        String equation = "";
        for (int i = 0; i < polynomials.length; i++) {
            equation += (i + offset) + "<=x<" + (i + 1 + offset) + ":"
                    + polynomials[i].toString("(x" + "-" + (i + offset) + ")") + ",";
        }
        equation = equation.substring(0, equation.length() - 1);

        System.out.println(equation);
    }
}
