package org.usfirst.frc.team1619.robot.framework.trajectory;

public abstract class TrajectoryData {

	public double frequency;
	public double[] velocityValues;
	public double[] headingValues;

	public TrajectoryData(double[] velocityValues, double[] headingValues, double frequency) {
		this.velocityValues = velocityValues;
		this.headingValues = headingValues;
		this.frequency = frequency;

	}
	
	

}
