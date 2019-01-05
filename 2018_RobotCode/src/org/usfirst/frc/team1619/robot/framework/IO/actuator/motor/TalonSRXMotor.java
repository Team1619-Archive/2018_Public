package org.usfirst.frc.team1619.robot.framework.IO.actuator.motor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * Wraps for CANTalons Motor value is not persisted past one frame
 */
public class TalonSRXMotor implements Motor {

	/**
	 * The CANTalon Object
	 */
	private TalonSRX motor;

	/**
	 * Local model of motor output
	 */
	private double output = 0;

	/**
	 * If the motor is inverted
	 */
	private boolean isInverted;

	/**
	 * Creates a motor
	 *
	 * @param motor
	 *            The CANTalon
	 * @param isBrakeMode
	 *            if motor is in brake mode
	 * @param isInverted
	 *            if the motor's output is inverted
	 */
	public TalonSRXMotor(TalonSRX motor, NeutralMode neutralMode, boolean isInverted) {
		this.motor = motor;
		this.motor.setNeutralMode(neutralMode);
		;
		this.isInverted = isInverted;

	}

	/**
	 * Updates the local model of the motor output
	 *
	 * @param output
	 *            the motor output between -1 and 1
	 */
	public void set(double output) {
		this.output = (this.isInverted ? -1 : 1) * output;
	}

	/**
	 * Flushes the local model of the motor output to the CANTalon
	 */
	public void flush() {
		this.motor.set(ControlMode.PercentOutput, this.output);
		this.output = 0;
	}
}
