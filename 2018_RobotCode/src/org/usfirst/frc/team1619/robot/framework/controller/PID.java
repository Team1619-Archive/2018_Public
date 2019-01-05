package org.usfirst.frc.team1619.robot.framework.controller;

import java.util.ArrayList;
import java.util.List;

public class PID {

	public double p, i, d;

	public class Profile {

		public double f, p, i, d;
		public double maxIntegral, integralRange;
		public double maxOutput;
		public double idleOutput;

		public Profile(double idleOutput, double f, double p, double i, double d, double maxIntegral,
				double integralRange, double maxOutput) {
			this.f = f;
			this.p = p;
			this.i = i;
			this.d = d;

			this.maxIntegral = maxIntegral;
			this.integralRange = integralRange;

			this.maxOutput = maxOutput;

			this.idleOutput = idleOutput;
		}

		public void setPID(double p, double i, double d, double intRange) {
			this.p = p;
			this.i = i;
			this.d = d;
			this.integralRange = intRange;

			this.maxIntegral = this.maxOutput / this.i;
		}

	}

	private List<Profile> profiles = new ArrayList<>();

	private Profile profile = null;

	private double setpoint = 0.0;

	public double integral = 0.0;
	private double previousError = 0.0;
	private long previousTime = -1;

	private boolean isHeading;

	private double previousDerivative = 0.0;

	public PID(boolean isHeading) {
		this.isHeading = isHeading;
	}

	public PID() {
		this.isHeading = false;
	}

	public int addProfile(double idleOutput, double f, double p, double i, double d, double maxOutput) {
		return this.addProfile(idleOutput, f, p, i, d, maxOutput / i, -1, maxOutput);
	}

	public int addProfile(double idleOutput, double f, double p, double i, double d, double maxIntegral,
			double maxOutput) {
		return this.addProfile(idleOutput, f, p, i, d, maxIntegral, -1, maxOutput);
	}

	public int addProfile(double idleOutput, double f, double p, double i, double d, double maxIntegral,
			double integralRange, double maxOutput) {
		this.profiles.add(new Profile(idleOutput, f, p, i, d, maxIntegral, integralRange, maxOutput));
		return this.profiles.size() - 1;
	}

	public void setProfile(int key) {
		this.reset();
		this.profile = this.profiles.get(key);
	}

	public void set(double setpoint) {
		this.setpoint = setpoint;
	}

	public void reset() {
		this.integral = 0.0;
		this.previousError = 0.0;
		this.previousTime = -1;
	}

	public double get(double measuredValue) {
		long time = System.currentTimeMillis();

		if (this.previousTime == -1) {
			this.previousTime = time;
		}

		double deltaTime = (time - this.previousTime) / 1000.0;

		double error = this.setpoint - measuredValue;

		boolean insideIntegralRange = (this.profile.integralRange == -1
				|| Math.abs(error) <= this.profile.integralRange);
		if (insideIntegralRange) {
			this.integral += deltaTime * error;

			if (this.profile.maxIntegral != -1) {
				if (this.integral < 0.0) {
					this.integral = Math.max(this.integral, -this.profile.maxIntegral);
				} else {
					this.integral = Math.min(this.integral, this.profile.maxIntegral);
				}
			}
		}

		double deltaError = error - this.previousError;
		double derivative = 0.0;

		if (deltaError == 0 && deltaTime < 0.005 && this.isHeading) {
			derivative = this.previousDerivative;
		} else {
			derivative = deltaTime > 0.0 ? (deltaError) : 0.0;
		}

		this.p = this.profile.p * error;
		this.i = this.profile.i * this.integral;
		this.d = this.profile.d * derivative;

		double output = this.profile.f * this.setpoint + this.p + this.i + this.d + this.profile.idleOutput;

		this.previousTime = time;
		this.previousError = error;
		this.previousDerivative = derivative;
		return (output < 0 ? -1 : 1) * Math.min(Math.abs(output), this.profile.maxOutput);

	}

	public Profile getProfile(int key) {
		return this.profiles.get(key);

	}

	public double getSetpoint() {
		return this.setpoint;
	}

	public double getError(double measuredValue) {
		return this.setpoint - measuredValue;
	}

}
