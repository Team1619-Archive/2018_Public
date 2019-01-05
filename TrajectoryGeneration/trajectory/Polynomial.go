package trajectory

import (
	"fmt"
	"math"
)

type Polynomial struct {
	coefficients []float64
}

func NewPolynomial(coefficients ...float64) Polynomial {
	return Polynomial{coefficients}
}

func (polynomial *Polynomial) Evaluate(parameter float64) float64 {
	result := 0.0
	for i := 0; i < len(polynomial.coefficients); i++ {
		result += math.Pow(parameter, float64(i)) * polynomial.coefficients[i]
	}

	return result
}

func (polynomial Polynomial) String() string {
	return polynomial.StringForParameter("X")
}

func (polynomial Polynomial) StringForParameter(parameter string) string {
	if len(polynomial.coefficients) == 0 {
		return "0"
	}

	stringPolynomial := ""
	for index, coefficient := range polynomial.coefficients {
		if math.Abs(polynomial.coefficients[index]) > 0.00000000000001 {
			stringPolynomial += stringForTerm(parameter, coefficient, index) + "+"
		}
	}

	if len(stringPolynomial) == 0 {
		return "0"
	} else {
		return string([]rune(stringPolynomial)[:len(stringPolynomial)-1])
	}
}

func stringForTerm(parameter string, coefficient float64, exponent int) string {
	coefficientString := fmt.Sprintf("%.4f", coefficient)

	switch exponent {
	case 0:
		return coefficientString
	case 1:
		return coefficientString + parameter
	default:
		return coefficientString + parameter + "^" + fmt.Sprintf("%d", exponent)
	}
}

func Differentiate(polynomial Polynomial) Polynomial {
	if len(polynomial.coefficients) == 0 {
		return Polynomial{}
	}

	derivativeCoefficients := make([]float64, len(polynomial.coefficients)-1)
	for i := 1; i < len(polynomial.coefficients); i++ {
		derivativeCoefficients[i-1] = polynomial.coefficients[i] * float64(i)
	}

	return Polynomial{derivativeCoefficients}
}

func Integrate(polynomial Polynomial, constant float64) Polynomial {
	integralCoefficients := make([]float64, len(polynomial.coefficients)+1)
	integralCoefficients[0] = constant
	for i := 0; i < len(polynomial.coefficients); i++ {
		integralCoefficients[i+1] = polynomial.coefficients[i] / float64(i+1)
	}

	return Polynomial{integralCoefficients}
}

func Sum(polynomials ...Polynomial) Polynomial {
	degree := 0
	for _, polynomial := range polynomials {
		if len(polynomial.coefficients) > degree {
			degree = len(polynomial.coefficients)
		}
	}

	summedCoefficients := make([]float64, degree)
	for _, polynomial := range polynomials {
		for index, coefficient := range polynomial.coefficients {
			summedCoefficients[index] += coefficient
		}
	}

	return Polynomial{summedCoefficients}
}

func Multiply(polynomial Polynomial, multiplier float64) Polynomial {
	multipliedCoefficients := make([]float64, len(polynomial.coefficients))
	for index, coefficient := range polynomial.coefficients {
		multipliedCoefficients[index] = coefficient * multiplier
	}

	return Polynomial{multipliedCoefficients}
}
