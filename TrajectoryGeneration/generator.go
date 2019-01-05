package main

import (
	"./trajectory"
	"fmt"
	"math"
	"io/ioutil"
)

func main() {
	fastRobot := &trajectory.SkidSteerRobot{
		Acceleration:        7.0,
		Deceleration:        7.0,
		MaxVelocity:         10,
		AngularAcceleration: math.Pi,
	}

	//slowRobot := &trajectory.SkidSteerRobot{
	//	Acceleration:        4,
	//	Deceleration:        4,
	//	MaxVelocity:         4.5,
	//	AngularAcceleration: math.Pi,
	//}

	centerToSwitchLeft := trajectory.SplineFor2(trajectory.Point2{0, 0, 90, 0.5}, trajectory.Point2{-5.035, 9.5, 90, 0.5})
	switchLeftToCenter := trajectory.SplineFor2(trajectory.Point2{-5.035, 9.5, 270, 0.5}, trajectory.Point2{-1.33, 2.0, 270, 0.5})
	switchLeftFromPyramid := trajectory.SplineFor2(trajectory.Point2{-1.33, 3.0, 90, 0.5}, trajectory.Point2{-5.035, 9.5, 90, 0.5})
	//scaleLeftFromPyramid := trajectory.SplineFor2(trajectory.Point2{-5.035, 9.0, 90, 0.5}, trajectory.Point2{-8.0, 11.0, 270, 0.5})

	writeProfileFileFor("CenterToSwitchLeft", fastRobot, 1000, 90.0, 90.0, centerToSwitchLeft)
	writeProfileFileFor("SwitchLeftToCenter", fastRobot, 1000, 270.0, 270.0, switchLeftToCenter)
	writeProfileFileFor("SwitchLeftFromPyramid", fastRobot, 1000, 90.0, 90.0, switchLeftFromPyramid)
	//writeProfileFileFor("ScaleLeftFromPyramid", fastRobot, 1000, 90.0, 270.0, trajectory.Point{-5.035, 9.0}, trajectory.Point{-8.0, 11.0})
	//writeProfileFileFor("CenterToSwitchRight", fastRobot, 1000, 90.0, 90.0, trajectory.Point{0, 0}, trajectory.Point{5.035, 9.5})
	//writeProfileFileFor("Forward5p5", slowRobot, 1000, 90.0, 90.0, trajectory.Point{0, 0}, trajectory.Point{0.0, 4.5})
	//writeProfileFileFor("Backward5p5", slowRobot, 1000, 270.0, 270.0, trajectory.Point{0.0, 4.5}, trajectory.Point{0.0, 0.0})
	//
	//writeProfileFileFor("CenterToSwitchRight", fastRobot, 1000, 90.0, 90.0, trajectory.Point{0, 0}, trajectory.Point{3.0, 9.5})

	//writeProfileFileFor("CenterToSwitchLeft", fastRobot, 1000, 90.0, 180.0, trajectory.Point{0, 0}, trajectory.Point{-10.0, 10.0})

}

const formatString = `package org.usfirst.frc.team1619.robot.trajectories;

import org.usfirst.frc.team1619.robot.framework.trajectory.TrajectoryData;

public class %v extends TrajectoryData {
	public %v() {
		super(
			%v,
			%v,
			%v,
			new double[]{%v},
			new double[]{%v},
			new double[]{%v},
			new double[]{%v},
			new double[]{%v}
		);
	}
}
`

func writeProfileFileFor(className string, robot *trajectory.SkidSteerRobot, resolution int, startHeadingDegrees, endHeadingDegrees float64, spline *trajectory.Spline) {
	profile := trajectory.NewVelocityProfile(robot, spline)

	profile.Calculate(resolution)

	fmt.Println(className)
	fmt.Println(spline)
	fmt.Println(profile)
	fmt.Println()

	timeStr := ""
	distanceStr := ""
	velocityStr := ""
	headingStr := ""
	curvatureStr := ""
	for i := 0; i <= resolution; i++ {
		timeStr += fmt.Sprintf("%G,", profile.TimeValues[i])
		distanceStr += fmt.Sprintf("%G,", profile.DistanceValues[i])
		velocityStr += fmt.Sprintf("%G,", profile.VelocityValues[i])
		headingStr += fmt.Sprintf("%G,", profile.HeadingValues[i])
		curvatureStr += fmt.Sprintf("%G,", profile.CurvatureValues[i])
	}

	classStr := fmt.Sprintf(formatString, className, className, resolution, startHeadingDegrees, endHeadingDegrees, timeStr, distanceStr, velocityStr, headingStr, curvatureStr)
	ioutil.WriteFile(fmt.Sprintf("./trajectories/%v.java", className), []byte(classStr), 777)
}
