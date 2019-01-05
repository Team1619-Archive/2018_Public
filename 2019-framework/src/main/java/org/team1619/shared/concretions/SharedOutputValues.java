package org.team1619.shared.concretions;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.shared.abstractions.OutputValues;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class SharedOutputValues implements OutputValues {

	private static final Logger sLogger = LoggerFactory.getLogger(SharedOutputValues.class);

	private Map<String, Double> fMotorOutputsValues = new ConcurrentHashMap<>();
	private Map<String, Motor.OutputType> fMotorOutputTypes = new ConcurrentHashMap<>();
	private Map<String, Boolean> fSolenoidOutputsValues = new ConcurrentHashMap<>();
	private Map<String, Map<Integer, Double>> fMotorCurrentValues = new ConcurrentHashMap<>();

	//Motor
	@Override
	public double getMotorOutputValue(String motorName) {
		return fMotorOutputsValues.getOrDefault(motorName, 0.0);
	}

	@Override
	public Motor.OutputType getMotorType(String motorName) {
		return fMotorOutputTypes.getOrDefault(motorName, Motor.OutputType.PERCENT);
	}

	@Override
	public void setMotorOutputValue(String motorName, Motor.OutputType motorType, double outputValue) {
		sLogger.debug("Setting motor '{}' to {} ({})", motorName, outputValue, motorType);
		fMotorOutputsValues.put(motorName, outputValue);
		fMotorOutputTypes.put(motorName, motorType);
	}

	@Override
	public void putMotorCurrentValues(String motorName, Map<Integer, Double> motorCurrentValues){
		fMotorCurrentValues.put(motorName, motorCurrentValues);
	}

	@Override
	public Map<Integer, Double> getMotorCurrentValues(String motorName){
		return fMotorCurrentValues.get(motorName);
	}

	//Solenoid
	@Override
	public boolean getSolenoidOutputValue(String solenoidName) {
		return fSolenoidOutputsValues.getOrDefault(solenoidName, false);
	}

	@Override
	public void setSolenoidOutputValue(String solenoidName, boolean outputValue) {
		sLogger.debug("Setting solenoid '{}' to {}", solenoidName, outputValue);
		fSolenoidOutputsValues.put(solenoidName, outputValue);
	}

}
