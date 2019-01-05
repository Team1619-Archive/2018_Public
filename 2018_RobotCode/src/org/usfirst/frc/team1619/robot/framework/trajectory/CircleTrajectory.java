package org.usfirst.frc.team1619.robot.framework.trajectory;

public class CircleTrajectory implements Trajectory {

	private TrapezoidTrajectory trapezoid;
	private double radius;
	private boolean right = false;

	public CircleTrajectory(double radius, double degrees, double acceleration, double deceleration,
			double coastingVelocity) {
		if (degrees < 0) {
			this.right = true;
		}
		this.radius = radius;
		double radians = Math.toRadians(degrees);
		double distance = radius * Math.abs(radians);
		this.trapezoid = new TrapezoidTrajectory(distance, acceleration, deceleration, coastingVelocity);
	}

	@Override
	public double distance(double time) {
		return this.trapezoid.distance(time);
	}

	@Override
	public double velocity(double time) {
		return this.trapezoid.velocity(time);
	}

	@Override
	public double acceleration(double time) {
		return this.trapezoid.velocity(time);
	}

	@Override
	public double heading(double time) {
		double distance = this.trapezoid.distance(time);
		double heading = 0.0;
		
		if (this.right) {
			heading = 360.0 - (distance / (2 * Math.PI * this.radius) * 360.0) % 360;
		} else {
			heading = distance / (2 * Math.PI * this.radius) * 360.0;
		}

		return heading;

	}

	@Override
	public boolean isDone(double time) {
		return this.trapezoid.isDone(time);
	}

}
