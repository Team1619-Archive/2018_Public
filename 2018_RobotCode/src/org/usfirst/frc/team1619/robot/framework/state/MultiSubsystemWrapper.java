package org.usfirst.frc.team1619.robot.framework.state;

import java.util.Set;

public abstract class MultiSubsystemWrapper<T extends MultiSubsystemState> extends Wrapper<T> {

	protected abstract Set<Integer> getSubsystemIds();

	@Override
	public void prepareState() {
		if (this.getState() == null || this.isDoneState()) {
			super.prepareState();
		}
	}

	@Override
	public boolean isSubsystemValid(int subsystemId) {
		return this.getSubsystemIds().contains(subsystemId);
	}

	@Override
	public boolean isDoneState() {
		return super.isDoneState();
	}
}
