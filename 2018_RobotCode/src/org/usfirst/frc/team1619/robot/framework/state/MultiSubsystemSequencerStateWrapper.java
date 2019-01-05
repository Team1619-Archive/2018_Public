package org.usfirst.frc.team1619.robot.framework.state;

public abstract class MultiSubsystemSequencerStateWrapper<T extends MultiSubsystemSequencerState>
		extends MultiSubsystemWrapper<T> {

	/**
	 * Returns true if this MultiSubsystemSequencerState is complete
	 */
	@Override
	protected boolean isDone() {
		return this.getState().isSequenceComplete();
	}

}
