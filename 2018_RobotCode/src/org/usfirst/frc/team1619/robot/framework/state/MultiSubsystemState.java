package org.usfirst.frc.team1619.robot.framework.state;

import java.util.HashSet;
import java.util.Set;

public abstract class MultiSubsystemState extends State {

	private Set<Integer> requiredSubsystems = new HashSet<>();
	private int finalSubsystemId = -1;

	private boolean active = false;

	public MultiSubsystemState(Set<Integer> subsystemIds) {
		for (int id : subsystemIds) {
			this.requiredSubsystems.add(id);
		}
	}

	@Override
	public void initializeState(int subsystemId) {
		this.requiredSubsystems.remove(subsystemId);
		if (this.requiredSubsystems.isEmpty()) {
			this.active = true;
			this.finalSubsystemId = subsystemId;
			super.initializeState(subsystemId);
		}
	}

	public void bypassInitializeState() {
		this.requiredSubsystems.clear();
		this.active = true;
		super.initializeState(-1);
	}

	public void bypassUpdateState() {
		super.updateState(-1);
	}

	@Override
	public void updateState(int subsystemId) {
		if (this.active && subsystemId == this.finalSubsystemId) {
			super.updateState(subsystemId);
		}
	}

	@Override
	public void disposeState() {
		super.disposeState();
	}

}
