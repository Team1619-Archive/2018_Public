package org.team1619.tests.states;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.state.State;
import org.team1619.services.states.StatesService;
import org.team1619.services.states.TeleopStateMachine;
import org.team1619.tests.TestModule;
import org.team1619.tests.utilities.TestModelFactory;
import org.team1619.shared.abstractions.FMS;
import org.team1619.shared.abstractions.InputValues;
import org.team1619.shared.abstractions.ModelFactory;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StatesServiceTest {

	private static Logger sLogger = LoggerFactory.getLogger(StatesServiceTest.class);

	private Injector fInjector;
	private StatesService fStatesService;

	private void initialize() throws Exception {
		fInjector = Guice.createInjector(new TestModule());
		fStatesService = fInjector.getInstance(StatesService.class);

		fStatesService.testStartUp();
	}

	//@Test
	public void testTeleop() throws Exception {
		initialize();

		FMS fms = fInjector.getInstance(FMS.class);
		InputValues inputValues = fInjector.getInstance(InputValues.class);

		TeleopStateMachine teleopStateMachine = fStatesService.getTeleopStateMachine();

		fms.setMode(FMS.Mode.TELEOP);

		fStatesService.testRunOneIteration();

		//On teleop init states service should reinitialize the teleop state machine
		assertNotEquals(teleopStateMachine, fStatesService.getTeleopStateMachine());

		assertStateActive("drive");
		assertStateInactive("intake_on");

		inputValues.setBoolean("intake", true);

		fStatesService.testRunOneIteration();

		assertStateActive("drive");
		assertStateActive("intake_on");

		fms.setMode(FMS.Mode.DISABLED);

		fStatesService.testRunOneIteration();

		fms.setMode(FMS.Mode.TELEOP);

		fStatesService.testRunOneIteration();

		assertStateActive("drive");
		assertStateActive("intake_on");
	}


	public void testParallelState() throws Exception {
		initialize();

		FMS fms = fInjector.getInstance(FMS.class);
		InputValues inputValues = fInjector.getInstance(InputValues.class);
		TestModelFactory modelFactory = (TestModelFactory) fInjector.getInstance(ModelFactory.class);


		State parallelTestState = (State) modelFactory.get("st_parallel_test");


		TeleopStateMachine teleopStateMachine = fStatesService.getTeleopStateMachine();

		fms.setMode(FMS.Mode.TELEOP);

		fStatesService.testRunOneIteration();


	}

	private void assertStateActive(String name) {
		checkNotNull(fStatesService.getTeleopStateMachine());
		Set<State> activeStates = fStatesService.getTeleopStateMachine().getCurrentActiveStates();
		assertEquals(1, activeStates.stream().filter(state -> state.getName().equals(name)).toArray().length);
	}

	private void assertStateInactive(String name) {
		checkNotNull(fStatesService.getTeleopStateMachine());
		Set<State> activeStates = fStatesService.getTeleopStateMachine().getCurrentActiveStates();
		assertEquals(0, activeStates.stream().filter(state -> state.getName().equals(name)).toArray().length);
	}
}
