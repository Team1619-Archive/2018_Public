package org.usfirst.frc.team1619.robot.framework.state;

public abstract class SequencerStateWrapper<T extends SequencerState> extends Wrapper<SequencerState> {

	/**
	 * Returns true if this MultiSubsystemSequencerState is complete
	 */
	@Override
	public boolean isDone() {
		return this.getState().isSequenceComplete();
	}

}
