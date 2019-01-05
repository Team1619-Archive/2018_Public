package org.team1619.models.inputs.numeric;

import org.team1619.utilities.Config;

public abstract class NumericInput {

	protected final Object fName;
	protected final boolean fIsInverted;

	public NumericInput(Object name, Config config) {
		fName = name;
		fIsInverted = config.getBoolean("inverted", false);
	}

	public abstract void initialize();

	public abstract void update();

	public abstract double get();

	public abstract double getDelta();

}
