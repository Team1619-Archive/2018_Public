package org.usfirst.frc.team1619.robot.framework.IO.actuator.motor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonSRXMotorGroup extends TalonSRXMotor {

	private TalonSRX master;
	private Set<TalonSRX> slaveMotors = new HashSet<>();

	/**
	 * Creates a motor group
	 *
	 * @param motor
	 *            The master CANTalon
	 * @param isBrakeMode
	 *            if motors are in brake mode
	 * @param isInverted
	 *            if the motors outputs are inverted
	 * @param slave
	 *            the slave CANTalon
	 */
	public TalonSRXMotorGroup(TalonSRX motor, NeutralMode neutralMode, boolean isInverted, TalonSRX slave) {
		this(motor, neutralMode, isInverted, new HashSet<>(Arrays.asList(slave)));
	}

	/**
	 * Creates a motor group
	 *
	 * @param motor
	 *            The master CANTalon
	 * @param isBrakeMode
	 *            if motors are in brake mode
	 * @param isInverted
	 *            if the motors outputs are inverted
	 * @param slaves
	 *            the slave CANTalons
	 */
	public TalonSRXMotorGroup(TalonSRX motor, NeutralMode neutralMode, boolean isInverted, Set<TalonSRX> slaves) {
		super(motor, neutralMode, isInverted);

		this.master = motor;

		for (TalonSRX slave : slaves) {
			slave.set(ControlMode.Follower, this.master.getDeviceID());
			;

			this.slaveMotors.add(slave);
		}
	}

	@Override
	public void set(double output) {
		super.set(output);

		// for (CANTalon slaveMotor : this.slaveMotors) {
		// slaveMotor.set(this.master.getDeviceID());
		// }
	}
}
