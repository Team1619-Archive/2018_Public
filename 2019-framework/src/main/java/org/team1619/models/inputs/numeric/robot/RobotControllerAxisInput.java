package org.team1619.models.inputs.numeric.robot;

import edu.wpi.first.wpilibj.XboxController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.inputs.numeric.AxisInput;
import org.team1619.utilities.Config;

public class RobotControllerAxisInput extends AxisInput {

	private static final Logger sLogger = LoggerFactory.getLogger(RobotJoystickAxisInput.class);

	private XboxController fController;

	public RobotControllerAxisInput(Object name, Config config) {
		super(name, config);
		fController = new XboxController(fPort);
	}

	@Override
	public double getAxis() {
		return fController.getRawAxis(fAxis);
	}
}
