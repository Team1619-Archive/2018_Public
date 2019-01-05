package org.team1619.shared.concretions.sim;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.inputs.bool.BooleanInput;
import org.team1619.models.inputs.bool.sim.SimButtonInput;
import org.team1619.models.inputs.bool.sim.SimDigitalInput;
import org.team1619.models.inputs.numeric.NumericInput;
import org.team1619.models.inputs.numeric.sim.SimAxisInput;
import org.team1619.models.inputs.numeric.sim.SimNavxInput;
import org.team1619.models.inputs.numeric.sim.SimTalonEncoderInput;
import org.team1619.models.inputs.vector.OdometryInput;
import org.team1619.models.inputs.vector.VectorInput;
import org.team1619.models.inputs.vector.sim.SimAccelerationInput;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.models.outputs.motors.TalonGroup;
import org.team1619.models.outputs.motors.sim.SimServo;
import org.team1619.models.outputs.motors.sim.SimTalon;
import org.team1619.models.outputs.solenoids.Solenoid;
import org.team1619.models.outputs.solenoids.sim.SimSolenoid;
import org.team1619.shared.abstractions.ObjectsDirectory;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.*;
import org.team1619.utilities.YamlConfigParser;

public class SimModelFactory extends ModelFactory {

	private static final Logger sLogger = LoggerFactory.getLogger(SimModelFactory.class);

	private final EventBus fEventBus;

	@Inject
	public SimModelFactory(EventBus eventBus, InputValues inputValues, OutputValues outputValues) {
		super(inputValues, outputValues);
		fEventBus = eventBus;
	}

	@Override
	public Motor createMotor(Object name, Config config, YamlConfigParser parser) {
		sLogger.info("Creating motor '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "talon":
				return new SimTalon(name, config);
			case "talon_group":
				return new TalonGroup(name, config, parser, this);
			case "servo":
				return new SimServo(name, config);
			default:
				return super.createMotor(name, config, parser);
		}
	}

	@Override
	public Solenoid createSolenoid(Object name, Config config, YamlConfigParser parser) {
		sLogger.info("Creating solenoid '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());
		switch (config.getType()) {
			case "solenoid":
				return new SimSolenoid(name, config);
			default:
				return super.createSolenoid(name, config, parser);
		}
	}

	@Override
	public BooleanInput createBooleanInput(Object name, Config config) {
		sLogger.info("Creating boolean input '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "joystick_button":
				return new SimButtonInput(fEventBus, name, config);
			case "controller_button":
				return new SimButtonInput(fEventBus, name, config);
			case "digital_input":
				return new SimDigitalInput(fEventBus, name, config);
			default:
				return super.createBooleanInput(name, config);
		}
	}

	@Override
	public NumericInput createNumericInput(Object name, Config config) {
		sLogger.info("Creating numeric input '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "joystick_axis":
				return new SimAxisInput(fEventBus, name, config);
			case "controller_axis":
				return new SimAxisInput(fEventBus, name, config);
			case "encoder":
				return new SimTalonEncoderInput(fEventBus, name, config);
			case "navx":
				return new SimNavxInput(fEventBus, name, config);
			default:
				return super.createNumericInput(name, config);
		}
	}

	@Override
	public VectorInput createVectorInput(ObjectsDirectory objectsDirectory, Object name, Config config) {
		sLogger.info("Creating vector input '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "accelerometer_input":
				return new SimAccelerationInput(fEventBus, name, config);
			case "odometry_input":
				return new OdometryInput(objectsDirectory, name, config);
			default:
				return super.createVectorInput(objectsDirectory, name, config);
		}
	}

}