package org.team1619.shared.abstractions;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.behavior.Behavior;
import org.team1619.robot.behavior.Drive;
import org.team1619.robot.behavior.IntakeSet;
import org.team1619.robot.behavior.Print;
import org.team1619.robot.behavior.ServoSet;
import org.team1619.models.exceptions.ConfigurationException;
import org.team1619.models.exceptions.ConfigurationTypeDoesNotExistException;
import org.team1619.models.inputs.bool.BooleanInput;
import org.team1619.models.inputs.numeric.NumericInput;
import org.team1619.models.inputs.vector.VectorInput;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.models.outputs.solenoids.Solenoid;
import org.team1619.models.state.*;
import org.team1619.utilities.Config;
import org.team1619.utilities.YamlConfigParser;

public abstract class  ModelFactory {
	private static final Logger sLogger = LoggerFactory.getLogger(ModelFactory.class);

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;

	@Inject
	public ModelFactory(InputValues inputValues, OutputValues outputValues) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;
	}

	public Motor createMotor(Object name, Config config, YamlConfigParser parser) {
		throw new ConfigurationTypeDoesNotExistException(config.getType());
	}

	public Solenoid createSolenoid(Object name, Config config, YamlConfigParser parser){
		throw new ConfigurationTypeDoesNotExistException(config.getType());
	}

	public BooleanInput createBooleanInput(Object name, Config config) {
		throw new ConfigurationTypeDoesNotExistException(config.getType());
	}

	public NumericInput createNumericInput(Object name, Config config) {
		throw new ConfigurationTypeDoesNotExistException(config.getType());
	}

	public VectorInput createVectorInput(ObjectsDirectory objectsDirectory, Object name, Config config) {
		throw new ConfigurationTypeDoesNotExistException(config.getType());
	}

	public Behavior createBehavior(String name, Config config) {
		sLogger.info("Creating behavior '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (name) {
			case "servo_set":
				return new ServoSet(fSharedInputValues, fSharedOutputValues, config);
			case "intake_set":
				return new IntakeSet(fSharedInputValues, fSharedOutputValues, config);
			case "bh_drive":
				return new Drive(fSharedInputValues, fSharedOutputValues, config);
			case "print":
				return new Print(config);
			default:
				throw new ConfigurationException("Behavior of name " + name + " does not exist.");
		}
	}

	public State createState(Object name, YamlConfigParser parser, Config config) {
		sLogger.info("Creating state '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "state":
				return new StateImpl(this, name, config);
			case "parallel_state":
				return new ParallelStateImpl(this, name, parser, config);
			case "sequencer_state":
				return new SequencerStateImpl(this, name, parser, config);
			case "timed_state":
				return new TimedStateImpl(this, name, parser, config);
			case "done_for_time_state":
				return new DoneForTimeStateImpl(this, name, parser, config);
			default:
				throw new ConfigurationException("State of name " + name + " does not exist.");
		}
	}

}
