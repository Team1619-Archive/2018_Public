package org.usfirst.frc.team1619.robot.framework.IO.actuator.motor;

import edu.wpi.first.wpilibj.Servo;

public class ServoMotor implements Motor {

	private Servo motor;

	private double output = -1;

	public ServoMotor(int channel) {
		this.motor = new Servo(channel);
	}

	@Override
	public void set(double output) {
		this.output = output;
	}

	@Override
	public void flush() {
		if (this.output < 0) {
			this.motor.setDisabled();
		} else {
			this.motor.set(this.output);
		}

		this.output = -1;
	}
	
	public void disable() {
		this.output = -1;
	}
	
	public double getSetpoint() {
		return this.motor.getPosition();
	}

}