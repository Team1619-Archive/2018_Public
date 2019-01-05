package org.usfirst.frc.team1619.robot.framework.IO.sensor;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Implements a joystick button sensor for a specific joystick and button id
 */
public class JoystickButtonSensor extends BooleanSensor {

	/**
	 * The joystick
	 */
	private Joystick joystick;

	/**
	 * The button id
	 */
	private int buttonId;

	/**
	 * Creates a joystick button sensor for a specific joystick and button id
	 *
	 * @param joystick
	 *            the joystick
	 * @param buttonId
	 *            the buttonId
	 */
	public JoystickButtonSensor(Joystick joystick, int buttonId) {
		this.joystick = joystick;
		this.buttonId = buttonId;
	}

	/**
	 * Gets if the joystick button is pressed
	 *
	 * @return if the joystick button is pressed
	 */
	@Override
	protected boolean getValue() {
		return this.joystick.getRawButton(this.buttonId);
	}
}
