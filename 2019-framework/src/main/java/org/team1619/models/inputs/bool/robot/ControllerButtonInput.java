package org.team1619.models.inputs.bool.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.inputs.bool.ButtonInput;
import org.team1619.utilities.Config;

public class ControllerButtonInput extends ButtonInput {
	private static final Logger sLogger = LoggerFactory.getLogger(RobotJoystickButtonInput.class);

	private final XboxController fController;

	public ControllerButtonInput(Object name, Config config) {
		super(name, config);
		fController = new XboxController(fPort);
	}

	@Override
	public boolean isPressed() {
		switch (fButton) {
			case "a":
				return this.fController.getAButton();
			case "x":
				return this.fController.getXButton();
			case "y":
				return this.fController.getYButton();
			case "b":
				return this.fController.getBButton();
			case "start":
				return this.fController.getStartButton();
			case "back":
				return this.fController.getBackButton();
			case "left_bumper":
				return this.fController.getBumper(GenericHID.Hand.kLeft);
			case "right_bumper":
				return this.fController.getBumper(GenericHID.Hand.kRight);
			case "d_pad_up":
				if (this.fController.getPOV(0) == 0) {
					return true;
				}
				return false;
			case "d_pad_down":
				if (this.fController.getPOV(0) == 180) {
					return true;
				}
				return false;

			case "d_pad_left":
				if (this.fController.getPOV(0) == 270) {
					return true;
				}
				return false;

			case "d_pad_right":
				if (this.fController.getPOV(0) == 90) {
					return true;
				}
				return false;

			default:
				return false;
		}
	}
}
