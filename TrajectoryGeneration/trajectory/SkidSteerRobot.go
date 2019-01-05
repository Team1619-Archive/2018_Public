package trajectory

type SkidSteerRobot struct {
	Acceleration        float64 `json: "acceleration"`
	Deceleration        float64 `json: "deceleration"`
	MaxVelocity         float64 `json: "maxVelocity"`
	AngularAcceleration float64 `json: "angularAcceleration"`
}
