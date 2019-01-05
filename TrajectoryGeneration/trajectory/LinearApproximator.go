package trajectory

import "math"

type LinearApproximator struct {
	parameterValues, values []float64
}

func (approximator *LinearApproximator) findIndexForParameter(parameter float64, approximateIndex int) int {
	index := approximateIndex
	if approximator.parameterValues[index] > parameter {
		for approximator.parameterValues[index] > parameter {
			if index == 0 {
				return -1
			}

			index--
		}
	} else {
		for approximator.parameterValues[index] < parameter {
			if index == len(approximator.parameterValues)-1 {
				return index
			}

			index++
		}

		index-- // Point the index to the parameter value just smaller than the target parameter
	}

	return index
}

func (approximator *LinearApproximator) findIndexForValue(value float64, approximateIndex int) int {
	index := int(math.Max(float64(approximateIndex), 0))
	if approximator.values[index] > value {
		for approximator.values[index] > value {
			if index == 0 {
				return -1
			}

			index--
		}
	} else {
		for approximator.values[index] < value {
			if index == len(approximator.values)-1 {
				return index
			}

			index++
		}

		index-- // Point the index to the value value just smaller than the target parameter
	}

	return index
}

func (approximator *LinearApproximator) evaluateValue(parameter float64, index int) float64 {
	if index == -1 {
		return approximator.values[0]
	} else if index == len(approximator.parameterValues)-1 {
		return approximator.values[len(approximator.parameterValues)-1]
	}

	x := (parameter - approximator.parameterValues[index]) / (approximator.parameterValues[index+1] - approximator.parameterValues[index])
	return x*(approximator.values[index+1]-approximator.values[index]) + approximator.values[index]
}

func (approximator *LinearApproximator) evaluateParameter(value float64, index int) float64 {
	if index == -1 {
		return approximator.parameterValues[0]
	} else if index == len(approximator.values)-1 {
		return approximator.parameterValues[len(approximator.parameterValues)-1]
	}

	x := (value - approximator.values[index]) / (approximator.values[index+1] - approximator.values[index])
	return x*(approximator.parameterValues[index+1]-approximator.parameterValues[index]) + approximator.parameterValues[index]
}
