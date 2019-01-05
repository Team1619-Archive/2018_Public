package org.usfirst.frc.team1619.robot.framework.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.usfirst.frc.team1619.robot.framework.IO.actuator.motor.Motor;
import org.usfirst.frc.team1619.robot.framework.IO.sensor.NavXHeadingSensor;
import org.usfirst.frc.team1619.robot.framework.IO.sensor.NumericSensor;
import org.usfirst.frc.team1619.robot.framework.trajectory.Trajectory;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HeadingController {

	private Trajectory trajectory;

	private PID rightPID;
	private PID leftPID;
	private PID headingPID;

	private Motor left;
	private Motor right;

	private NumericSensor velocityLeft;
	private NumericSensor velocityRight;

	public NavXHeadingSensor headingSensor;

	private double previousTime;
	private double heading;
	private double distance;
	private double previousVelocity = 0;
	private boolean isInverted;

	public HeadingController(PID leftPID, PID rightPID, PID headingPID, boolean isInverted) {
		this.leftPID = leftPID;
		this.rightPID = rightPID;
		this.headingPID = headingPID;
		this.isInverted = isInverted;
	}

	// These two methods must be called before controller is used
	public void setMotors(Motor left, Motor right) {
		this.left = left;
		this.right = right;
	}

	public void setSenors(NumericSensor velocityLeft, NumericSensor velocityRight, NavXHeadingSensor headingSensor) {
		this.velocityLeft = velocityLeft;
		this.velocityRight = velocityRight;
		this.headingSensor = headingSensor;
	}

	public void setInverted(boolean isInverted) {
		this.isInverted = isInverted;
	}

	public void prepareTrajectory(Trajectory trajectory) {
		this.trajectory = trajectory;
		this.headingSensor.zero();
		this.previousTime = 0.0;
	}

	public void set(double time) {
		// this.previousTime = time - 0.005;

		double velocity = this.trajectory.velocity(time);
		SmartDashboard.putNumber("velocity", velocity);
		this.headingPID.set(0);
		this.heading = this.trajectory.heading(time);

		double measuredHeading = this.headingSensor.get();

		double measuredLeftVelocity = this.velocityLeft.get();
		double measuredRightVelocity = this.velocityRight.get();

		double headingError = Math.abs(heading - measuredHeading);

		SmartDashboard.putNumber("measuredHeading", measuredHeading);

		if (headingError > 180) {
			headingError = 360.0 - headingError;
		}

		double leftAdjustment = 0.0;
		double rightAdjustment = 0.0;

		double headingPIDValue = 0.0;

		if (this.isInverted) {
			velocity = -velocity;
		}

		SmartDashboard.putNumber("Desired Heading", this.heading);

		if (((measuredHeading < heading) && ((heading - measuredHeading) < 180))
				|| ((measuredHeading > heading) && ((measuredHeading - heading) > 180))) {
			headingPIDValue = this.headingPID.get(headingError);
		} else {
			headingPIDValue = this.headingPID.get(-headingError);
			headingError = -headingError;
		}

		rightAdjustment += -(velocity + 0.01) * headingPIDValue;
		leftAdjustment += (velocity + 0.01) * headingPIDValue;

		SmartDashboard.putNumber("headingError", headingError);
		SmartDashboard.putNumber("VelocityChange", rightAdjustment);
		SmartDashboard.putNumber("Unscaled Heading Correction", headingPIDValue);

		if (!this.isInverted) {
			this.leftPID.set(velocity + leftAdjustment);
			this.rightPID.set(velocity + rightAdjustment);
		} else {
			this.leftPID.set(velocity - leftAdjustment);
			this.rightPID.set(velocity - rightAdjustment);
		}

		double leftOutput = this.leftPID.get(measuredLeftVelocity);
		double rightOutput = this.rightPID.get(measuredRightVelocity);

		SmartDashboard.putNumber("Left Output", leftOutput);

		SmartDashboard.putNumber("Error", this.leftPID.getError(measuredLeftVelocity));

		SmartDashboard.putNumber("P Contribution", this.leftPID.p);
		SmartDashboard.putNumber("I Contribution", this.leftPID.i);
		SmartDashboard.putNumber("D Contribution", this.leftPID.d);

		this.left.set(leftOutput);
		this.right.set(rightOutput);
	}

	public NavXHeadingSensor getHeadingSensor() {
		return headingSensor;
	}

	public PID getHeadingPID() {
		return this.headingPID;
	}

	public PID getLeftPID() {
		return leftPID;
	}

	public PID getRightPID() {
		return rightPID;
	}

}
