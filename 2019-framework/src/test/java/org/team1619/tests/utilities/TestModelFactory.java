package org.team1619.tests.utilities;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.NonNullByDefault;
import org.team1619.models.inputs.bool.BooleanInput;
import org.team1619.models.inputs.bool.sim.SimButtonInput;
import org.team1619.models.inputs.numeric.NumericInput;
import org.team1619.models.inputs.numeric.sim.SimAxisInput;
import org.team1619.models.inputs.numeric.sim.SimNavxInput;
import org.team1619.models.inputs.numeric.sim.SimTalonEncoderInput;
import org.team1619.models.inputs.vector.OdometryInput;
import org.team1619.models.inputs.vector.VectorInput;
import org.team1619.models.inputs.vector.sim.SimAccelerationInput;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.models.outputs.motors.TalonGroup;
import org.team1619.models.outputs.motors.sim.SimTalon;
import org.team1619.shared.abstractions.ObjectsDirectory;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.*;
import org.team1619.utilities.YamlConfigParser;

import java.util.HashMap;
import java.util.Map;

@NonNullByDefault
public class TestModelFactory extends ModelFactory {

	private Map<Object, Object> fObjects = new HashMap<>();

	private static final Logger sLogger = LoggerFactory.getLogger(TestModelFactory.class);

	private final EventBus fEventBus;

	@Inject
	public TestModelFactory(EventBus eventBus, InputValues inputValues, OutputValues outputValues) {
		super(inputValues, outputValues);
		fEventBus = eventBus;
	}

	@Override
	public Motor createMotor(Object name, Config config, YamlConfigParser parser) {
		sLogger.info("Creating motor '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "talon":
				SimTalon simTalon = new SimTalon(name, config);
				fObjects.put(name, simTalon);
				return simTalon;
			case "talon_group":
				TalonGroup talonGroup = new TalonGroup(name, config, parser, this);
				fObjects.put(name, talonGroup);
				return talonGroup;
			default:
				return super.createMotor(name, config, parser);
		}
	}

	@Override
	public BooleanInput createBooleanInput(Object name, Config config) {
		sLogger.info("Creating boolean input '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "joystick_button":
			case "controller_button":
				SimButtonInput simButtonInput = new SimButtonInput(fEventBus, name, config);
				fObjects.put(name, simButtonInput);
				return simButtonInput;
			default:
				return super.createBooleanInput(name, config);
		}
	}

	@Override
	public NumericInput createNumericInput(Object name, Config config) {
		sLogger.info("Creating numeric input '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "joystick_axis":
			case "controller_axis":
				SimAxisInput simAxisInput = new SimAxisInput(fEventBus, name, config);
				fObjects.put(name, simAxisInput);
				return simAxisInput;
			case "encoder":
				SimTalonEncoderInput simTalonEncoderInput = new SimTalonEncoderInput(fEventBus, name, config);
				fObjects.put(name, simTalonEncoderInput);
				return simTalonEncoderInput;
			case "navx":
				SimNavxInput simNavxInput = new SimNavxInput(fEventBus, name, config);
				fObjects.put(name, simNavxInput);
				return simNavxInput;
			default:
				return super.createNumericInput(name, config);
		}
	}

	@Override
	public VectorInput createVectorInput(ObjectsDirectory objectsDirectory, Object name, Config config) {
		sLogger.info("Creating vector input '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "accelerometer_input":
				SimAccelerationInput simAccelerationInput = new SimAccelerationInput(fEventBus, name, config);
				fObjects.put(name, simAccelerationInput);
				return simAccelerationInput;
			case "odometry_input":
				OdometryInput odometry = new OdometryInput(objectsDirectory, name, config);
				fObjects.put(name, odometry);
				return odometry;
			default:
				return super.createVectorInput(objectsDirectory, name, config);
		}
	}

	public Object get(Object name) {
		return fObjects.get(name);
	}
}