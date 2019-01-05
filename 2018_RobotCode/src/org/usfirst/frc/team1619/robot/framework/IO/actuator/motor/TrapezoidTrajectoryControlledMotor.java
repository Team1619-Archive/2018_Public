package org.usfirst.frc.team1619.robot.framework.IO.actuator.motor;

import org.usfirst.frc.team1619.robot.framework.IO.sensor.NumericSensor;
import org.usfirst.frc.team1619.robot.framework.controller.Feedforward;
import org.usfirst.frc.team1619.robot.framework.trajectory.TrapezoidTrajectory;

public class TrapezoidTrajectoryControlledMotor implements Motor {

	private Motor motor;
	private double time;
	private TrapezoidTrajectory trajectory;
	private Feedforward feedforward;
	private NumericSensor velocitySensor;

	public TrapezoidTrajectoryControlledMotor(Motor motor, Feedforward feedforward, NumericSensor velocitySensor) {
		this.motor = motor;
		this.feedforward = feedforward;
		this.velocitySensor = velocitySensor;

	}

	public void prepareTrajectory(TrapezoidTrajectory trajectory) {
		this.trajectory = trajectory;
		this.time = 0.0;
	}

	@Override
	public void set(double time) {
		this.time = time;

	}

	@Override
	public void flush() {
		// double distance = this.trajectory.distance(this.time);
		double velocity = this.trajectory.velocity(this.time);
		double acceleration = this.trajectory.acceleration(this.time);

		this.motor.set(this.feedforward.get(velocity, acceleration, this.velocitySensor.get()));
		;
		this.motor.flush();

	}

}
