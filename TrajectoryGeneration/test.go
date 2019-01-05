package main

import (
	"./learning"
	"./trajectory"
	"math/rand"
	"time"
	"math"
	"fmt"
	"github.com/NOX73/go-neural/persist"
)

func findMaxIndex(input []float64) int {
	index := 0
	for i := 1; i < len(input); i++ {
		if input[i] > input[index] {
			index = i
		}
	}

	return index
}

func main() {
	rand.Seed(int64(time.Now().Nanosecond()))

	network := persist.FromFile("./network.json")

	robot := &trajectory.SkidSteerRobot{6, 6, 8, math.Pi}

	sum1 := 0.0
	negativeSum1 := 0.0
	negativeCount1 := 0
	min1 := 0.0
	max1 := 0.0

	fmt.Println("------------------------------------------------------------------------------------------------------------------------------------")
	fmt.Println("--- Tests on optimal paths ---------------------------------------------------------------------------------------------------------")
	fmt.Println("------------------------------------------------------------------------------------------------------------------------------------")

	for i := 0; i < 500; i++ {
		fmt.Printf("%d/500\n", i+1)

		environment := learning.NewRandomRealEnvironment()
		spline := environment.Spline()
		profile := trajectory.NewVelocityProfile(robot, spline)

		profile.Calculate(500)

		time := profile.Time()
		if math.IsNaN(time) {
			i--
			continue
		}

		actions := []int{0, 0, 0, 0, 0, 0, 0, 0}
		for j := 0; j < 12; j++ {
			qVals := network.Calculate(environment.State())
			action := findMaxIndex(qVals)
			actions[action]++

			environment.Manipulate(action)
		}

		spline = environment.Spline()
		profile = trajectory.NewVelocityProfile(robot, spline)

		profile.Calculate(500)

		if math.IsNaN(profile.Time()) {
			i--
			continue
		}

		fmt.Println(spline)
		fmt.Printf("Time before: %.4f\n", profile.Time())

		delta := profile.Time() - time
		percentage := 100 * delta / time
		sum1 += percentage
		if percentage < 0 {
			negativeSum1 += percentage
			negativeCount1++
		}
		if percentage < min1 {
			min1 = percentage
		} else if percentage > max1 {
			max1 = percentage
		}

		fmt.Println(spline)
		fmt.Printf("Time after: %.4f\nDelta: %.4f\nPercentage change: %.4f\n", profile.Time(), delta, percentage)
		fmt.Println()
	}

	sum2 := 0.0
	negativeSum2 := 0.0
	negativeCount2 := 0
	min2 := 0.0
	max2 := 0.0

	fmt.Println("------------------------------------------------------------------------------------------------------------------------------------")
	fmt.Println("--- Tests on altered paths ---------------------------------------------------------------------------------------------------------")
	fmt.Println("------------------------------------------------------------------------------------------------------------------------------------")

	for i := 0; i < 500; i++ {
		fmt.Printf("%d/500\n", i+1)

		environment := learning.NewRandomRealEnvironment()

		for j := 0; j < 12; j++ {
			environment.Manipulate(rand.Intn(8))
		}

		spline := environment.Spline()
		profile := trajectory.NewVelocityProfile(robot, spline)

		profile.Calculate(500)

		time := profile.Time()
		if math.IsNaN(time) {
			i--
			continue
		}

		actions := []int{0, 0, 0, 0, 0, 0, 0, 0}
		for j := 0; j < 12; j++ {
			qVals := network.Calculate(environment.State())
			action := findMaxIndex(qVals)
			actions[action]++

			environment.Manipulate(action)
		}

		spline = environment.Spline()
		profile = trajectory.NewVelocityProfile(robot, spline)

		profile.Calculate(500)

		if math.IsNaN(profile.Time()) {
			i--
			continue
		}

		fmt.Println(spline)
		fmt.Printf("Time before: %.4f\n", profile.Time())

		delta := profile.Time() - time
		percentage := 100 * delta / time
		sum2 += percentage
		if percentage < 0 {
			negativeSum2 += percentage
			negativeCount2++
		}
		if percentage < min2 {
			min2 = percentage
		} else if percentage > max2 {
			max2 = percentage
		}

		fmt.Println(spline)
		fmt.Printf("Time after: %.4f\nDelta: %.4f\nPercentage change: %.4f\n", profile.Time(), delta, percentage)
		fmt.Println()
	}

	fmt.Println("------------------------------------------------------------------------------------------------------------------------------------")
	fmt.Println("------------------------------------------------------------------------------------------------------------------------------------")
	fmt.Println()
	fmt.Println("Tests on optimal paths:")
	fmt.Printf("Average: %.4f\nAverage of negative: %.4f\nMin: %.4f\nMax: %.4f\n", sum1/500, negativeSum1/float64(negativeCount1), min1, max1)
	fmt.Println()
	fmt.Println("Tests on altered paths:")
	fmt.Printf("Average: %.4f\nAverage of negative: %.4f\nMin: %.4f\nMax: %.4f\n", sum2/500, negativeSum2/float64(negativeCount2), min2, max2)
}
