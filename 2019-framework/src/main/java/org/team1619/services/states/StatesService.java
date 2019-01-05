package org.team1619.services.states;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.shared.abstractions.*;
import org.team1619.utilities.YamlConfigParser;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

public class StatesService extends AbstractScheduledService {

	private static final Logger sLogger = LoggerFactory.getLogger(StatesService.class);
	private final ObjectsDirectory fSharedObjectsDirectory;
	private final InputValues fSharedInputValues;
	private final FMS fFms;
	private final YamlConfigParser fParser;
	private final RobotConfiguration fRobotConfiguration;

	private FMS.Mode fCurrentFmsMode;
	@Nullable
	private TeleopStateMachine fTeleopStateMachine;

	@Nullable
	private org.team1619.models.state.State fAutonomousState;

	@Inject
	public StatesService(ModelFactory modelFactory, InputValues inputValues, FMS fms, RobotConfiguration robotConfiguration, ObjectsDirectory objectsDirectory) {
		fParser = new YamlConfigParser();
		fSharedObjectsDirectory = objectsDirectory;
		fSharedInputValues = inputValues;
		fFms = fms;
		fCurrentFmsMode = fFms.getMode();
		fRobotConfiguration = robotConfiguration;
	}

	@Override
	protected void startUp() throws Exception {
		sLogger.info("Starting StatesService");

		fParser.load("states");
		fSharedObjectsDirectory.registerAllStates(fParser);

		sLogger.info("StatesService started");
	}

	@Override
	protected void runOneIteration() {
		FMS.Mode nextFmsMode = fFms.getMode();
		if (nextFmsMode != fCurrentFmsMode) {
			if (nextFmsMode == FMS.Mode.TELEOP) {
				sLogger.info("Starting teleop");

				TeleopControls teleopControls = new TeleopControls(fSharedInputValues);
				fTeleopStateMachine = new TeleopStateMachine(fSharedObjectsDirectory, teleopControls, fRobotConfiguration);
			} else if (fCurrentFmsMode == FMS.Mode.TELEOP && fTeleopStateMachine != null) {
				sLogger.info("Ending teleop");

				fTeleopStateMachine.dispose();
			} else if (nextFmsMode == FMS.Mode.AUTONOMOUS) {
				sLogger.info("Starting auton");

				fAutonomousState = fSharedObjectsDirectory.getStateObject(fRobotConfiguration.getString("autonomous", "state"));
				fAutonomousState.initialize();
			} else if (fCurrentFmsMode == FMS.Mode.AUTONOMOUS && fAutonomousState != null) {
				sLogger.info("Ending auton");

				fAutonomousState.dispose();
			}
		}

		fCurrentFmsMode = nextFmsMode;
		if (fCurrentFmsMode == FMS.Mode.TELEOP && fTeleopStateMachine != null) {
			fTeleopStateMachine.update();
		} else if (fCurrentFmsMode == FMS.Mode.AUTONOMOUS && fAutonomousState != null) {
			fAutonomousState.update();
		}
	}

	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedRateSchedule(0, 1000 / 60, TimeUnit.MILLISECONDS);
	}

	public void testStartUp() throws Exception {
		startUp();

		sLogger.info("Started in test mode");
	}

	public void testRunOneIteration() {
		runOneIteration();

		sLogger.info("Ran one iteration in test mode");
	}

	@Nullable
	public TeleopStateMachine getTeleopStateMachine() {
		return fTeleopStateMachine;
	}
}
