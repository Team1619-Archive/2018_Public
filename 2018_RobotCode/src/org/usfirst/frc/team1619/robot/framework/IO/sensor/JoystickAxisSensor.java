package org.usfirst.frc.team1619.robot.framework.IO.sensor;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickAxisSensor extends NumericSensor {

	public enum Axis {
		X, Y, Z
	}

	private Axis axis;

	private Joystick joystick;

	public JoystickAxisSensor(Joystick joystick, Axis axis, boolean isInverted) {
		super(isInverted);

		this.joystick = joystick;
		this.axis = axis;
	}

	@Override
	protected double getValue() {
		switch (this.axis) {
		case X:
			return joystick.getX();
		case Y:
			return joystick.getY();
		case Z:
			return joystick.getZ();
		default:
			return 0.0;
		}
	}

}
