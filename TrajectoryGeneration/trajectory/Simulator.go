package trajectory

import (
	"fmt"
	"math"
)

func RunSimulation(robot *SkidSteerRobot, profile *VelocityProfile) string {
	points := ""

	x := 0.0
	y := 0.0

	points += fmt.Sprintf("(%.4f,%.4f),", x, y)

	for i := 1; i < len(profile.TimeValues); i++ {
		time := profile.TimeValues[i] - profile.TimeValues[i-1]
		averageVelocity := (profile.VelocityValues[i-1] + profile.VelocityValues[i]) / 2

		heading := profile.HeadingValues[i]
		x += averageVelocity * math.Cos(heading*math.Pi/180) * time
		y += averageVelocity * math.Sin(heading*math.Pi/180) * time

		points += fmt.Sprintf("(%.4f,%.4f),", x, y)
	}

	points = string([]rune(points)[:len(points)-1])

	return points
}
