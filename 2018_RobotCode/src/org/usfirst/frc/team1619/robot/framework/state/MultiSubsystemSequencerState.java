package org.usfirst.frc.team1619.robot.framework.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Sequencer state using multiple subsystems
 */
public abstract class MultiSubsystemSequencerState extends MultiSubsystemState {

	/**
	 * Indexed list of Wrapper objects
	 */
	private List<Wrapper> sequence = new ArrayList<>();

	/**
	 * Iterator of Wrapper objects
	 */
	private Iterator<Wrapper> sequenceIterator;

	/**
	 * Returns false if this MultiSubsystemSequencerState has not initialized
	 */
	private boolean initialized = false;

	/**
	 * Holds on to the current state wrapper
	 */
	private Wrapper current;

	/**
	 * Creates a MultiSubsystemSequencerState, containing a Set of necessary
	 * subsystemIds
	 * 
	 * @param subsystemIds
	 *            a Set of necessary subsystemIds
	 */
	public MultiSubsystemSequencerState(Set<Integer> subsystemIds) {
		super(subsystemIds);
	}

	/**
	 * Adds a state Wrapper to this sequence
	 * 
	 * @param wrapper
	 *            the wrapper to add to the sequence
	 */
	public void add(Wrapper wrapper) {
		this.sequence.add(wrapper);
	}

	/**
	 * Advances to the next state in this sequence
	 * 
	 * @return false if sequence has been completed
	 */
	private boolean advanceSequence() {
		if (this.current != null) {
			this.current.getState().disposeState();
		}

		if (this.sequenceIterator.hasNext()) {
			this.current = this.sequenceIterator.next();
			this.current.prepareState();
			if (this.current.getState() instanceof MultiSubsystemState) {
				((MultiSubsystemState) this.current.getState()).bypassInitializeState();
			} else {
				this.current.getState().initializeState(-1);
			}
			return true;
		} else {
			this.current = null;
			return false;
		}
	}

	/**
	 * Initializes this MultiSubsystemSequencerState
	 */
	@Override
	protected void initialize() {
		this.initialized = true;

		this.sequenceIterator = this.sequence.iterator();
		this.advanceSequence();
	}

	/**
	 * Updates this current Wrapper
	 */
	@Override
	protected void update() {
		if (this.current != null) {
			if (this.current.isDoneState()) {
				if (!this.advanceSequence()) {
					return;
				}
			}

			if (this.current.getState() instanceof MultiSubsystemState) {
				((MultiSubsystemState) this.current.getState()).bypassUpdateState();
			} else {
				this.current.getState().updateState(-1);
			}
		}
	}

	/**
	 * Disposes this current Wrapper
	 */
	@Override
	protected void dispose() {
		if (this.current != null) {
			this.current.getState().disposeState();
		}
	}

	/**
	 * Checks if this sequence is completed
	 * 
	 * @return true if this sequence is completed
	 */
	public boolean isSequenceComplete() {
		return this.current == null && this.initialized;
	}
}
