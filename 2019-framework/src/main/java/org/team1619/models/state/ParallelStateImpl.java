package org.team1619.models.state;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.exceptions.ConfigurationInvalidTypeException;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.ModelFactory;
import org.team1619.utilities.YamlConfigParser;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParallelStateImpl implements State {

	private class StatePair {
		public boolean isIdling = false;

		public State state;
		@Nullable
		public State idleState;

		public StatePair(State state, @Nullable State idleState) {
			this.state = state;
			this.idleState = idleState;
		}

		public Set<State> getState() {
			return idleState == null ? ImmutableSet.of(state) : ImmutableSet.of(state, idleState);
		}
	}

	private static Logger sLogger = LoggerFactory.getLogger(ParallelStateImpl.class);

	private final ModelFactory fModelFactory;
	private final Object fName;

	private Set<StatePair> fForegroundStates = new HashSet<>();
	private Set<State> fBackgroundStates = new HashSet<>();

	public ParallelStateImpl(ModelFactory modelFactory, Object name, YamlConfigParser parser, Config config) {
		fModelFactory = modelFactory;
		fName = name;

		for (Object statePairConfig : config.getList("foreground_states")) {

			if (!(statePairConfig instanceof List)) {
				throw new ConfigurationInvalidTypeException("List", "foreground_states", statePairConfig);
			}
			List<Object> statePairList = (List<Object>) statePairConfig;

			Object stateName = statePairList.get(0);
			State state = fModelFactory.createState(stateName, parser, parser.getConfig(stateName));

			@Nullable
			State idleState = null;
			if (!statePairList.get(1).toString().equals("none")) {
				Object idleStateName = statePairList.get(1);
				idleState = fModelFactory.createState(idleStateName, parser, parser.getConfig(idleStateName));
			}

			StatePair statePair = new StatePair(state, idleState);

			fForegroundStates.add(statePair);
		}

		for (Object backgroundStateName : config.getList("background_states")) {
			fBackgroundStates.add(fModelFactory.createState(backgroundStateName, parser, parser.getConfig(backgroundStateName)));
		}

	}

	@Override
	public void initialize() {
		for (StatePair foregroundState : fForegroundStates) {
			foregroundState.state.initialize();
		}

		for (State backgroundState : fBackgroundStates) {
			backgroundState.initialize();
		}
	}

	@Override
	public void update() {
		Iterator<StatePair> iterator = fForegroundStates.iterator();
		while (iterator.hasNext()) {
			StatePair statePair = iterator.next();

			if (!statePair.isIdling && statePair.state.isDone()) {
				statePair.state.dispose();
				statePair.isIdling = true;
				if (statePair.idleState != null) {
					statePair.idleState.initialize();
				}
			} else if (statePair.isIdling) {
				if (statePair.idleState != null) {
					statePair.idleState.update();
				}
			} else if (!statePair.state.isDone()) {
				statePair.state.update();
			}
		}

		for (State backgroundState : fBackgroundStates) {
			backgroundState.update();
		}
	}

	@Override
	public void dispose() {
		for (StatePair statePair : fForegroundStates) {
			if (!statePair.isIdling) {
				statePair.state.dispose();

			} else if (statePair.idleState != null) {
				statePair.idleState.dispose();
			}
		}

		for (State backgroundState : fBackgroundStates) {
			backgroundState.dispose();
		}
	}

	@Override
	public boolean isDone() {
		for (StatePair statePair : fForegroundStates) {
			if (!statePair.isIdling) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ImmutableSet<String> getSubsystems() {
		return ImmutableSet.copyOf(Streams.concat(
				fForegroundStates.stream()
						.map(StatePair::getState)
						.flatMap(Set::stream)
						.map(State::getSubsystems),
				fBackgroundStates.stream()
						.map(State::getSubsystems)
		).flatMap(Set::stream).collect(Collectors.toSet()));
	}

	@Override
	public Object getName() {
		return fName;
	}
}
