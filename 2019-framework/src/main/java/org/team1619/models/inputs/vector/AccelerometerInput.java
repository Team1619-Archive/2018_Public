package org.team1619.models.inputs.vector;

import org.team1619.utilities.Config;

import java.util.ArrayList;
import java.util.List;

public abstract class AccelerometerInput extends VectorInput {

	private List<Double> fAcceleration = new ArrayList<>();

	public AccelerometerInput(Object name, Config config) {
		super(name, config);
	}

	@Override
	public void update() {
		fAcceleration = getAcceleration();
	}

	@Override
	public void initialize() {

	}

	@Override
	public List<Double> get() {
		return fAcceleration;
	}

	public abstract List<Double> getAcceleration();
}
