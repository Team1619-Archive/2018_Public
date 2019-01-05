package trajectory

import (
	"fmt"
	"math"
)

type VelocityProfile struct {
	robot                                *SkidSteerRobot
	spline                               *Spline
	arcLengthApproximator                *LinearApproximator
	DistanceValues, VelocityValues       []float64
	HeadingValues, AngularVelocityValues []float64
	TimeValues                           []float64
	CurvatureValues						 []float64
	StringValue                          string
}

func NewVelocityProfile(robot *SkidSteerRobot, spline *Spline) *VelocityProfile {
	return &VelocityProfile{robot, spline, nil, nil, nil, nil, nil, nil, nil,""}
}

func (velocityProfile *VelocityProfile) Calculate(resolution int) {
	tValues := make([]float64, resolution+1)
	arcLengthValues := make([]float64, resolution+1)

	tValues[0] = 0
	arcLengthValues[0] = 0

	derivative := velocityProfile.spline.EvaluateFirstDerivative(0)
	previousArcLengthDerivative := math.Sqrt(math.Pow(derivative.X, 2) + math.Pow(derivative.Y, 2))

	for i := 1; i <= resolution; i++ {
		t := float64(i) / float64(resolution) * velocityProfile.spline.DomainMax()

		derivative = velocityProfile.spline.EvaluateFirstDerivative(t)
		arcLengthDerivative := math.Sqrt(math.Pow(derivative.X, 2) + math.Pow(derivative.Y, 2))

		tValues[i] = t
		arcLengthValues[i] = arcLengthValues[i-1] + velocityProfile.spline.DomainMax()/float64(resolution)*(arcLengthDerivative+previousArcLengthDerivative)/2
		previousArcLengthDerivative = arcLengthDerivative
	}

	arcLengthApproximator := LinearApproximator{tValues, arcLengthValues}
	distance := arcLengthValues[resolution]
	distanceDelta := distance / float64(resolution)

	tValues = make([]float64, resolution+1)
	distanceValues := make([]float64, resolution+1)
	curvatureValues := make([]float64, resolution+1)
	velocityValues := make([]float64, resolution+1)
	headingValues := make([]float64, resolution+1)

	tValues[0] = 0
	distanceValues[0] = 0
	curvatureValues[0] = 0
	velocityValues[0] = 0
	headingValues[0] = velocityProfile.spline.EvaluateHeading(0)

	index := 0
	for i := 1; i <= resolution; i++ {
		distanceValues[i] = float64(i) * distanceDelta
		index = arcLengthApproximator.findIndexForValue(distanceValues[i], index)
		tValues[i] = arcLengthApproximator.evaluateParameter(distanceValues[i], index)

		curvatureValues[i] = velocityProfile.spline.EvaluateCurvature(tValues[i])

		var threshold float64
		if curvatureValues[i] > 0 {
			if curvatureValues[i-1] >= 0 {
				if curvatureValues[i] > curvatureValues[i-1] {
					threshold = math.Sqrt(2 * distanceDelta * math.Pow(velocityProfile.robot.AngularAcceleration+curvatureValues[i]*velocityProfile.robot.Acceleration, 2) / ((velocityProfile.robot.Acceleration*(curvatureValues[i]+curvatureValues[i-1]) + 2*velocityProfile.robot.AngularAcceleration) * (curvatureValues[i] - curvatureValues[i-1])))
				} else if curvatureValues[i] < curvatureValues[i-1] {
					thresh1 := math.Sqrt(8 * curvatureValues[i] * velocityProfile.robot.AngularAcceleration * distanceDelta / math.Pow(curvatureValues[i-1]+curvatureValues[i], 2))
					tmp1 := math.Sqrt(4 * curvatureValues[i] * distanceDelta * (curvatureValues[i]*velocityProfile.robot.Acceleration + velocityProfile.robot.AngularAcceleration) / math.Pow(curvatureValues[i-1]-curvatureValues[i], 2))
					tmp2 := math.Sqrt(2 * distanceDelta * math.Pow(curvatureValues[i]*velocityProfile.robot.Acceleration+velocityProfile.robot.AngularAcceleration, 2) / ((curvatureValues[i-1] - curvatureValues[i]) * (2*velocityProfile.robot.AngularAcceleration + (curvatureValues[i-1]+curvatureValues[i])*velocityProfile.robot.Acceleration)))
					threshTmp1 := math.Min(tmp1, tmp2)
					threshTmp2 := math.Min(math.Sqrt(2*velocityProfile.robot.AngularAcceleration*distanceDelta/curvatureValues[i-1]), math.Sqrt(2*velocityProfile.robot.Acceleration*distanceDelta))
					threshTmp3 := math.Inf(-1)
					tmp := math.Min(2*velocityProfile.robot.AngularAcceleration*distanceDelta/curvatureValues[i-1], 2*distanceDelta*math.Pow(curvatureValues[i]*velocityProfile.robot.Acceleration-velocityProfile.robot.AngularAcceleration, 2)/((curvatureValues[i-1]-curvatureValues[i])*(2*velocityProfile.robot.AngularAcceleration-(curvatureValues[i-1]+curvatureValues[i])*velocityProfile.robot.Acceleration)))
					if tmp > -4*curvatureValues[i]*distanceDelta*(curvatureValues[i]*velocityProfile.robot.Acceleration-velocityProfile.robot.AngularAcceleration)/((curvatureValues[i-1]-curvatureValues[i])*(curvatureValues[i-1]+curvatureValues[i])) && tmp > 2*velocityProfile.robot.Acceleration*distanceDelta {
						threshTmp3 = math.Sqrt(tmp)
					}

					threshold = math.Max(math.Max(thresh1, threshTmp1), math.Max(threshTmp2, threshTmp3))
				} else {
					threshold = math.Inf(1)
				}
			} else {
				vonestarpos := math.Sqrt(-2 * distanceDelta * velocityProfile.robot.AngularAcceleration / curvatureValues[i-1])
				precond := math.Inf(1)
				if curvatureValues[i-1]+curvatureValues[i] > 0 {
					precond = math.Sqrt(-4 * curvatureValues[i] * distanceDelta * (velocityProfile.robot.AngularAcceleration + curvatureValues[i]*velocityProfile.robot.Acceleration) / ((curvatureValues[i-1] - curvatureValues[i]) * (curvatureValues[i-1] + curvatureValues[i])))
				}
				threshTmp := math.Min(precond, math.Sqrt(-2*distanceDelta*math.Pow(velocityProfile.robot.AngularAcceleration+curvatureValues[i]*velocityProfile.robot.Acceleration, 2)/((curvatureValues[i-1]-curvatureValues[i])*((curvatureValues[i-1]+curvatureValues[i])*velocityProfile.robot.Acceleration+2*velocityProfile.robot.AngularAcceleration))))
				threshTmp = math.Max(threshTmp, math.Sqrt(2*distanceDelta*velocityProfile.robot.Acceleration))
				threshold = math.Min(threshTmp, vonestarpos)
			}
		} else if curvatureValues[i] < 0 {
			if curvatureValues[i-1] <= 0 {
				if curvatureValues[i] > curvatureValues[i-1] {
					thresh1 := math.Sqrt(-8 * curvatureValues[i] * velocityProfile.robot.AngularAcceleration * distanceDelta / math.Pow(curvatureValues[i-1]+curvatureValues[i], 2))
					tmp1 := math.Sqrt(-4 * curvatureValues[i] * distanceDelta * (velocityProfile.robot.AngularAcceleration - curvatureValues[i]*velocityProfile.robot.Acceleration) / ((curvatureValues[i-1] + curvatureValues[i]) * (curvatureValues[i-1] - curvatureValues[i])))
					tmp2 := math.Sqrt(-2 * distanceDelta * math.Pow(velocityProfile.robot.AngularAcceleration-curvatureValues[i]*velocityProfile.robot.Acceleration, 2) / ((curvatureValues[i-1] - curvatureValues[i]) * (2*velocityProfile.robot.AngularAcceleration - (curvatureValues[i-1]+curvatureValues[i])*velocityProfile.robot.Acceleration)))
					threshTmp1 := math.Min(tmp1, tmp2)
					threshTmp2 := math.Min(math.Sqrt(-2*velocityProfile.robot.AngularAcceleration*distanceDelta/curvatureValues[i-1]), math.Sqrt(2*velocityProfile.robot.Acceleration*distanceDelta))
					threshTmp3 := math.Inf(-1)
					tmp := math.Min(-2*velocityProfile.robot.AngularAcceleration*distanceDelta/curvatureValues[i-1], -2*distanceDelta*math.Pow(velocityProfile.robot.AngularAcceleration+curvatureValues[i]*velocityProfile.robot.Acceleration, 2)/((curvatureValues[i-1]-curvatureValues[i])*(2*velocityProfile.robot.AngularAcceleration+(curvatureValues[i-1]+curvatureValues[i])*velocityProfile.robot.Acceleration)))
					if tmp > -4*curvatureValues[i]*distanceDelta*(velocityProfile.robot.AngularAcceleration+curvatureValues[i]*velocityProfile.robot.Acceleration)/((curvatureValues[i-1]-curvatureValues[i])*(curvatureValues[i-1]+curvatureValues[i])) && tmp > 2*velocityProfile.robot.Acceleration*distanceDelta {
						threshTmp3 = math.Sqrt(tmp)
					}

					threshold = math.Max(math.Max(thresh1, threshTmp1), math.Max(threshTmp2, threshTmp3))
				} else if curvatureValues[i] < curvatureValues[i-1] {
					threshold = math.Sqrt(-2 * distanceDelta * math.Pow(velocityProfile.robot.AngularAcceleration-curvatureValues[i]*velocityProfile.robot.Acceleration, 2) / ((curvatureValues[i-1] - curvatureValues[i]) * ((curvatureValues[i]+curvatureValues[i-1])*velocityProfile.robot.Acceleration - 2*velocityProfile.robot.AngularAcceleration)))
				} else {
					threshold = math.Inf(1)
				}
			} else {
				vtwostarpos := math.Sqrt(2 * distanceDelta * velocityProfile.robot.AngularAcceleration / curvatureValues[i-1])
				precond := math.Inf(1)
				if curvatureValues[i-1]+curvatureValues[i] < 0 {
					precond = math.Sqrt(-4 * curvatureValues[i] * distanceDelta * (curvatureValues[i]*velocityProfile.robot.Acceleration - velocityProfile.robot.AngularAcceleration) / ((curvatureValues[i-1] - curvatureValues[i]) * (curvatureValues[i-1] + curvatureValues[i])))
				}
				threshTmp := math.Min(precond, math.Sqrt(-2*distanceDelta*math.Pow(curvatureValues[i]*velocityProfile.robot.Acceleration-velocityProfile.robot.AngularAcceleration, 2)/((curvatureValues[i-1]-curvatureValues[i])*((curvatureValues[i-1]+curvatureValues[i])*velocityProfile.robot.Acceleration-2*velocityProfile.robot.AngularAcceleration))))
				threshTmp = math.Max(threshTmp, math.Sqrt(2*distanceDelta*velocityProfile.robot.Acceleration))
				threshold = math.Min(threshTmp, vtwostarpos)
			}
		} else {
			if curvatureValues[i-1] > 0 {
				vtwohatpos := math.Sqrt(2 * distanceDelta * velocityProfile.robot.AngularAcceleration / curvatureValues[i-1])
				threshTmp := math.Max(math.Sqrt(2*distanceDelta*velocityProfile.robot.Acceleration), math.Sqrt(-2*distanceDelta*math.Pow(velocityProfile.robot.AngularAcceleration, 2)/(curvatureValues[i-1]*(curvatureValues[i-1]*velocityProfile.robot.Acceleration-2*velocityProfile.robot.AngularAcceleration))))
				threshold = math.Min(vtwohatpos, threshTmp)
			} else if curvatureValues[i-1] < 0 {
				vonehatpos := math.Sqrt(-2 * distanceDelta * velocityProfile.robot.AngularAcceleration / curvatureValues[i-1])
				threshTmp := math.Max(math.Sqrt(2*distanceDelta*velocityProfile.robot.Acceleration), math.Sqrt(-2*distanceDelta*math.Pow(velocityProfile.robot.AngularAcceleration, 2)/(curvatureValues[i-1]*(curvatureValues[i-1]*velocityProfile.robot.Acceleration+2*velocityProfile.robot.AngularAcceleration))))
				threshold = math.Min(vonehatpos, threshTmp)
			} else {
				threshold = math.Inf(1)
			}
		}

		velocityValues[i-1] = math.Min(threshold, velocityValues[i-1])

		maxVelocityForAcceleration := math.Sqrt(math.Pow(velocityValues[i-1], 2) + 2*velocityProfile.robot.Acceleration*distanceDelta)
		var maxVelocityForAngularAcceleration float64

		a := curvatureValues[i]
		b1 := (curvatureValues[i-1] - curvatureValues[i]) * velocityValues[i-1]
		b := (curvatureValues[i] + curvatureValues[i-1]) * velocityValues[i-1]
		c := 2 * distanceDelta * velocityProfile.robot.AngularAcceleration

		if curvatureValues[i] == 0 {
			if curvatureValues[i-1] == 0 {
				maxVelocityForAngularAcceleration = math.Inf(1)
			} else if curvatureValues[i-1] < 0 {
				maxVelocityForAngularAcceleration = -(2 * distanceDelta * velocityProfile.robot.AngularAcceleration)/(curvatureValues[i-1]*velocityValues[i-1]) - velocityValues[i-1]
			} else {
				maxVelocityForAngularAcceleration = (2*distanceDelta*velocityProfile.robot.AngularAcceleration)/(curvatureValues[i-1]*velocityValues[i-1]) - velocityValues[i-1]
			}
		} else if curvatureValues[i] < 0 {
			maxVelocityForAngularAcceleration = (b1 - math.Sqrt(math.Pow(b, 2)-4*a*c)) / (2 * a)
		} else {
			maxVelocityForAngularAcceleration = (b1 + math.Sqrt(math.Pow(b, 2)+4*a*c)) / (2 * a)
		}

		velocityValues[i] = math.Min(math.Min(maxVelocityForAcceleration, maxVelocityForAngularAcceleration), velocityProfile.robot.MaxVelocity)
		headingValues[i] = velocityProfile.spline.EvaluateHeading(tValues[i])
	}

	angularVelocityValues := make([]float64, resolution+1)

	tValues[resolution] = velocityProfile.spline.DomainMax()
	distanceValues[resolution] = distance
	curvatureValues[resolution] = 0
	velocityValues[resolution] = 0
	angularVelocityValues[resolution] = 0

	for i := resolution - 1; i >= 0; i-- {
		maxVelocityForAcceleration := math.Sqrt(math.Pow(velocityValues[i+1], 2) + 2*velocityProfile.robot.Deceleration*distanceDelta)
		var maxVelocityForAngularAcceleration float64

		a := curvatureValues[i]
		b1 := (curvatureValues[i+1] - curvatureValues[i]) * velocityValues[i+1]
		b := (curvatureValues[i] + curvatureValues[i+1]) * velocityValues[i+1]
		c := 2 * distanceDelta * velocityProfile.robot.AngularAcceleration

		if curvatureValues[i] == 0 {
			if curvatureValues[i+1] == 0 {
				maxVelocityForAngularAcceleration = math.Inf(1)
			} else if curvatureValues[i+1] < 0 {
				maxVelocityForAngularAcceleration = -(2 * distanceDelta * velocityProfile.robot.AngularAcceleration)/(curvatureValues[i+1]*velocityValues[i+1]) - velocityValues[i+1]
			} else {
				maxVelocityForAngularAcceleration = (2*distanceDelta*velocityProfile.robot.AngularAcceleration)/(curvatureValues[i+1]*velocityValues[i+1]) - velocityValues[i+1]
			}
		} else if curvatureValues[i] < 0 {
			maxVelocityForAngularAcceleration = (b1 - math.Sqrt(math.Pow(b, 2)-4*a*c)) / (2 * a)
		} else {
			maxVelocityForAngularAcceleration = (b1 + math.Sqrt(math.Pow(b, 2)+4*a*c)) / (2 * a)
		}

		velocityValues[i] = math.Min(math.Min(maxVelocityForAcceleration, maxVelocityForAngularAcceleration), velocityValues[i])
		angularVelocityValues[i] = velocityValues[i] * curvatureValues[i]
	}

	timeValues := make([]float64, resolution+1)
	timeValues[0] = 0
	for i := 1; i <= resolution; i++ {
		timeValues[i] = timeValues[i-1] + 2*distanceDelta/(velocityValues[i-1]+velocityValues[i])
	}

	maxAngularAcceleration := 0.0
	for i := 1; i <= resolution; i++ {
		angularAcceleration := (angularVelocityValues[i] - angularVelocityValues[i-1]) / (timeValues[i] - timeValues[i-1])
		if math.Abs(angularAcceleration) > math.Abs(maxAngularAcceleration) {
			maxAngularAcceleration = angularAcceleration
		}
	}
	//fmt.Printf("Maximum angular acceleration: %.4f\n", maxAngularAcceleration)

	velocityProfile.arcLengthApproximator = &arcLengthApproximator
	velocityProfile.TimeValues = timeValues
	velocityProfile.DistanceValues = distanceValues
	velocityProfile.VelocityValues = velocityValues
	velocityProfile.HeadingValues = headingValues
	velocityProfile.AngularVelocityValues = angularVelocityValues
	velocityProfile.CurvatureValues = curvatureValues

	velocityProfile.StringValue = velocityProfile.String()
}

func (velocityProfile *VelocityProfile) Distance() float64 {
	return velocityProfile.DistanceValues[len(velocityProfile.DistanceValues)-1]
}

func (velocityProfile *VelocityProfile) Time() float64 {
	return velocityProfile.TimeValues[len(velocityProfile.TimeValues)-1]
}

func (velocityProfile *VelocityProfile) MeanCurvatureMagnitude(resolution int) float64 {
	distance := velocityProfile.DistanceValues[len(velocityProfile.DistanceValues)-1]
	distanceDelta := distance / float64(resolution)

	index := 0
	sum := 0.0
	for i := 0; i <= resolution; i++ {
		index = velocityProfile.arcLengthApproximator.findIndexForValue(float64(i)*distanceDelta, index)
		t := velocityProfile.arcLengthApproximator.evaluateParameter(float64(i)*distanceDelta, index)

		sum += math.Abs(velocityProfile.spline.EvaluateCurvature(t))
	}

	return sum / float64(resolution)
}

func (velocityProfile *VelocityProfile) BendingEnergy(resolution int) float64 {
	distance := velocityProfile.DistanceValues[len(velocityProfile.DistanceValues)-1]
	distanceDelta := distance / float64(resolution)

	index := 0
	energy := 0.0
	for i := 0; i <= resolution; i++ {
		index = velocityProfile.arcLengthApproximator.findIndexForValue(float64(i)*distanceDelta, index)
		t := velocityProfile.arcLengthApproximator.evaluateParameter(float64(i)*distanceDelta, index)

		energy += math.Pow(velocityProfile.spline.EvaluateCurvature(t), 2) * distanceDelta
	}

	return energy
}

func (velocityProfile *VelocityProfile) String() string {
	velocityPoints := ""
	angularVelocityPoints := ""
	for i := 0; i < len(velocityProfile.TimeValues); i++ {
		velocityPoints += fmt.Sprintf("(%.4f,%.4f),", velocityProfile.TimeValues[i], velocityProfile.VelocityValues[i])
		angularVelocityPoints += fmt.Sprintf("(%.4f,%.4f),", velocityProfile.TimeValues[i], velocityProfile.AngularVelocityValues[i])
	}
	velocityPoints = string([]rune(velocityPoints)[:len(velocityPoints)-1])
	angularVelocityPoints = string([]rune(angularVelocityPoints)[:len(angularVelocityPoints)-1])

	return velocityPoints + "\n" + angularVelocityPoints
}
