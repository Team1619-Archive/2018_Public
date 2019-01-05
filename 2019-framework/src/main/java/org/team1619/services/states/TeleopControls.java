package org.team1619.services.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.state.State;
import org.team1619.shared.abstractions.InputValues;

public class TeleopControls {

	private static final Logger sLogger = LoggerFactory.getLogger(TeleopControls.class);
	private final InputValues fSharedInputValues;

	private boolean isFirst = true;

	public TeleopControls(InputValues inputValues) {
		fSharedInputValues = inputValues;
	}

	public boolean isReady(String name) {
		switch (name) {
			case "st_intake_on":
				return isIntakeOn();
			case "st_drive_low":
				return isDriveLow();
			case "st_drive_high":
				return true;
			case "st_servo_on":
				return isServoMoveOn();
//			case PARALLEL_TEST_STATE:
//				return isTestStateDone();
			case "st_sequencer_test":
				return isTestStateDone();
			default:
				return false;
		}
	}

	public boolean isDone(String name, State state) {
		switch (name) {
			case "st_intake_on":
				return !isIntakeOn();
			case "st_servo_on":
				return !isServoMoveOn();
			case "st_drive_low":
				return !isDriveLow();
			default:
				return state.isDone();
		}
	}

	private boolean isIntakeOn() {
		return fSharedInputValues.getBoolean("bi_intake");
	}

	private boolean isServoMoveOn(){
		return fSharedInputValues.getBoolean("bi_servo");
	}

	private boolean isDriveLow() {
		return fSharedInputValues.getBoolean("bi_drive_gear");
	}

	private boolean isTestStateDone() {
		if (isFirst) {
			isFirst = false;
			return true;
		}

		return false;
	}
}
