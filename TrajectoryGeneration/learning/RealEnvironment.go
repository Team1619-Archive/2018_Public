package learning

import (
	"../trajectory"
	"math"
)

var robot = &trajectory.SkidSteerRobot{6, 6, 8, math.Pi}

type RealEnvironment struct {
	skeleton           *trajectory.SplineSkeleton
	previousTime, time float64
}

func NewRealEnvironment(skeleton *trajectory.SplineSkeleton) *RealEnvironment {
	profile := trajectory.NewVelocityProfile(robot, trajectory.SplineForSkeleton(skeleton))
	profile.Calculate(1000)

	return &RealEnvironment{skeleton, profile.Time(), profile.Time()}
}

func NewRandomRealEnvironment() *RealEnvironment {
	skeleton := trajectory.NewRandomSplineSkeleton(5, 5, 10)

	return NewRealEnvironment(skeleton)
}

func (environment *RealEnvironment) Spline() *trajectory.Spline {
	return trajectory.SplineForSkeleton(environment.skeleton)
}

func (environment *RealEnvironment) State() []float64 {
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

func (environment *RealEnvironment) Manipulate(action int) {
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
	case 4:
		environment.adjustFirstDerivativeAngle(math.Pi / 10)
	case 5:
		environment.adjustFirstDerivativeAngle(-math.Pi / 10)
	case 6:
		environment.adjustSecondDerivativeAngle(math.Pi / 10)
	case 7:
		environment.adjustSecondDerivativeAngle(-math.Pi / 10)
	}

	environment.recalculate()
}

func (environment *RealEnvironment) Reward() float64 {
	return environment.previousTime - environment.time
}

func (environment *RealEnvironment) adjustFirstDerivativeAngle(delta float64) {
	angle := math.Atan2(environment.skeleton.Y.VValues[2], environment.skeleton.X.VValues[2])
	angle += delta

	magnitude := math.Sqrt(math.Pow(environment.skeleton.X.VValues[2], 2) + math.Pow(environment.skeleton.Y.VValues[2], 2))
	environment.skeleton.X.VValues[2] = magnitude * math.Cos(angle)
	environment.skeleton.Y.VValues[2] = magnitude * math.Sin(angle)
}

func (environment *RealEnvironment) adjustSecondDerivativeAngle(delta float64) {
	angle := math.Atan2(environment.skeleton.Y.AValues[2], environment.skeleton.X.AValues[2])
	angle += delta

	magnitude := math.Sqrt(math.Pow(environment.skeleton.X.AValues[2], 2) + math.Pow(environment.skeleton.Y.AValues[2], 2))
	environment.skeleton.X.AValues[2] = magnitude * math.Cos(angle)
	environment.skeleton.Y.AValues[2] = magnitude * math.Sin(angle)
}

func (environment *RealEnvironment) recalculate() {
	profile := trajectory.NewVelocityProfile(robot, trajectory.SplineForSkeleton(environment.skeleton))
	profile.Calculate(1000)

	if !math.IsNaN(profile.Time()) {
		environment.previousTime = environment.time
		environment.time = profile.Time()
	} else {
		environment.previousTime = environment.time
	}
}
