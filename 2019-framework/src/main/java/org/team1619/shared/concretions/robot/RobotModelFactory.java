package org.team1619.shared.concretions.robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.inputs.bool.BooleanInput;
import org.team1619.models.inputs.bool.robot.ControllerButtonInput;
import org.team1619.models.inputs.bool.robot.RobotDigitalInput;
import org.team1619.models.inputs.bool.robot.RobotJoystickButtonInput;
import org.team1619.models.inputs.numeric.NumericInput;
import org.team1619.models.inputs.numeric.robot.RobotControllerAxisInput;
import org.team1619.models.inputs.numeric.robot.RobotJoystickAxisInput;
import org.team1619.models.inputs.numeric.robot.RobotNavxInput;
import org.team1619.models.inputs.numeric.robot.RobotTalonEncoderInput;
import org.team1619.models.inputs.vector.OdometryInput;
import org.team1619.models.inputs.vector.VectorInput;
import org.team1619.models.inputs.vector.robot.RobotAccelerationInput;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.models.outputs.motors.TalonGroup;
import org.team1619.models.outputs.motors.robot.RobotServo;
import org.team1619.models.outputs.motors.robot.RobotTalon;
import org.team1619.models.outputs.solenoids.Solenoid;
import org.team1619.models.outputs.solenoids.robot.RobotSolenoid;
import org.team1619.shared.abstractions.ObjectsDirectory;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.InputValues;
import org.team1619.shared.abstractions.ModelFactory;
import org.team1619.shared.abstractions.OutputValues;
import org.team1619.utilities.YamlConfigParser;

import javax.inject.Inject;

public class RobotModelFactory extends ModelFactory {

	private static final Logger sLogger = LoggerFactory.getLogger(RobotModelFactory.class);

	@Inject
	public RobotModelFactory(InputValues inputValues, OutputValues outputValues)
	{
		super(inputValues, outputValues);
	}

	@Override
	public Motor createMotor(Object name, Config config, YamlConfigParser parser) {
		sLogger.info("Creating motor '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "talon":
				return new RobotTalon(name, config);
			case "talon_group":
				return new TalonGroup(name, config, parser, this);
			case "servo":
				return new RobotServo(name, config);
			default:
				return super.createMotor(name, config, parser);
		}
	}

	@Override
	public Solenoid createSolenoid(Object name, Config config, YamlConfigParser parser){
		sLogger.info("Creating solenoid '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "solenoid":
				return new RobotSolenoid(name, config);
			default:
				return super.createSolenoid(name, config, parser);
		}
	}

	@Override
	public BooleanInput createBooleanInput(Object name, Config config) {
		sLogger.info("Creating boolean input '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "joystick_button":
				return new RobotJoystickButtonInput(name, config);
			case "controller_button":
				return new ControllerButtonInput(name, config);
			case "digital_input":
				return new RobotDigitalInput(name, config);
			default:
				return super.createBooleanInput(name, config);
		}
	}

	@Override
	public NumericInput createNumericInput(Object name, Config config) {
		sLogger.info("Creating numeric input '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "joystick_axis":
				return new RobotJoystickAxisInput(name, config);
			case "controller_axis":
				return new RobotControllerAxisInput(name, config);
			case "encoder":
				return new RobotTalonEncoderInput(name, config);
			case "navx":
				return new RobotNavxInput(name, config);
			default:
				return super.createNumericInput(name, config);
		}
	}

	@Override
	public VectorInput createVectorInput(ObjectsDirectory objectsDirectory, Object name, Config config) {
		sLogger.info("Creating vector input '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

		switch (config.getType()) {
			case "accelerometer_input":
				return new RobotAccelerationInput(name, config);
			case "odometry_input":
				return new OdometryInput(objectsDirectory, name, config);
			default:
				return super.createVectorInput(objectsDirectory, name, config);
		}
	}
}