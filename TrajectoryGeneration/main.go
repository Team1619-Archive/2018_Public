package main

import (
	"./trajectory"
	"net/http"
	"github.com/gin-gonic/gin"
	"github.com/gin-contrib/cors"
	"fmt"
)

func main() {
	router := gin.Default()

	router.Use(cors.Default())

	router.POST("/velocity-profile", getVelocityProfile)

	router.Run(":7777")
}

type GetVelocityProfile struct {
	Robot               trajectory.SkidSteerRobot `json: "robot"`
	StartHeadingDegrees float64                   `json: "startHeadingDegrees"`
	EndHeadingDegrees   float64                   `json: "endHeadingDegrees"`
	Points              []trajectory.Point        `json: "points"`
	Resolution          int                       `json: "resolution"`
}

func getVelocityProfile(c *gin.Context) {
	var data GetVelocityProfile
	if err := c.BindJSON(&data); err == nil {
		spline := trajectory.SplineFor(data.StartHeadingDegrees, data.EndHeadingDegrees, data.Points...)
		profile := trajectory.NewVelocityProfile(&data.Robot, spline)

		profile.Calculate(data.Resolution)

		fmt.Println(data.Robot)
		fmt.Println(spline)
		fmt.Println(profile)

		c.JSON(http.StatusAccepted, profile)
	}
}
