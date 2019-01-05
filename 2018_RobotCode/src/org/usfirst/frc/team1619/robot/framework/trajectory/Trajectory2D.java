package org.usfirst.frc.team1619.robot.framework.trajectory;

public class Trajectory2D implements Trajectory {

	private double[] velocityValues;
	private double[] headingValues;
	private double frequency;

	public Trajectory2D(TrajectoryData data) {
		this.velocityValues = data.velocityValues;
		this.headingValues = data.headingValues;
		this.frequency = data.frequency;
	}

	@Override
	public double distance(double time) {
		return 0;
	}

	@Override
	public double velocity(double time) {
		int index = (int) (time * this.frequency);
		if (index < velocityValues.length - 1) {

			double offset = time * this.frequency % (1 / this.frequency);
			return this.velocityValues[index]
					+ (this.velocityValues[index + 1] - this.velocityValues[index]) * offset;
		}

		return 0.0;

	}

	@Override
	public double acceleration(double time) {
		return 0;
	}

	@Override
	public double heading(double time) {
		int index = (int) (time * this.frequency);
		
		if (index < headingValues.length - 1) {
			
			double distance = time * this.frequency % (1 / this.frequency);
			double heading = this.headingValues[index]
					+ (this.headingValues[index + 1] + this.headingValues[index]) * distance;
			
			return heading >= 90 ? heading - 90 : heading + 270;
		}
		
		return headingValues[headingValues.length - 1] >= 90 ? headingValues[headingValues.length - 1] - 90 : headingValues[headingValues.length - 1] + 270;
	}
	
	public double getFrequency() {
		return this.frequency;
	}
	
	public double getIndexedLength() {
		return this.velocityValues.length - 1;
	}
	
	public boolean isDone(double time) {
		return ((int) (time * this.getFrequency()) > this.getIndexedLength());
	}

}
