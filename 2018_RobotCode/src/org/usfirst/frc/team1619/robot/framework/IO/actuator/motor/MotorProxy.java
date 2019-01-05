package org.usfirst.frc.team1619.robot.framework.IO.actuator.motor;

import java.util.ArrayList;
import java.util.List;

public class MotorProxy implements Motor {

	private List<Motor> motors = new ArrayList<>();
	private Motor motor = null;

	@Override
	public void set(double output) {
		if (this.motor != null) {
			this.motor.set(output);
		}
	}

	@Override
	public void flush() {
		if (this.motor != null) {
			this.motor.flush();
		}
	}

	public int addMotor(Motor motor) {
		this.motors.add(motor);
		return this.motors.size() - 1;
	}

	public void setMotor(int key) {
		this.motor = this.motors.get(key);
	}
	
	public Motor getMotor(int key) {
		return this.motors.get(key);
	}

}
