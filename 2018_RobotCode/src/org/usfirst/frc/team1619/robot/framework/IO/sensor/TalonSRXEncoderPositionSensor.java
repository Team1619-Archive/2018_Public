package org.usfirst.frc.team1619.robot.framework.IO.sensor;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonSRXEncoderPositionSensor extends NumericSensor {

	private TalonSRX motor;
	private double revolutionsPerUnit;
	private double zeroEncPosition = 0.0;
	private String unit;
	private int cyclesPerRevolution;

	public TalonSRXEncoderPositionSensor(TalonSRX motor, boolean isInverted, FeedbackDevice feedbackDevice,
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
		return ((double) (this.motor.getSelectedSensorPosition(0)) * this.revolutionsPerUnit / this.cyclesPerRevolution)
				- this.zeroEncPosition;
	}

	public void zero() {
		this.zeroEncPosition += this.get();
	}

	public void zero(double offset) {
		this.zeroEncPosition = this.get() - offset;
	}

	public String getUnit() {
		return this.unit;
	}

}