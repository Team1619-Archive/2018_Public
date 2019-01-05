package org.team1619.models.outputs.motors;

import org.team1619.utilities.Config;

public abstract class Servo implements Motor {

	protected final Object fName;
	protected final int fChannel;

	public Servo(Object name, Config config) {
		fName = name;

		fChannel = config.getInt("device_channel");
	}

	public double getMotorOutput(String name){
		return 0.0;
	}

	public Motor.OutputType getMotorType(String name){
		return OutputType.SERVO;
	}

	public int getChannel() {
		return fChannel;
	}
}
