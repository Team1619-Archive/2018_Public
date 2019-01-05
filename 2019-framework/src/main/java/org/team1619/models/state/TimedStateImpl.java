package org.team1619.models.state;

import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.utilities.Config;
import org.team1619.utilities.Timer;
import org.team1619.shared.abstractions.ModelFactory;
import org.team1619.utilities.YamlConfigParser;

public class TimedStateImpl implements State {

	private static final Logger sLogger = LoggerFactory.getLogger(TimedStateImpl.class);

	private final Object fName;
	private final State fState;
	private final Timer fTimer = new Timer();
	private final int fTimeout;

	public TimedStateImpl(ModelFactory modelFactory, Object name, YamlConfigParser parser, Config config) {
		fName = name;

		String stateName = config.getString("state");
		fState = modelFactory.createState(stateName, parser, parser.getConfig(stateName));

		fTimeout = config.getInt("timeout");
	}

	@Override
	public void initialize() {
		fTimer.start(fTimeout);
		fState.initialize();
	}

	@Override
	public void update() {
		fState.update();
	}

	@Override
	public void dispose() {
		fTimer.reset();
		fState.dispose();
	}

	@Override
	public boolean isDone() {
		return fState.isDone() || fTimer.isDone();
	}

	@Override
	public ImmutableSet<String> getSubsystems() {
		return fState.getSubsystems();
	}

	@Override
	public Object getName() {
		return fName;
	}
}
