package main

import (
	"github.com/NOX73/go-neural/persist"
	"./learning"
	"math/rand"
	"github.com/NOX73/go-neural/learn"
	"fmt"
	"time"
	"github.com/NOX73/go-neural"
)

func findMax(input []float64) float64 {
	max := input[0]
	for i := 1; i < len(input); i++ {
		if input[i] > max {
			max = input[i]
		}
	}

	return max
}

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

	network := neural.NewNetwork(18, []int{18, 200, 64, 8})
	network.RandomizeSynapses()

	epochs := 5000
	gamma := 0.9
	for i := 0; i < epochs; i++ {
		fmt.Printf("%d/%d\n", i+1, epochs)

		environment := learning.NewRandomRealEnvironment()

		epsilon := 1.0 - 0.75*float64(i)/float64(epochs)
		for j := 0; j < 15; j++ {
			qVals := network.Calculate(environment.State())

			var action int
			if rand.Float64() < epsilon {
				action = rand.Intn(8)
			} else {
				action = findMaxIndex(qVals)
			}

			environment.Manipulate(action)

			reward := environment.Reward()

			maxQ := findMax(network.Calculate(environment.State()))
			qVals[action] = reward + gamma*maxQ

			learn.Learn(network, environment.State(), qVals, 0.05)

			if epsilon > 0.1 {
				epsilon -= (1.0 - 0.75*float64(i)/float64(epochs)) / 15
			}
		}
	}

	persist.ToFile("./network.json", network)
}
