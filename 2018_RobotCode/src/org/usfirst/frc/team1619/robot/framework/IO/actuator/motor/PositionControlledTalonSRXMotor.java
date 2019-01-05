package org.usfirst.frc.team1619.robot.framework.IO.actuator.motor;

import org.usfirst.frc.team1619.robot.framework.IO.sensor.NumericSensor;
import org.usfirst.frc.team1619.robot.framework.controller.PID;

public class PositionControlledTalonSRXMotor implements Motor {

	private Motor motor;
	private double output;
	// private boolean isInverted;

	public PID pid;
	private NumericSensor feedbackSensor;

	public PositionControlledTalonSRXMotor(Motor motor, PID pid, NumericSensor feedbackSensor) {
		this.motor = motor;
		this.pid = pid;
		this.feedbackSensor = feedbackSensor;

	}

	@Override
	public void set(double setpoint) {
		this.pid.set(setpoint);
	}

	@Override
	public void flush() {
		this.output = this.pid.get(feedbackSensor.get());
		this.motor.set(output);
		this.motor.flush();
	}

}