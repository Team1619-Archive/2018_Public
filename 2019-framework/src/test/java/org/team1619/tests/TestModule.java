package org.team1619.tests;

import com.google.inject.AbstractModule;
import org.team1619.tests.utilities.TestYamlConfigParser;
import org.team1619.shared.abstractions.*;
import org.team1619.shared.concretions.SharedEventBus;
import org.team1619.shared.concretions.SharedFMS;
import org.team1619.shared.concretions.SharedInputValues;
import org.team1619.shared.concretions.SharedOutputValues;
import org.team1619.shared.concretions.sim.SimModelFactory;

public class TestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).to(SharedEventBus.class);
		bind(InputValues.class).to(SharedInputValues.class);
		bind(OutputValues.class).to(SharedOutputValues.class);
		bind(FMS.class).to(SharedFMS.class);
		bind(ModelFactory.class).to(SimModelFactory.class);
	}
}
