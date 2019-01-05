package trajectory

import (
	"fmt"
	"math"
)

type Point struct {
	X float64 `json: "X"`
	Y float64 `json: "Y"`
}

type Point2 struct {
	X, Y    float64
	Heading float64
	Tension float64
}

func (p Point2) Point() Point {
	return Point{p.X, p.Y}
}

type Spline struct {
	points                                 []Point
	xPolynomials, yPolynomials             []Polynomial
	xFirstDerivatives, yFirstDerivatives   []Polynomial
	xSecondDerivatives, ySecondDerivatives []Polynomial
}

func NewSpline(points []Point, xPolynomials, yPolynomials []Polynomial) *Spline {
	xFirstDerivatives := make([]Polynomial, len(xPolynomials))
	yFirstDerivatives := make([]Polynomial, len(yPolynomials))
	xSecondDerivatives := make([]Polynomial, len(xPolynomials))
	ySecondDerivatives := make([]Polynomial, len(yPolynomials))

	for i := 0; i < len(xPolynomials); i++ {
		xFirstDerivatives[i] = Differentiate(xPolynomials[i])
		yFirstDerivatives[i] = Differentiate(yPolynomials[i])
		xSecondDerivatives[i] = Differentiate(xFirstDerivatives[i])
		ySecondDerivatives[i] = Differentiate(yFirstDerivatives[i])
	}

	return &Spline{points, xPolynomials, yPolynomials, xFirstDerivatives, yFirstDerivatives, xSecondDerivatives, ySecondDerivatives}
}

func (spline *Spline) Evaluate(t float64) Point {
	if int(t) == len(spline.xPolynomials) {
		return Point{spline.xPolynomials[len(spline.xPolynomials)-1].Evaluate(1.0), spline.yPolynomials[len(spline.yPolynomials)-1].Evaluate(1.0)}
	} else {
		index := math.Floor(t)
		return Point{spline.xPolynomials[int(index)].Evaluate(t - index), spline.yPolynomials[int(index)].Evaluate(t - index)}
	}
}

func (spline *Spline) EvaluateFirstDerivative(t float64) Point {
	if int(t) == len(spline.xFirstDerivatives) {
		return Point{spline.xFirstDerivatives[len(spline.xFirstDerivatives)-1].Evaluate(1.0), spline.yFirstDerivatives[len(spline.yFirstDerivatives)-1].Evaluate(1.0)}
	} else {
		index := math.Floor(t)
		return Point{spline.xFirstDerivatives[int(index)].Evaluate(t - index), spline.yFirstDerivatives[int(index)].Evaluate(t - index)}
	}
}

func (spline *Spline) EvaluateSecondDerivative(t float64) Point {
	if int(t) == len(spline.xSecondDerivatives) {
		return Point{spline.xSecondDerivatives[len(spline.xSecondDerivatives)-1].Evaluate(1.0), spline.ySecondDerivatives[len(spline.ySecondDerivatives)-1].Evaluate(1.0)}
	} else {
		index := math.Floor(t)
		return Point{spline.xSecondDerivatives[int(index)].Evaluate(t - index), spline.ySecondDerivatives[int(index)].Evaluate(t - index)}
	}
}

func (spline *Spline) EvaluateHeading(t float64) float64 {
	firstDerivative := spline.EvaluateFirstDerivative(t)
	return math.Atan2(firstDerivative.Y, firstDerivative.X) * 180 / math.Pi
}

func (spline *Spline) EvaluateCurvature(t float64) float64 {
	firstDerivative := spline.EvaluateFirstDerivative(t)
	secondDerivative := spline.EvaluateSecondDerivative(t)

	return (firstDerivative.X*secondDerivative.Y - firstDerivative.Y*secondDerivative.X) / math.Pow(math.Pow(firstDerivative.X, 2)+math.Pow(firstDerivative.Y, 2), 1.5)
}

func (spline *Spline) DomainMax() float64 {
	return float64(len(spline.xPolynomials))
}

func (spline Spline) String() string {
	return "(" + PolynomialListToString(spline.xPolynomials) + "," + PolynomialListToString(spline.yPolynomials) + ")"
}

func PolynomialListToString(polynomials []Polynomial) string {
	equation := "\\left\\{"
	for index, polynomial := range polynomials {
		equation += fmt.Sprintf("%d<=t<%d:%s,", index, index+1, polynomial.StringForParameter(fmt.Sprintf("(t-%d)", index)))
	}

	return string([]rune(equation)[:len(equation)-1]) + "\\right\\}"
}

func SplineFor(startHeadingDegrees, endHeadingDegrees float64, points ...Point) *Spline {
	startHeading := startHeadingDegrees * math.Pi / 180
	endHeading := endHeadingDegrees * math.Pi / 180

	startDistance := math.Sqrt(math.Pow(points[1].X-points[0].X, 2) + math.Pow(points[1].Y-points[0].Y, 2))
	endDistance := math.Sqrt(math.Pow(points[len(points)-1].X-points[len(points)-2].X, 2) + math.Pow(points[len(points)-1].Y-points[len(points)-2].Y, 2))

	xValues := make([]float64, len(points))
	yValues := make([]float64, len(points))
	for index, point := range points {
		xValues[index] = point.X
		yValues[index] = point.Y
	}

	xPolynomials := QuinticHermiteFor(0.5*math.Cos(startHeading)*startDistance, 0.5*math.Cos(endHeading)*endDistance, xValues...)
	yPolynomials := QuinticHermiteFor(0.5*math.Sin(startHeading)*startDistance, 0.5*math.Sin(endHeading)*endDistance, yValues...)

	return NewSpline(points, xPolynomials, yPolynomials)
}

func SplineFor2(points ...Point2) *Spline {
	xValues := make([]float64, len(points))
	xVValues := make([]float64, len(points))
	yValues := make([]float64, len(points))
	yVValues := make([]float64, len(points))
	for index, point := range points {
		xValues[index] = point.X
		yValues[index] = point.Y
	}
	for index, point := range points {
		var distanceX, distanceY float64
		if index == 0 {
			distanceX = 2 * math.Abs(xValues[index+1]-xValues[index])
			distanceY = 2 * math.Abs(yValues[index+1]-yValues[index])
		} else if index == len(points)-1 {
			distanceX = 2 * math.Abs(xValues[index]-xValues[index-1])
			distanceY = 2 * math.Abs(yValues[index]-yValues[index-1])
		} else {
			distanceX = math.Abs(xValues[index+1] - xValues[index-1])
			distanceY = math.Abs(yValues[index+1] - yValues[index-1])
		}

		distance := math.Sqrt(math.Pow(distanceX, 2) + math.Pow(distanceY, 2))

		xVValues[index] = (1 - point.Tension) * distance * math.Cos(point.Heading*math.Pi/180)
		yVValues[index] = (1 - point.Tension) * distance * math.Sin(point.Heading*math.Pi/180)
	}

	xPolynomials := QuinticHermiteFor2(xValues, xVValues)
	yPolynomials := QuinticHermiteFor2(yValues, yVValues)

	simplePoints := make([]Point, len(points))
	for index, point := range points {
		simplePoints[index] = point.Point()
	}

	return NewSpline(simplePoints, xPolynomials, yPolynomials)
}

func SplineForSkeleton(skeleton *SplineSkeleton) *Spline {
	length := len(skeleton.X.Values)
	xPolynomials := make([]Polynomial, length-1)
	yPolynomials := make([]Polynomial, length-1)
	for i := 0; i < length-1; i++ {
		xPolynomials[i] = NewQuinticHermite(skeleton.X.Values[i], skeleton.X.Values[i+1], skeleton.X.VValues[i], skeleton.X.VValues[i+1], skeleton.X.AValues[i], skeleton.X.AValues[i+1])
		yPolynomials[i] = NewQuinticHermite(skeleton.Y.Values[i], skeleton.Y.Values[i+1], skeleton.Y.VValues[i], skeleton.Y.VValues[i+1], skeleton.Y.AValues[i], skeleton.Y.AValues[i+1])
	}

	points := make([]Point, length)
	for i := 0; i < length; i++ {
		points[i] = Point{skeleton.X.Values[i], skeleton.Y.Values[i]}
	}

	return NewSpline(points, xPolynomials, yPolynomials)
}
