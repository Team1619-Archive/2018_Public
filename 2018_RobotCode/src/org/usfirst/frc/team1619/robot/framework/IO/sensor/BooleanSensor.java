package org.usfirst.frc.team1619.robot.framework.IO.sensor;

/**
 * Describes a Sensor whose value is a boolean Delta is an Integer where 1 is
 * rising edge, -1 means falling edge, and 0 means no delta
 */
public abstract class BooleanSensor implements Sensor<Boolean, Integer> {

	/**
	 * Our local model of the previous model
	 */
	private boolean previousValue;

	/**
	 * Our local model of the value
	 */
	private boolean value;

	/**
	 * Gets the sensor value
	 *
	 * @return the sensor value
	 */
	protected abstract boolean getValue();

	@Override
	public void update() {
		this.previousValue = this.value;
		this.value = this.getValue();
	}

	@Override
	public Boolean get() {
		return this.value;
	}

	/**
	 * Gets the delta where 1 is rising edge, -1 means falling edge, and 0 means no
	 * delta
	 *
	 * @return the delta
	 */
	@Override
	public Integer getDelta() {
		if (this.value && !this.previousValue) {
			return 1;
		} else if (!this.value && this.previousValue) {
			return -1;
		} else {
			return 0;
		}
	}
}
