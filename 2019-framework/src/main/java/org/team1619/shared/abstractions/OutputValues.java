package org.team1619.shared.abstractions;


import org.team1619.models.outputs.motors.Motor;

import java.util.Map;

public interface OutputValues {

	//Motor
	double getMotorOutputValue(String motorName);
	Motor.OutputType getMotorType(String motorName);
	void setMotorOutputValue(String motorName, Motor.OutputType outputType, double outputValue);
	void putMotorCurrentValues(String motorName, Map<Integer, Double> motorCurrentValues);
	Map<Integer, Double> getMotorCurrentValues(String motorName);

	//Solenoid
	boolean getSolenoidOutputValue(String solenoidName);
	void setSolenoidOutputValue(String solenoidName, boolean outputValue);


}
