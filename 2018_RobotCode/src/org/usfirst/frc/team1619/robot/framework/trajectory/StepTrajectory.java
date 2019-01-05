package org.usfirst.frc.team1619.robot.framework.trajectory;

public class StepTrajectory implements Trajectory {

	private double v1;
	private double v2;

	private double v1Time;

	public StepTrajectory(double v1, double v2, double v1Time) {
		this.v1 = v1;
		this.v2 = v2;
		this.v1Time = v1Time;
	}

	@Override
	public double distance(double time) {
		return 0.0;
	}

	@Override
	public double velocity(double time) {
		if (time <= this.v1Time) {
			return this.v1;
		}

		return this.v2;
	}

	@Override
	public double acceleration(double time) {
		return 0.0;
	}

	@Override
	public double heading(double time) {
		return 0.0;
	}

	@Override
	public boolean isDone(double time) {
		return false;
	}
}
