package org.team1619.models.state;

import com.google.common.collect.ImmutableSet;
import org.team1619.models.behavior.Behavior;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.ModelFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class StateImpl implements State {

	private final ModelFactory fModelFactory;
	private final Object fName;

	private Behavior fBehavior;

	private String fBehaviorName;
	private Config fBehaviorConfig;

	public StateImpl(ModelFactory modelFactory, Object name, Config config) {
		fModelFactory = modelFactory;
		fName = name;

		fBehaviorName = config.getString("behavior");
		fBehaviorConfig = config.getSubConfig("behavior_config", "behavior_config");

		fBehavior = fModelFactory.createBehavior(fBehaviorName, fBehaviorConfig);
	}


	@Override
	public void initialize() {
		fBehavior.initialize();
	}

	@Override
	public void update() {
		checkNotNull(fBehavior);
		fBehavior.update();

	}

	@Override
	public void dispose() {
		checkNotNull(fBehavior);
		fBehavior.dispose();
		fBehavior = fModelFactory.createBehavior(fBehaviorName, fBehaviorConfig);
	}

	@Override
	public boolean isDone() {
		checkNotNull(fBehavior);
		return fBehavior.isDone();
	}

	@Override
	public ImmutableSet<String> getSubsystems() {
		return fBehavior.getSubsystems();
	}

	@Override
	public Object getName() {
		return fName;
	}
}
