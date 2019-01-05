package main

import (
	"math/rand"
	"./trajectory"
	"math"
	"fmt"
)

func main() {
	rand.Seed(498327984732)

	resolution := 1000

	robot := &trajectory.SkidSteerRobot{6, 6, 8, math.Pi}

	distances := make([]float64, resolution)
	meanCurvatureMagnitudes := make([]float64, resolution)
	energies := make([]float64, resolution)
	times := make([]float64, resolution)

	for i := 0; i < resolution; i++ {
		length := rand.Intn(3) + 3
		points := make([]trajectory.Point, length)

		initialHeading := rand.Float64() * 360

		position := trajectory.Point{0, 0}
		heading := initialHeading

		points[0] = position
		for j := 1; j < length; j++ {
			distance := 5 + 10*rand.Float64()
			direction := heading + 91*rand.Float64() - 45

			position.X += math.Cos(direction*math.Pi/180) * distance
			position.Y += math.Sin(direction*math.Pi/180) * distance

			heading = direction + 181*rand.Float64() - 90

			points[j] = position
		}

		spline := trajectory.SplineFor(initialHeading, heading, points...)
		profile := trajectory.NewVelocityProfile(robot, spline)

		profile.Calculate(250)
		if math.IsNaN(profile.Time()) {
			i--
			continue
		}

		distances[i] = profile.Distance()
		meanCurvatureMagnitudes[i] = profile.MeanCurvatureMagnitude(250) * profile.Distance()
		energies[i] = profile.BendingEnergy(250)
		times[i] = profile.Time()

		fmt.Println(spline)
	}

	distancesString := ""
	meanCurvatureMagnitudesString := ""
	energiesString := ""
	for i := 0; i < resolution; i++ {
		distancesString += fmt.Sprintf("(%.4f,%.4f),", distances[i], times[i])
		meanCurvatureMagnitudesString += fmt.Sprintf("(%.4f,%.4f),", meanCurvatureMagnitudes[i], times[i])
		energiesString += fmt.Sprintf("(%.4f,%.4f),", energies[i], times[i])
	}

	fmt.Println(string([]rune(distancesString)[:len(distancesString)-1]))
	fmt.Println(string([]rune(meanCurvatureMagnitudesString)[:len(meanCurvatureMagnitudesString)-1]))
	fmt.Println(string([]rune(energiesString)[:len(energiesString)-1]))

	distancesString = ""
	meanCurvatureMagnitudesString = ""
	energiesString = ""
	timesString := ""
	for i := 0; i < resolution; i++ {
		distancesString += fmt.Sprintf("%.8f,", distances[i])
		meanCurvatureMagnitudesString += fmt.Sprintf("%.8f,", meanCurvatureMagnitudes[i])
		energiesString += fmt.Sprintf("%.8f,", energies[i])
		timesString += fmt.Sprintf("%.8f,", times[i])
	}

	fmt.Println(string([]rune(distancesString)[:len(distancesString)-1]))
	fmt.Println(string([]rune(meanCurvatureMagnitudesString)[:len(meanCurvatureMagnitudesString)-1]))
	fmt.Println(string([]rune(energiesString)[:len(energiesString)-1]))
	fmt.Println(string([]rune(timesString)[:len(timesString)-1]))
}
