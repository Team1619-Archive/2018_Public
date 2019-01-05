package org.usfirst.frc.team1619.robot.framework.IO;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1619.robot.framework.IO.sensor.Sensor;

/**
 * Entry point to all robot inputs
 */
public class In {

	/**
	 * The singleton instance
	 */
	private static In instance;

	/**
	 * Gets the singleton instance Creates it if first time
	 *
	 * @return the singleton instance
	 */
	public static In getInstance() {
		if (instance == null) {
			instance = new In();
		}

		return instance;
	}

	/**
	 * Indexed list of sensors
	 */
	private List<Sensor> sensors = new ArrayList<>();

	/**
	 * Private constructor because singleton
	 */
	private In() {

	}

	/**
	 * Registers a new sensor object
	 *
	 * @param sensor
	 *            a sensor object
	 * @return the key
	 */
	public int register(Sensor sensor) {
		this.sensors.add(sensor);
		return this.sensors.size() - 1;
	}

	/**
	 * Gets a sensor
	 *
	 * @param key
	 *            the key
	 * @return the sensor associated with this key
	 */
	public <T extends Sensor> T get(int key) {
		assert key > -1 && key < this.sensors.size() : "Invalid Key";
		return (T) this.sensors.get(key);
	}

	/**
	 * Updates all the motors
	 */
	public void update() {
		for (Sensor sensor : this.sensors) {
			sensor.update();
		}
	}

}
