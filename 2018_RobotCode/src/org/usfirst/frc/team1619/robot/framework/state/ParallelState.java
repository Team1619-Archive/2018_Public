package org.usfirst.frc.team1619.robot.framework.state;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * A state which consists of background states and foreground states running simultaneously. The life-cycle of the state ends 
 * when the longest foreground state ends. 
 */

public class ParallelState extends MultiSubsystemState {

	private class StatePair {

		public boolean isIdling = false;

		public Wrapper wrapper;
		public Wrapper idleWrapper;

		public StatePair(Wrapper wrapper, Wrapper idleWrapper) {
			this.wrapper = wrapper;
			this.idleWrapper = idleWrapper;
		}
	}

	public ParallelState(Set<Integer> subsystemIds) {
		super(subsystemIds);

	}

	private Set<Wrapper> backgroundStates = new HashSet<>();
	private Set<StatePair> statePairs = new HashSet<>();

	public void addStatePair(Wrapper wrapper, Wrapper idleWrapper) {
		this.statePairs.add(new StatePair(wrapper, idleWrapper));
	}

	public void addState(Wrapper wrapper) {
		this.addStatePair(wrapper, null);
	}

	public void addBackgroundState(Wrapper wrapper) {
		this.backgroundStates.add(wrapper);
	}

	@Override
	protected void initialize() {
		for (StatePair statePair : this.statePairs) {
			statePair.wrapper.prepareState();

			if (statePair.wrapper instanceof MultiSubsystemWrapper) {
				((MultiSubsystemWrapper<MultiSubsystemState>) statePair.wrapper).getState().bypassInitializeState();
			} else {
				statePair.wrapper.getState().initializeState(-1);
			}
		}
		for (Wrapper wrapper : this.backgroundStates) {
			wrapper.prepareState();

			if (wrapper instanceof MultiSubsystemWrapper<?>) {
				((MultiSubsystemWrapper<MultiSubsystemState>) wrapper).getState().bypassInitializeState();
			} else {
				wrapper.getState().initializeState(-1);
			}
		}

	}

	@Override
	protected void update() {
		Iterator<StatePair> iterator = this.statePairs.iterator();
		while (iterator.hasNext()) {
			StatePair statePair = iterator.next();

			if (!statePair.isIdling && statePair.wrapper.isDoneState()) {
				statePair.wrapper.getState().disposeState();
				statePair.isIdling = true;
				if (statePair.idleWrapper != null) {
					statePair.idleWrapper.prepareState();

					if (statePair.idleWrapper instanceof MultiSubsystemWrapper<?>) {
						((MultiSubsystemWrapper<MultiSubsystemState>) statePair.idleWrapper).getState()
								.bypassInitializeState();
					} else {
						statePair.idleWrapper.getState().initializeState(-1);
					}
				}

			}

			else if (statePair.isIdling) {
				if (statePair.idleWrapper != null) {
					if (statePair.idleWrapper instanceof MultiSubsystemWrapper) {
						((MultiSubsystemWrapper<MultiSubsystemState>) statePair.idleWrapper).getState()
								.bypassUpdateState();
					} else {
						statePair.idleWrapper.getState().updateState(-1);
					}
				}

			} else if (!statePair.isIdling && !statePair.wrapper.isDoneState()) {
				if (statePair.wrapper instanceof MultiSubsystemWrapper) {
					((MultiSubsystemWrapper<MultiSubsystemState>) statePair.wrapper).getState().bypassUpdateState();
				} else {
					statePair.wrapper.getState().updateState(-1);
				}
			}

		}

		for (Wrapper wrapper : this.backgroundStates) {
			if (wrapper instanceof MultiSubsystemWrapper) {
				((MultiSubsystemWrapper<MultiSubsystemState>) wrapper).getState().bypassUpdateState();
			} else {
				wrapper.getState().updateState(-1);
			}
		}

	}

	@Override
	protected void dispose() {
		for (StatePair statePair : this.statePairs) {
			if (!statePair.isIdling) {
				statePair.wrapper.getState().dispose();

			} else if (statePair.idleWrapper != null) {
				statePair.idleWrapper.getState().disposeState();
			}
		}
		for (Wrapper wrapper : this.backgroundStates) {
			wrapper.getState().disposeState();
		}

	}

	public boolean isDone() {
		for (StatePair statePair : this.statePairs) {
			if (!statePair.isIdling) {
				return false;
			}
		}

		return true;
	}

}
