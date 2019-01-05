package org.team1619.models.state;

import com.google.common.collect.ImmutableSet;
import org.team1619.utilities.Config;
import org.team1619.utilities.Timer;
import org.team1619.shared.abstractions.ModelFactory;
import org.team1619.utilities.YamlConfigParser;

public class DoneForTimeStateImpl implements State {

	private final Object fName;
	private final State fState;

	private final Timer fStateTimer = new Timer();
	private final Timer fMaxTimer = new Timer();

	private final int fStateTimeout;

	private int fMaxTimeout;

	public DoneForTimeStateImpl(ModelFactory modelFactory, Object name, YamlConfigParser parser, Config config) {
		fName = name;

		String stateName = config.getString("state");
		fState = modelFactory.createState(stateName, parser, parser.getConfig(stateName));

		fStateTimeout = config.getInt("state_timeout");
		fMaxTimeout = config.getInt("max_timeout", -1);
	}

	@Override
	public void initialize() {
		fStateTimer.start(fStateTimeout);

		if (fMaxTimeout != -1) {
			fMaxTimer.start(fMaxTimeout);
		}
		fState.initialize();
	}

	@Override
	public void update() {
		fState.update();
	}

	@Override
	public void dispose() {
		fStateTimer.reset();
		fState.dispose();
	}

	@Override
	public boolean isDone() {
		if (fState.isDone()) {
			if (!fStateTimer.isStarted()) {
				fStateTimer.start(fStateTimeout);
			}

		} else if (fStateTimer.isStarted()) {
			fStateTimer.reset();
		}

		return fStateTimer.isDone() || fMaxTimer.isDone();
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
