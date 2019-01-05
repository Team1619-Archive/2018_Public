package trajectory

var h50 = Polynomial{[]float64{1.0, 0.0, 0.0, -10.0, 15.0, -6.0}}
var h51 = Polynomial{[]float64{0.0, 1.0, 0.0, -6.0, 8.0, -3.0}}
var h52 = Polynomial{[]float64{0.0, 0.0, 0.5, -1.5, 1.5, -0.5}}
var h53 = Polynomial{[]float64{0.0, 0.0, 0.0, 0.5, -1.0, 0.5}}
var h54 = Polynomial{[]float64{0.0, 0.0, 0.0, -4.0, 7.0, -3.0}}
var h55 = Polynomial{[]float64{0.0, 0.0, 0.0, 10.0, -15.0, 6.0}}

func NewQuinticHermite(p0, p1, v0, v1, a0, a1 float64) Polynomial {
	return Sum(Multiply(h50, p0),
		Multiply(h51, v0),
		Multiply(h52, a0),
		Multiply(h53, a1),
		Multiply(h54, v1),
		Multiply(h55, p1))
}

func QuinticHermiteFor(vi, vf float64, values ...float64) []Polynomial {
	vValues := make([]float64, len(values))
	vValues[0] = vi
	vValues[len(values)-1] = vf
	for i := 1; i < len(values)-1; i++ {
		vValues[i] = 0.5 * (values[i+1] - values[i-1])
	}

	aValues := make([]float64, len(values))
	aValues[0] = 0
	aValues[len(values)-1] = 0
	for i := 1; i < len(values)-1; i++ {
		aValues[i] = 0.5 * (vValues[i+1] - vValues[i-1])
	}

	spline := make([]Polynomial, len(values)-1)
	for i := 0; i < len(spline); i++ {
		spline[i] = NewQuinticHermite(values[i], values[i+1], vValues[i], vValues[i+1], aValues[i], aValues[i+1])
	}

	return spline
}

func QuinticHermiteFor2(values []float64, vValues []float64) []Polynomial {
	aValues := make([]float64, len(values))
	aValues[0] = 0
	aValues[len(values)-1] = 0
	for i := 1; i < len(values)-1; i++ {
		aValues[i] = 0.5 * (vValues[i+1] - vValues[i-1])
	}

	spline := make([]Polynomial, len(values)-1)
	for i := 0; i < len(spline); i++ {
		spline[i] = NewQuinticHermite(values[i], values[i+1], vValues[i], vValues[i+1], aValues[i], aValues[i+1])
	}

	return spline
}
