package org.team1619.models.inputs.vector;

import org.team1619.utilities.Config;

import java.util.List;

public abstract class VectorInput {

	protected final Object fName;

	public VectorInput(Object name, Config config) {
		fName = name;
	}

	public abstract void initialize();

	public abstract void update();

	public abstract List<Double> get();

	public Object getName() {
		return fName;
	}
}
