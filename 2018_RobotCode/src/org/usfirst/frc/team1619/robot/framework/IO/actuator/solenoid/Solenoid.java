package org.usfirst.frc.team1619.robot.framework.IO.actuator.solenoid;

public interface Solenoid {

	void set(boolean output);

	void flush();

	void reset();

}
