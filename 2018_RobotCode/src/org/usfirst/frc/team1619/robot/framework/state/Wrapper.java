package org.usfirst.frc.team1619.robot.framework.state;

import org.usfirst.frc.team1619.robot.framework.IO.In;
import org.usfirst.frc.team1619.robot.framework.IO.Out;


public abstract class Wrapper<T extends State> {

	protected static final In in = In.getInstance();
	protected static final Out out = Out.getInstance();

	private T state;

	public void prepareState() {
		this.state = this.createState();
	}

	public T getState() {
		return this.state;
	}

	protected abstract T createState();


	protected abstract boolean isReady();

	public boolean isReadyState() {
		return this.isReady();
	}

	protected abstract boolean isDone();

	public boolean isDoneState() {
		return this.isDone();
	}


	public abstract boolean isSubsystemValid(int subsystemId);


}
