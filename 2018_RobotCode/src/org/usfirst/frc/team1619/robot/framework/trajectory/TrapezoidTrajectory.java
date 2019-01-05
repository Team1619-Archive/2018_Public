package org.usfirst.frc.team1619.robot.framework.trajectory;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TrapezoidTrajectory implements Trajectory {

	private double distance;
	private double acceleration;
	private double deceleration;
	private double coastingVelocity;
	private boolean isBackwards;

	private Trajectory accelerationTrajectory;
	private Trajectory coastingTrajectory;
	private Trajectory decelerationTrajectory;

	private double accelerationEndTime;
	private double coastingEndTime;
	private double decelerationEndTime;

	public TrapezoidTrajectory(double distance, double acceleration, double deceleration, double coastingVelocity) {
		if (this.distance < 0) {
			this.isBackwards = true;
		}
		this.distance = Math.abs(distance);
		this.acceleration = acceleration;
		this.deceleration = deceleration;
		this.coastingVelocity = coastingVelocity;

		double accelerationTime = this.coastingVelocity / this.acceleration;
		double decelerationTime = this.coastingVelocity / this.deceleration;

		double accelerationDistance = 0.5 * this.acceleration * Math.pow(accelerationTime, 2);
		double decelerationDistance = 0.5 * this.deceleration * Math.pow(decelerationTime, 2);
		double coastingDistance = this.distance - accelerationDistance - accelerationDistance;

		this.accelerationTrajectory = new Trajectory(new Polynomial(0.0, this.acceleration), 0.0); // v = at
		this.coastingTrajectory = new Trajectory(new Polynomial(this.coastingVelocity), accelerationDistance);
		this.decelerationTrajectory = new Trajectory(new Polynomial(this.coastingVelocity, -1 * this.deceleration),
				accelerationDistance + coastingDistance); // v = -at

		if (accelerationDistance + decelerationDistance <= this.distance) {
			double coastingTime = coastingDistance / this.coastingVelocity;
			this.accelerationEndTime = accelerationTime;
			this.coastingEndTime = accelerationTime + coastingTime;
			this.decelerationEndTime = this.coastingEndTime + decelerationTime;

		} else {
			this.coastingEndTime = 0;
			double accelerationRatio = this.deceleration / (this.acceleration + this.deceleration);
			double decelerationRatio = this.acceleration / (this.acceleration + this.deceleration);
			
			this.accelerationEndTime = Math.sqrt(2.0 * (distance * accelerationRatio) / this.acceleration);
			this.decelerationEndTime = Math.sqrt(2.0 * (distance * decelerationRatio) / this.deceleration);

		}
	}

	@Override
	public double distance(double time) {

		if (time < 0.0) {
			return 0.0;
		} else if (time <= this.accelerationEndTime) {
			return (this.isBackwards ? -1.0 : 1.0) * this.accelerationTrajectory.distance(time);
		} else if (time <= this.coastingEndTime) {
			return (this.isBackwards ? -1.0 : 1.0) * this.coastingTrajectory.distance(time - this.accelerationEndTime);
		} else if (time <= this.decelerationEndTime) {
			return (this.isBackwards ? -1.0 : 1.0) * this.decelerationTrajectory.distance(time - this.coastingEndTime);
		} else {
			return (this.isBackwards ? -1.0 : 1.0) * this.distance;
		}

	}

	@Override
	public double velocity(double time) {
		if (time < 0.0) {
			return 0.0;
		} else if (time <= this.accelerationEndTime) {
			return (this.isBackwards ? -1.0 : 1.0) * this.accelerationTrajectory.velocity(time);
		} else if (time <= this.coastingEndTime) {
			return (this.isBackwards ? -1.0 : 1.0) * this.coastingTrajectory.velocity(time - this.accelerationEndTime);
		} else if (time <= this.decelerationEndTime) {
			return (this.isBackwards ? -1.0 : 1.0) * this.decelerationTrajectory.velocity(time - this.coastingEndTime);
		} else {
			return 0.0;
		}

	}

	@Override
	public double acceleration(double time) {
		if (time < 0.0) {
			return 0.0;
		} else if (time <= this.accelerationEndTime) {
			return (this.isBackwards ? -1.0 : 1.0) * this.accelerationTrajectory.acceleration(time);
		} else if (time <= this.coastingEndTime) {
			return (this.isBackwards ? -1.0 : 1.0)
					* this.coastingTrajectory.acceleration(time - this.accelerationEndTime);
		} else if (time <= this.decelerationEndTime) {
			return (this.isBackwards ? -1.0 : 1.0)
					* this.decelerationTrajectory.acceleration(time - this.coastingEndTime);
		} else {
			return 0.0;
		}

	}

	public double getDistance() {
		return this.distance;
	}

	public double getTotalTime() {
		return this.decelerationEndTime;
	}

	private class Trajectory {
		private Polynomial velocityPolynomial;
		private Polynomial accelerationPolynomial;
		private Polynomial distancePolynomial;

		private Trajectory(Polynomial velocityPolynomial, double startDistance) {
			this.velocityPolynomial = velocityPolynomial;
			this.accelerationPolynomial = Polynomial.differentiate(velocityPolynomial);
			this.distancePolynomial = Polynomial.integrate(velocityPolynomial, startDistance);
		}

		public double velocity(double time) {
			return this.velocityPolynomial.evaluate(time);

		}

		public double distance(double time) {
			return this.distancePolynomial.evaluate(time);

		}

		public double acceleration(double time) {
			return this.accelerationPolynomial.evaluate(time);

		}

	}

	@Override
	public double heading(double time) {
		return 0.0;
	}

	public boolean isDone(double time) {
		SmartDashboard.putNumber("Current Distance", this.distance(time));
		SmartDashboard.putNumber("Total Distance", this.distance);
		return (this.distance(time) >= this.distance);

	}

}
