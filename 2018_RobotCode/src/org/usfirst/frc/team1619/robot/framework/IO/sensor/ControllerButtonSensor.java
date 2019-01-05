package org.usfirst.frc.team1619.robot.framework.IO.sensor;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

public class ControllerButtonSensor extends BooleanSensor {
	
	public enum ControllerButton {
		A, X, Y, B, START, BACK, D_PAD_UP, D_PAD_DOWN, D_PAD_LEFT, D_PAD_RIGHT, LEFT_BUMPER, RIGHT_BUMPER, LEFT_TRIGGER, RIGHT_TRIGGER

	}

	private XboxController controller;

	private ControllerButton id;

	public ControllerButtonSensor(int controller, ControllerButton id) {
		this.controller = new XboxController(controller);
		this.id = id;
	}

	@Override
	protected boolean getValue() {
		switch (this.id) {
		case A:
			return this.controller.getAButton();
		case X:
			return this.controller.getXButton();
		case Y:
			return this.controller.getYButton();
		case B:
			return this.controller.getBButton();
		case START:
			return this.controller.getStartButton();
		case BACK:
			return this.controller.getBackButton();
		case LEFT_BUMPER:
			return this.controller.getBumper(GenericHID.Hand.kLeft);
		case RIGHT_BUMPER:
			return this.controller.getBumper(GenericHID.Hand.kRight);
		case LEFT_TRIGGER:
			return this.controller.getTriggerAxis(GenericHID.Hand.kLeft) > 0.25;
		case RIGHT_TRIGGER:
			return this.controller.getTriggerAxis(GenericHID.Hand.kRight) > 0.25;
		case D_PAD_UP:
			if (this.controller.getPOV(0) == 0) {
				return true;
			}
			return false;

		case D_PAD_DOWN:
			if (this.controller.getPOV(0) == 180) {
				return true;
			}
			return false;

		case D_PAD_LEFT:
			if (this.controller.getPOV(0) == 270) {
				return true;
			}
			return false;

		case D_PAD_RIGHT:
			if (this.controller.getPOV(0) == 90) {
				return true;
			}
			return false;

		default:
			return false;
		}
	}
}
