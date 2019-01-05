package org.usfirst.frc.team1619.robot.framework.IO.actuator.motor;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all motors
 */
public class Motors {

	/**
	 * Indexed list of motors
	 */
	private List<Motor> motors = new ArrayList<>();

	/**
	 * Registers a new motor object
	 *
	 * @param motor
	 *            a motor object
	 * @return the key
	 */
	public int register(Motor motor) {
		this.motors.add(motor);
		return this.motors.size() - 1;
	}

	/**
	 * Gets a motor
	 *
	 * @param key
	 *            the key
	 * @return the motor associated with this key
	 */
	public Motor get(int key) {
		assert key > -1 && key < this.motors.size() : "Invalid key";
		return this.motors.get(key);
	}

	/**
	 * Flushes all motors
	 */
	public void update() {
		for (Motor motor : this.motors) {
			motor.flush();
		}
	}

	/**
	 * Sets all the motor outputs to 0
	 */
	public void disable() {
		for (Motor motor : this.motors) {
			motor.set(0);
		}
	}

}
