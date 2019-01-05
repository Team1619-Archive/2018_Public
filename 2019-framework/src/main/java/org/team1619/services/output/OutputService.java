package org.team1619.services.output;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.models.outputs.solenoids.Solenoid;
import org.team1619.shared.abstractions.ObjectsDirectory;
import org.team1619.shared.abstractions.OutputValues;
import org.team1619.shared.abstractions.RobotConfiguration;
import org.team1619.shared.abstractions.ModelFactory;
import org.team1619.utilities.YamlConfigParser;


import java.util.concurrent.TimeUnit;

public class OutputService extends AbstractScheduledService {

	private static final Logger sLogger = LoggerFactory.getLogger(OutputService.class);

	private final OutputValues fSharedOutputValues;
	private final ObjectsDirectory fSharedOutputsDirectory;
	private final RobotConfiguration fRobotConfiguration;
	private final YamlConfigParser fMotorsParser;
	private final YamlConfigParser fSolenoidsParser;



	@Inject
	public OutputService(ModelFactory modelFactory, OutputValues outputValues, RobotConfiguration robotConfiguration, ObjectsDirectory objectsDirectory) {
		fSharedOutputValues = outputValues;
		fRobotConfiguration = robotConfiguration;
		fSharedOutputsDirectory = objectsDirectory;
		fMotorsParser = new YamlConfigParser();
		fSolenoidsParser = new YamlConfigParser();

	}

	@Override
	protected void startUp() throws Exception {
		sLogger.info("Starting OutputService");
		fMotorsParser.load("motors");
		fSolenoidsParser.load("solenoids");
		fSharedOutputsDirectory.registerAllOutputs(fMotorsParser, fSolenoidsParser);

		sLogger.info("OutputService started");
	}

	@Override
	protected void runOneIteration() throws Exception {
		for (String motorName : fRobotConfiguration.getMotorNames()) {
			Motor motorObject = fSharedOutputsDirectory.getMotorObject(motorName);
			motorObject.setHardware(fSharedOutputValues.getMotorType(motorName), fSharedOutputValues.getMotorOutputValue(motorName));
			fSharedOutputValues.putMotorCurrentValues(motorName, motorObject.getMotorCurrentValues());
		}
		for (String solenoidName  : fRobotConfiguration.getSolenoidNames()){
			Solenoid solenoidObject = fSharedOutputsDirectory.getSolenoidObject(solenoidName);
			solenoidObject.setHardware(fSharedOutputValues.getSolenoidOutputValue(solenoidName));
		}
	}

	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedDelaySchedule(0, 1000 / 60, TimeUnit.MILLISECONDS);
	}
}
