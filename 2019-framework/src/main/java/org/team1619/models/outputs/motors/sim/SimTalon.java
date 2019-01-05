package org.team1619.models.outputs.motors.sim;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.outputs.motors.Talon;
import org.team1619.utilities.Config;

import java.util.HashMap;
import java.util.Map;

public class SimTalon extends Talon {

	private static final Logger sLogger = LoggerFactory.getLogger(SimTalon.class);

	private double fOutput = 0.0;

	public SimTalon(Object name, Config config) {
		super(name, config);
	}

	@Override
	public void setHardware(OutputType outputType, double outputValue) {
		fOutput = outputValue;
		sLogger.trace("{}", outputValue);
	}

	@Override
	public Map<Integer, Double> getMotorCurrentValues(){
		Map<Integer, Double> motorCurrentValues = new HashMap<>();
		double motorCurrent = fDeviceNumber + 100.0;
		motorCurrentValues.put(fDeviceNumber, motorCurrent);
		sLogger.debug("Motor ID {} has Current Value of {}", fDeviceNumber, motorCurrent);
		return motorCurrentValues;
	}
}
