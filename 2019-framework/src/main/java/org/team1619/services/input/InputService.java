package org.team1619.services.input;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.inputs.bool.BooleanInput;
import org.team1619.models.inputs.numeric.NumericInput;
import org.team1619.models.inputs.vector.VectorInput;
import org.team1619.shared.abstractions.InputValues;
import org.team1619.shared.abstractions.ObjectsDirectory;
import org.team1619.shared.abstractions.RobotConfiguration;
import org.team1619.shared.abstractions.ModelFactory;
import org.team1619.utilities.YamlConfigParser;

import java.util.concurrent.TimeUnit;

public class InputService extends AbstractScheduledService {

	private static final Logger sLogger = LoggerFactory.getLogger(InputService.class);

	private final InputValues fSharedInputValues;
	private final ObjectsDirectory fSharedObjectsDirectory;
	private final RobotConfiguration fRobotConfiguration;
	private final YamlConfigParser fBooleanParser;
	private final YamlConfigParser fNumericParser;
	private final YamlConfigParser fVectorParser;

	@Inject
	public InputService(ModelFactory modelFactory, InputValues inputValues, RobotConfiguration robotConfiguration, ObjectsDirectory objectsDirectory) {
		fSharedInputValues = inputValues;
		fRobotConfiguration = robotConfiguration;
		fSharedObjectsDirectory = objectsDirectory;
		fBooleanParser = new YamlConfigParser();
		fNumericParser = new YamlConfigParser();
		fVectorParser = new YamlConfigParser();
	}

	@Override
	protected void startUp() throws Exception {
		sLogger.info("Starting InputService");
		fBooleanParser.load("boolean-inputs");
		fNumericParser.load("numeric-inputs");
		fVectorParser.load("vector-inputs");
		fSharedObjectsDirectory.registerAllInputs(fBooleanParser, fNumericParser, fVectorParser);
		sLogger.info("InputService started");
	}

	@Override
	protected void runOneIteration() throws Exception {
		for (String name : fRobotConfiguration.getBooleanInputNames()) {
			BooleanInput booleanInput = fSharedObjectsDirectory.getBooleanInputObject(name);
			booleanInput.update();
			fSharedInputValues.setBoolean(name, booleanInput.get());
		}

		sLogger.debug("Updated boolean inputs");

		for (String name : fRobotConfiguration.getNumericInputNames()) {
			NumericInput numericInput = fSharedObjectsDirectory.getNumericInputObject(name);
			numericInput.update();
			double delta = numericInput.getDelta();
			fSharedInputValues.setNumeric(name, numericInput.get());
		}

		sLogger.debug("Updated numeric inputs");

		for (String name : fRobotConfiguration.getVectorInputNames()) {
			VectorInput vectorInput = fSharedObjectsDirectory.getVectorInputObject(name);
			vectorInput.update();
			fSharedInputValues.setVector(name, vectorInput.get());
		}

		sLogger.debug("Updated vector inputs");
	}

	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedDelaySchedule(0, 1000 / 60, TimeUnit.MILLISECONDS);
	}

	public void broadcast() {
		sLogger.info("Broadcasting input values");

		for (String name : fRobotConfiguration.getBooleanInputNames()) {
			BooleanInput booleanInput = fSharedObjectsDirectory.getBooleanInputObject(name);
			fSharedInputValues.setBoolean(name, booleanInput.get());
		}

		for (String name : fRobotConfiguration.getNumericInputNames()) {
			NumericInput numericInput = fSharedObjectsDirectory.getNumericInputObject(name);
			fSharedInputValues.setNumeric(name, numericInput.get());
		}

		for (String name : fRobotConfiguration.getVectorInputNames()) {
			VectorInput vectorInput = fSharedObjectsDirectory.getVectorInputObject(name);
			fSharedInputValues.setVector(name, vectorInput.get());
		}
	}
}
