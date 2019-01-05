package org.team1619.models.inputs.numeric.sim;

import org.team1619.models.inputs.numeric.TalonEncoderInput;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.EventBus;

public class SimTalonEncoderInput extends TalonEncoderInput {

	private final SimNumericInputListener fListener;

	public SimTalonEncoderInput(EventBus eventBus, Object name, Config config) {
		super(name, config);

		fListener = new SimNumericInputListener(eventBus, name);
	}

	@Override
	public double getSensorPosition() {
		return fListener.get();
	}

	@Override
	public void update() {

	}
}
