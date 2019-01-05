package org.team1619.services.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.state.State;
import org.team1619.shared.abstractions.ObjectsDirectory;
import org.team1619.shared.abstractions.RobotConfiguration;

import java.util.HashSet;
import java.util.Set;

public class TeleopStateMachine {

	private static final Logger sLogger = LoggerFactory.getLogger(TeleopStateMachine.class);

	private final ObjectsDirectory fSharedObjectsDirectory;
	private final TeleopControls fTeleopControls;
	private final RobotConfiguration fRobotConfiguration;

	private Set<State> fActiveStates = new HashSet<>();

	public TeleopStateMachine(ObjectsDirectory objectsDirectory, TeleopControls teleopControls, RobotConfiguration robotConfiguration) {
		fSharedObjectsDirectory = objectsDirectory;
		fTeleopControls = teleopControls;
		fRobotConfiguration = robotConfiguration;
	}

	public void update() {
		Set<State> nextActiveStates = getActiveStates();

		dispose(nextActiveStates);
		initialize(nextActiveStates);

		for (State state : nextActiveStates) {
			state.update();
		}

		fActiveStates = nextActiveStates;
	}

	public void dispose() {
		for (State state : fActiveStates) {
			state.dispose();
		}

		fActiveStates = new HashSet<>();
	}

	private Set<State> getActiveStates() {
		Set<State> activeStates = new HashSet<>();
		Set<String> subsystems = fRobotConfiguration.getSubsystemNames();
		for (String name : fRobotConfiguration.getStateNames()) {
			if (subsystems.isEmpty()) {
				break;
			}

			State state = fSharedObjectsDirectory.getStateObject(name);
			boolean isReady = fTeleopControls.isReady(name);
			if (isReady || (fActiveStates.contains(state) && !fTeleopControls.isDone(name, state))) {
				boolean valid = true;

				for (String subsystemName : state.getSubsystems()) {
					if (!subsystems.contains(subsystemName)) {
						valid = false;
						break;
					}
				}

				if (valid) {
					for (String subsystemName : state.getSubsystems()) {
						subsystems.remove(subsystemName);
					}

					activeStates.add(state);
				}
			}
		}

		return activeStates;
	}

	private void initialize(Set<State> nextActiveStates) {
		for (String name : fRobotConfiguration.getStateNames()) {
			State state = fSharedObjectsDirectory.getStateObject(name);

			boolean isInCurrent = fActiveStates.contains(state);
			boolean isInNext = nextActiveStates.contains(state);

			if (isInNext && !isInCurrent) {
				state.initialize();

			}
		}
	}

	private void dispose(Set<State> nextActiveStates) {
		for (String name : fRobotConfiguration.getStateNames()) {
			State state = fSharedObjectsDirectory.getStateObject(name);

			boolean isInCurrent = fActiveStates.contains(state);
			boolean isInNext = nextActiveStates.contains(state);

			if (isInCurrent && !isInNext) {
				state.dispose();
			}
		}
	}

	public Set<State> getCurrentActiveStates() {
		return fActiveStates;
	}
}
