package org.usfirst.frc.team1619.robot.framework.IO.sensor;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonSRXEncoderVelocitySensor extends NumericSensor {

	private TalonSRX motor;
	private double revolutionsPerUnit;
	private String unit;
	private int cyclesPerRevolution;

	public TalonSRXEncoderVelocitySensor(TalonSRX motor, boolean isInverted, FeedbackDevice feedbackDevice,
			int cyclesPerRevolution, double revolutionsPerUnit, String unit) {
		super(isInverted);

		this.cyclesPerRevolution = cyclesPerRevolution;
		this.motor = motor;
		this.motor.configSelectedFeedbackSensor(feedbackDevice, 0, 0);
		this.revolutionsPerUnit = revolutionsPerUnit;
		this.unit = unit;

	}

	@Override
	protected double getValue() {
		double encoder = this.motor.getSelectedSensorVelocity(0);
		return (double) encoder * this.revolutionsPerUnit * 10.0 / this.cyclesPerRevolution;
	}

	public String getUnit() {
		return this.unit;
	}
}
