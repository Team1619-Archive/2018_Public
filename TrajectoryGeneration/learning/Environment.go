package learning

import (
	"../trajectory"
)

type Environment struct {
	skeleton                             *trajectory.SplineSkeleton
	previousArcLength, arcLength         float64
	previousBendingEnergy, bendingEnergy float64
}

func NewEnvironment(skeleton *trajectory.SplineSkeleton) *Environment {
	evaluator := trajectory.NewSplineEvaluator(trajectory.SplineForSkeleton(skeleton), 100)

	arcLength := evaluator.ArcLength()
	bendingEnergy := evaluator.BendingEnergy(100)

	return &Environment{skeleton, arcLength, arcLength, bendingEnergy, bendingEnergy}
}

func NewRandomEnvironment() *Environment {
	skeleton := trajectory.NewRandomSplineSkeleton(5, 5, 10)
	evaluator := trajectory.NewSplineEvaluator(trajectory.SplineForSkeleton(skeleton), 100)

	arcLength := evaluator.ArcLength()
	bendingEnergy := evaluator.BendingEnergy(100)

	return &Environment{skeleton, arcLength, arcLength, bendingEnergy, bendingEnergy}
}

func (environment *Environment) State() []float64 {
	return []float64{
		environment.skeleton.X.Values[1], environment.skeleton.Y.Values[1],
		environment.skeleton.X.VValues[1], environment.skeleton.Y.VValues[1],
		environment.skeleton.X.AValues[1], environment.skeleton.Y.AValues[1],
		environment.skeleton.X.Values[2], environment.skeleton.Y.Values[2],
		environment.skeleton.X.VValues[2], environment.skeleton.Y.VValues[2],
		environment.skeleton.X.AValues[2], environment.skeleton.Y.AValues[2],
		environment.skeleton.X.Values[3], environment.skeleton.Y.Values[3],
		environment.skeleton.X.VValues[3], environment.skeleton.Y.VValues[3],
		environment.skeleton.X.AValues[3], environment.skeleton.Y.AValues[3],
	}
}

func (environment *Environment) Manipulate(action int) {
	switch action {
	case 0:
		environment.skeleton.X.VValues[2] *= 1.1
		environment.skeleton.Y.VValues[2] *= 1.1
	case 1:
		environment.skeleton.X.AValues[2] *= 1.1
		environment.skeleton.Y.AValues[2] *= 1.1
	case 2:
		environment.skeleton.X.VValues[2] *= 0.9
		environment.skeleton.Y.VValues[2] *= 0.9
	case 3:
		environment.skeleton.X.AValues[2] *= 0.9
		environment.skeleton.Y.AValues[2] *= 0.9
	}

	environment.recalculate()
}

func (environment *Environment) Reward() float64 {
	return estimateTime(environment.previousArcLength, environment.previousBendingEnergy) - estimateTime(environment.arcLength, environment.bendingEnergy)
}

func (environment *Environment) recalculate() {
	evaluator := trajectory.NewSplineEvaluator(trajectory.SplineForSkeleton(environment.skeleton), 100)

	arcLength := evaluator.ArcLength()
	bendingEnergy := evaluator.BendingEnergy(100)

	environment.previousArcLength = environment.arcLength
	environment.previousBendingEnergy = environment.bendingEnergy

	environment.arcLength = arcLength
	environment.bendingEnergy = bendingEnergy
}

func estimateTime(arcLength, bendingEnergy float64) float64 {
	return 1.3202 + arcLength*0.1436 + bendingEnergy*0.54125
}
