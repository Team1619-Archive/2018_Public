package org.usfirst.frc.team1619.robot.framework.state;

public abstract class State {

	protected abstract void initialize();

	protected abstract void update();

	protected abstract void dispose();

	public void initializeState(int subsystemId) {
		this.initialize();
	}

	public void updateState(int subsystemId) {
		this.update();
	}

	public void disposeState() {
		this.dispose();
	}

}
