package org.usfirst.frc.team1619.robot.framework.IO;

import org.usfirst.frc.team1619.robot.framework.IO.actuator.motor.Motors;

/**
 * Entry point to all robot outputs
 */
public class Out {

	/**
	 * The singleton instance
	 */
	private static Out instance;

	/**
	 * Gets the singleton instance Creates it if first time
	 *
	 * @return the singleton instance
	 */
	public static Out getInstance() {
		if (instance == null) {
			instance = new Out();
		}

		return instance;
	}

	/**
	 * Private constructor because singleton
	 */
	private Out() {
	}

	/**
	 * The Motors instance
	 */
	public Motors motors = new Motors();
	
	/**
	 * The Solenoids Instance
	 */
//	public Solenoids solenoids = new Solenoids();
	
	/**
	 * Updates all outputs
	 */
	public void update() {
		this.motors.update();
//		this.solenoids.update();
	}

	/**
	 * Disables all outputs
	 */
	public void disable() {
		this.motors.disable();
//		this.solenoids.disable();
	}

}
