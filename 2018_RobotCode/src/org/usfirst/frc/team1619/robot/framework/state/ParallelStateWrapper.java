package org.usfirst.frc.team1619.robot.framework.state;

public abstract class ParallelStateWrapper<T extends ParallelState> extends MultiSubsystemWrapper<T> {

	@Override
	protected boolean isDone() {
		return this.getState().isDone();
	}

}
