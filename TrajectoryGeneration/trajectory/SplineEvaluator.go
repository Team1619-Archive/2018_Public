package trajectory

import "math"

type SplineEvaluator struct {
	spline                *Spline
	arcLengthApproximator *LinearApproximator
	arcLength             float64
}

func NewSplineEvaluator(spline *Spline, resolution int) SplineEvaluator {
	tValues := make([]float64, resolution+1)
	arcLengthValues := make([]float64, resolution+1)

	tValues[0] = 0
	arcLengthValues[0] = 0

	derivative := spline.EvaluateFirstDerivative(0)
	previousArcLengthDerivative := math.Sqrt(math.Pow(derivative.X, 2) + math.Pow(derivative.Y, 2))

	for i := 1; i <= resolution; i++ {
		t := float64(i) / float64(resolution) * spline.DomainMax()

		derivative = spline.EvaluateFirstDerivative(t)
		arcLengthDerivative := math.Sqrt(math.Pow(derivative.X, 2) + math.Pow(derivative.Y, 2))

		tValues[i] = t
		arcLengthValues[i] = arcLengthValues[i-1] + spline.DomainMax()/float64(resolution)*(arcLengthDerivative+previousArcLengthDerivative)/2
		previousArcLengthDerivative = arcLengthDerivative
	}

	arcLengthApproximator := &LinearApproximator{tValues, arcLengthValues}

	return SplineEvaluator{spline, arcLengthApproximator, arcLengthValues[len(arcLengthValues)-1]}
}

func (evaluator *SplineEvaluator) ArcLength() float64 {
	return evaluator.arcLength
}

func (evaluator *SplineEvaluator) BendingEnergy(resolution int) float64 {
	distanceDelta := evaluator.arcLength / float64(resolution)

	index := 0
	energy := 0.0
	for i := 0; i <= resolution; i++ {
		index = evaluator.arcLengthApproximator.findIndexForValue(float64(i)*distanceDelta, index)
		t := evaluator.arcLengthApproximator.evaluateParameter(float64(i)*distanceDelta, index)

		energy += math.Pow(evaluator.spline.EvaluateCurvature(t), 2) * distanceDelta
	}

	return energy
}
