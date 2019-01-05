package org.team1619.models.inputs.numeric;

import org.team1619.utilities.Config;

public abstract class AxisInput extends NumericInput {

	protected final int fPort;
	protected final int fAxis;

	private double fAxisValue = 0.0;
	private double fDelta = 0.0;

	public AxisInput(Object name, Config config) {
		super(name, config);

		fPort = config.getInt("port");
		fAxis = config.getInt("axis");
	}

	@Override
	public void update() {
		double nextAxis = fIsInverted ? -getAxis() : getAxis();
		fDelta = nextAxis - fAxisValue;
		fAxisValue = nextAxis;
	}

	@Override
	public void initialize() {

	}

	@Override
	public double get() {
		return fAxisValue;
	}

	@Override
	public double getDelta() {
		return fDelta;
	}

	protected abstract double getAxis();
}
