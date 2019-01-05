package org.team1619.models.inputs.numeric.robot;

import edu.wpi.first.wpilibj.Joystick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.inputs.numeric.AxisInput;
import org.team1619.utilities.Config;

public class RobotJoystickAxisInput extends AxisInput {

	private static final Logger sLogger = LoggerFactory.getLogger(RobotJoystickAxisInput.class);

	private Joystick fJoystick;

	public RobotJoystickAxisInput(Object name, Config config) {
		super(name, config);
		fJoystick = new Joystick(fPort);
	}

	@Override
	public double getAxis() {
		return fJoystick.getRawAxis(fAxis);
	}
}
