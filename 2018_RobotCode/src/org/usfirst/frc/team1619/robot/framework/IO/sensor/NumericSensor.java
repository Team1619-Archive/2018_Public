package org.usfirst.frc.team1619.robot.framework.IO.sensor;

public abstract class NumericSensor implements Sensor<Double, Double> {

	/**
	 * Our local model of the previous model
	 */
	private double previousValue;

	/**
	 * Our local model of the value
	 */
	private double value;

	private boolean isInverted;

	/**
	 * Gets the sensor value
	 *
	 * @return the sensor value
	 */
	protected abstract double getValue();

	public NumericSensor(boolean isInverted) {
		this.isInverted = isInverted;
	}

	@Override
	public void update() {
		this.previousValue = this.value;
		this.value = (this.isInverted ? -1 : 1) * this.getValue();
	}

	@Override
	public Double get() {
		return this.value;
	}

	@Override
	public Double getDelta() {
		return this.value - this.previousValue;
	}
}
