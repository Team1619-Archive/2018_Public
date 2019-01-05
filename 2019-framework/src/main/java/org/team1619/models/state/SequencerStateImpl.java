package org.team1619.models.state;

import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.ModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.team1619.utilities.YamlConfigParser;

public class SequencerStateImpl implements State {

	private static Logger sLogger = LoggerFactory.getLogger(ParallelStateImpl.class);

	private final ModelFactory fModelFactory;
	private final Object fName;

	private List<State> fStates = new ArrayList<>();
	private int fCurrentStateIndex = 0;

	public SequencerStateImpl(ModelFactory modelFactory, Object name, YamlConfigParser parser, Config config) {
		fModelFactory = modelFactory;
		fName = name;

		for (Object stateName : config.getList("sequence")) {
			fStates.add(fModelFactory.createState(stateName, parser, parser.getConfig(stateName)));
		}

	}

	@Override
	public void initialize() {
		fStates.get(0).initialize();
	}

	@Override
	public void update() {
		if (fCurrentStateIndex >= fStates.size()) {
			return;
		}

		State current = fStates.get(fCurrentStateIndex);
		if (current.isDone()) {
			current.dispose();
			fCurrentStateIndex++;
			if (fCurrentStateIndex < fStates.size()) {
				current = fStates.get(fCurrentStateIndex);
				current.initialize();
			}

		} else {
			current.update();
		}
	}

	@Override
	public void dispose() {
		fStates.get(fStates.size() - 1).dispose();
		fCurrentStateIndex = 0;
	}

	@Override
	public boolean isDone() {
		return fCurrentStateIndex >= fStates.size();
	}

	@Override
	public ImmutableSet<String> getSubsystems() {
		return ImmutableSet.copyOf(fStates.stream()
				.map(State::getSubsystems).flatMap(Set::stream).collect(Collectors.toSet()));
	}

	@Override
	public Object getName() {
		return fName;
	}

}
