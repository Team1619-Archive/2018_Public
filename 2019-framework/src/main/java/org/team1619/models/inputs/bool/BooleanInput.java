package org.team1619.models.inputs.bool;

import org.team1619.utilities.Config;

public abstract class BooleanInput {

	public enum DeltaType {
		RISING_EDGE,
		FALLING_EDGE,
		NO_DELTA
	}

	protected final Object fName;
	protected final boolean fIsInverted;

	public BooleanInput(Object name, Config config) {
		fName = name;
		fIsInverted = config.getBoolean("inverted", false);
	}

	public abstract void initialize();

	public abstract void update();

	public abstract boolean get();

	public abstract DeltaType getDelta();
}
