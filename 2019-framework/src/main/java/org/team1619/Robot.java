package org.team1619;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.wpi.first.wpilibj.IterativeRobot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.services.input.InputService;
import org.team1619.services.output.OutputService;
import org.team1619.services.states.StatesService;
import org.team1619.shared.abstractions.RobotConfiguration;
import org.team1619.shared.abstractions.FMS;

public class Robot extends IterativeRobot {

	private static final Logger sLogger = LoggerFactory.getLogger(Main.class);

	private Injector fInjector;
	private ServiceManager fServiceManager;
	private InputService fInputService;
	private FMS fFMS;

	public Robot() {
		fInjector = Guice.createInjector(new RobotModule());
		StatesService statesService = fInjector.getInstance(StatesService.class);
		fInputService = fInjector.getInstance(InputService.class);
		OutputService outputService = fInjector.getInstance(OutputService.class);
		fServiceManager = new ServiceManager(ImmutableSet.of(statesService, fInputService, outputService));
		fFMS = fInjector.getInstance(FMS.class);
	}


	@Override
	public void robotInit() {
		sLogger.info("Initializing RobotConfiguration");
		fInjector.getInstance(RobotConfiguration.class).initialize();
		sLogger.info("Starting services");
		fServiceManager.startAsync();
		fServiceManager.awaitHealthy();
		fInputService.broadcast();
		sLogger.info("All Services started");
	}

	@Override
	public void teleopInit() {
		fFMS.setMode(FMS.Mode.TELEOP);
	}

	@Override
	public void autonomousInit() {
		fFMS.setMode(FMS.Mode.AUTONOMOUS);
	}

	@Override
	public void disabledInit() {
		fFMS.setMode(FMS.Mode.DISABLED);
	}

	@Override
	public void testInit() {
		fFMS.setMode(FMS.Mode.TEST);
	}
}
