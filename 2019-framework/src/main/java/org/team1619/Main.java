package org.team1619;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.services.input.InputService;
import org.team1619.services.output.OutputService;
import org.team1619.services.sim.SimInputSocketListenerService;
import org.team1619.services.states.StatesService;
import org.team1619.shared.concretions.SharedRobotConfiguration;

public class Main {

	private static final Logger sLogger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new SimModule());

		injector.getInstance(SharedRobotConfiguration.class).initialize();

		StatesService statesService = injector.getInstance(StatesService.class);
		InputService inputService = injector.getInstance(InputService.class);
		OutputService outputService = injector.getInstance(OutputService.class);
		SimInputSocketListenerService simInputSocketListenerService = injector.getInstance(SimInputSocketListenerService.class);

		ServiceManager serviceManager = new ServiceManager(ImmutableSet.of(statesService, inputService, outputService, simInputSocketListenerService));
		sLogger.info("Starting services");
		serviceManager.startAsync();
		serviceManager.awaitHealthy();
		inputService.broadcast();
		sLogger.info("All Services started");
		serviceManager.awaitStopped();
		sLogger.info("All Services stopped");
	}
}
