package trajectory

import (
	"math"
	"math/rand"
)

type oneDimensionSplineSkeleton struct {
	Values, VValues, AValues []float64
}

type SplineSkeleton struct {
	X, Y *oneDimensionSplineSkeleton
}

func newOneDimensionSplineSkeleton(vi, vf float64, values ...float64) *oneDimensionSplineSkeleton {
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

	return &oneDimensionSplineSkeleton{values, vValues, aValues}
}

func NewSplineSkeleton(startHeadingDegrees, endHeadingDegrees float64, points ...Point) *SplineSkeleton {
	startHeading := startHeadingDegrees * math.Pi / 180
	endHeading := endHeadingDegrees * math.Pi / 180

	startDistance := math.Sqrt(math.Pow(points[1].X-points[0].X, 2) + math.Pow(points[1].Y-points[0].Y, 2))
	endDistance := math.Sqrt(math.Pow(points[len(points)-1].X-points[len(points)-2].X, 2) + math.Pow(points[len(points)-1].Y-points[len(points)-2].Y, 2))

	xValues := make([]float64, len(points))
	yValues := make([]float64, len(points))
	for index, point := range points {
		xValues[index] = point.X
		yValues[index] = point.Y
	}

	return &SplineSkeleton{
		newOneDimensionSplineSkeleton(0.5*math.Cos(startHeading)*startDistance, 0.5*math.Cos(endHeading)*endDistance, xValues...),
		newOneDimensionSplineSkeleton(0.5*math.Sin(startHeading)*startDistance, 0.5*math.Sin(endHeading)*endDistance, yValues...),
	}
}

func NewRandomSplineSkeleton(length int, minDistance, maxDistance float64) *SplineSkeleton {
	points := make([]Point, length)

	position := Point{0, 0}
	heading := 0.0

	points[1] = position

	distance := minDistance + maxDistance*rand.Float64()
	direction := heading + 91*rand.Float64() - 45

	position.X -= math.Cos(direction*math.Pi/180) * distance
	position.Y -= math.Sin(direction*math.Pi/180) * distance

	initialHeading := direction + 181*rand.Float64() - 90

	points[0] = position

	position.X = 0
	position.Y = 0

	for j := 2; j < length; j++ {
		distance := minDistance + maxDistance*rand.Float64()
		direction := heading + 91*rand.Float64() - 45

		position.X += math.Cos(direction*math.Pi/180) * distance
		position.Y += math.Sin(direction*math.Pi/180) * distance

		heading = direction + 181*rand.Float64() - 90

		points[j] = position
	}

	return NewSplineSkeleton(initialHeading, heading, points...)
}
