package org.team1619.models.outputs.motors.sim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.models.outputs.motors.Servo;
import org.team1619.utilities.Config;

import java.util.HashMap;
import java.util.Map;

public class SimServo extends Servo {
	private static final Logger sLogger = LoggerFactory.getLogger(SimServo.class);

	private double fOutput = 0.0;

	public SimServo(Object name, Config config) {
		super(name, config);
	}

	@Override
	public void setHardware(Motor.OutputType outputType, double outputValue) {
		fOutput = outputValue;
		sLogger.trace("{}", outputValue);
	}

	@Override
	public Map<Integer, Double> getMotorCurrentValues(){
		Map<Integer, Double> motorCurrentValues = new HashMap<>();
		motorCurrentValues.put(fChannel, -1.0);
		return motorCurrentValues;
	}
}
