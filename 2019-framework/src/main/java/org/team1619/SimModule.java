package org.team1619;

import com.google.inject.AbstractModule;
import org.team1619.shared.concretions.SharedRobotConfiguration;
import org.team1619.shared.abstractions.*;
import org.team1619.shared.concretions.*;
import org.team1619.shared.concretions.sim.SimModelFactory;
import org.team1619.utilities.YamlConfigParser;

public class SimModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).to(SharedEventBus.class);
		bind(InputValues.class).to(SharedInputValues.class);
		bind(OutputValues.class).to(SharedOutputValues.class);
		bind(FMS.class).to(SharedFMS.class);
		bind(ModelFactory.class).to(SimModelFactory.class);
		bind(RobotConfiguration.class).to(SharedRobotConfiguration.class);
		bind(ObjectsDirectory.class).to(SharedObjectsDirectory.class);
	}
}
