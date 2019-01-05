package org.team1619.robot.behavior;

import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.behavior.Behavior;
import org.team1619.models.outputs.motors.Motor;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.InputValues;
import org.team1619.shared.abstractions.OutputValues;

import java.util.HashMap;
import java.util.Map;

public class Drive implements Behavior {

	private static final Logger sLogger = LoggerFactory.getLogger(Drive.class);
	private static final ImmutableSet<String> sSubsystems = ImmutableSet.of("ss_drive");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;

	private String fXAxis;
	private String fYAxis;
	private boolean fIsDriveGearLow;
	private String fIsDriveStraight;
	private Map<Integer, Double> fMotorCurrentValuesLeft;
	private Map<Integer, Double> fMotorCurrentValuesRight;


	public Drive(InputValues inputValues, OutputValues outputValues, Config config) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;

		fMotorCurrentValuesLeft = new HashMap<>();
		fMotorCurrentValuesRight = new HashMap<>();

		fXAxis = config.getString("x");
		fYAxis = config.getString("y");
		fIsDriveGearLow = config.getBoolean("is_drive_low");
		fIsDriveStraight = config.getString("drive_straight");
	}

	@Override
	public void initialize() {
		sLogger.debug("Drive Init");
	}

	@Override
	public void update() {
		double xAxis = fSharedInputValues.getNumeric(fXAxis);
		double yAxis = fSharedInputValues.getNumeric(fYAxis);
		boolean isDriveStraight = fSharedInputValues.getBoolean(fIsDriveStraight);

		if(isDriveStraight){
			xAxis = 0;
		}

		// Getting Current Values
		fMotorCurrentValuesLeft = fSharedOutputValues.getMotorCurrentValues("mo_drive_left");
		for(Map.Entry<Integer, Double> currentValuesLeftMap : fMotorCurrentValuesLeft.entrySet()){
			sLogger.debug("Motor {} has current value of {}", currentValuesLeftMap.getKey(), currentValuesLeftMap.getValue());
		}
		fMotorCurrentValuesRight = fSharedOutputValues.getMotorCurrentValues("mo_drive_right");
		for(Map.Entry<Integer, Double> currentValuesRightMap : fMotorCurrentValuesRight.entrySet()){
			sLogger.debug("Motor {} has a current value of {}", currentValuesRightMap.getKey(), currentValuesRightMap.getValue());
		}

		fSharedOutputValues.setMotorOutputValue("mo_drive_left", Motor.OutputType.PERCENT, yAxis - xAxis);
		fSharedOutputValues.setMotorOutputValue("mo_drive_right", Motor.OutputType.PERCENT, yAxis + xAxis);
		fSharedOutputValues.setSolenoidOutputValue("so_drive_gear", fIsDriveGearLow);
	}

	@Override
	public void dispose() {
		sLogger.debug("Drive Dispose");
		fSharedOutputValues.setMotorOutputValue("mo_drive_left", Motor.OutputType.PERCENT, 0.0);
		fSharedOutputValues.setMotorOutputValue("mo_drive_right", Motor.OutputType.PERCENT, 0.0);
		fSharedOutputValues.setSolenoidOutputValue("so_drive_gear", false);
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
