package org.usfirst.frc.team1619.robot.framework.IO.sensor;

import edu.wpi.first.wpilibj.XboxController;

public class ControllerAxisSensor extends NumericSensor {

	private int axis;
	private double deadband;
	private XboxController controller;

	public ControllerAxisSensor(int controllerID, int axis, boolean isInverted, double deadband) {
		super(isInverted);

		this.controller = new XboxController(controllerID);
		this.axis = axis;
		this.deadband = deadband;
	}

	@Override
	protected double getValue() {

		double value = this.controller.getRawAxis(this.axis);
		boolean isPositive = value > 0;
		value = Math.abs(value);
		
		double correctedValue = this.deadband;
		if (value > this.deadband) {
			correctedValue = value;
		}

		double result = ((correctedValue - this.deadband) / (1.0 - this.deadband));

		if (isPositive) {
			return result;
		} else {
			return -1.0 * result;
		}
	}

}
