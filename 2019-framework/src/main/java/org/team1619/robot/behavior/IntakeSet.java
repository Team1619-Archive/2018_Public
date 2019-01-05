package org.team1619.robot.behavior;

import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.behavior.Behavior;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.InputValues;
import org.team1619.shared.abstractions.OutputValues;

public class IntakeSet implements Behavior {

	private static final Logger sLogger = LoggerFactory.getLogger(IntakeSet.class);
	private static final ImmutableSet<String> sSubsystems = ImmutableSet.of("ss_intake");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;

	private double fSetpoint;

	public IntakeSet(InputValues inputValues, OutputValues outputValues, Config config) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;

		fSetpoint = config.getDouble("setpoint");
	}

	@Override
	public void initialize() {
		sLogger.debug("Intake Init");
	}

	@Override
	public void update() {
		fSharedOutputValues.setMotorOutputValue("intake", Motor.OutputType.PERCENT, fSetpoint);
	}

	@Override
	public void dispose() {
		sLogger.debug("Intake Dispose");
		fSharedOutputValues.setMotorOutputValue("intake", Motor.OutputType.PERCENT, 0.0);
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
