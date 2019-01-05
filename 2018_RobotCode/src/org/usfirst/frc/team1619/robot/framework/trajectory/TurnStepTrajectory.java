package org.usfirst.frc.team1619.robot.framework.trajectory;

public class TurnStepTrajectory implements Trajectory {

	private double v1;

	public TurnStepTrajectory(double v1) {
		this.v1 = v1;
	}

	@Override
	public double distance(double time) {
		return 0.0;
	}

	@Override
	public double velocity(double time) {
		return v1;
	}

	@Override
	public double acceleration(double time) {
		return 0.0;
	}

	@Override
	public double heading(double time) {
		if (time > 2.0) {
			return 90.0;
		}

		else
			return 0.0;
	}

	@Override
	public boolean isDone(double time) {
		return false;
	}
}
