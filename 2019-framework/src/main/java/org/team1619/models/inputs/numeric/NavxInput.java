package org.team1619.models.inputs.numeric;

import org.team1619.utilities.Config;

public abstract class NavxInput extends NumericInput {

	protected double fHeadingRadians = 0.0;
	private double fDelta = 0.0;

	public NavxInput(Object name, Config config) {
		super(name, config);
	}

	@Override
	public void update() {
		double nextHeadingRadians = getHeadingDegrees() * Math.PI / 180.0;
		nextHeadingRadians = fIsInverted ? 2 * Math.PI - nextHeadingRadians : nextHeadingRadians;
		fDelta = nextHeadingRadians - fHeadingRadians;
		fHeadingRadians = nextHeadingRadians;
	}

	@Override
	public void initialize() {

	}

	@Override
	public double get() {
		return fHeadingRadians;
	}

	@Override
	public double getDelta() {
		return fDelta;
	}

	protected abstract double getHeadingDegrees();
}
