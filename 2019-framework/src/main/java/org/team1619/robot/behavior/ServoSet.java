package org.team1619.robot.behavior;

import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.behavior.Behavior;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.InputValues;
import org.team1619.shared.abstractions.OutputValues;

public class ServoSet implements Behavior {

	private static final Logger sLogger = LoggerFactory.getLogger(ServoSet.class);
	private static final ImmutableSet<String> sSubsystems = ImmutableSet.of("ss_servo");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;

	private double fSetpoint;

	public ServoSet(InputValues inputValues, OutputValues outputValues, Config config){
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;

		fSetpoint = config.getDouble("setpoint");
	}

	@Override
	public void initialize() {
		sLogger.debug("Servo Set Init");
	}

	@Override
	public void update() {
		fSharedOutputValues.setMotorOutputValue("mo_test_servo", Motor.OutputType.SERVO, fSetpoint);
	}

	@Override
	public void dispose() {
		fSharedOutputValues.setMotorOutputValue("mo_test_servo", Motor.OutputType.SERVO, -1);
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public ImmutableSet<String> getSubsystems() {
		return sSubsystems;
	}
}
