package org.usfirst.frc.team1619.robot.framework.IO.sensor;

/**
 * Describes a sensor
 *
 * @param <T> type of sensor value
 * @param <U> type of sensor delta
 */
public interface Sensor<T, U> {

	/**
	 * Updates the sensor value
	 */
	void update();

	/**
	 * Gets the sensor value
	 *
	 * @return the sensor value
	 */
	T get();

	/**
	 * Gets the sensor delta
	 *
	 * @return the sensor delta
	 */
	U getDelta();

}
