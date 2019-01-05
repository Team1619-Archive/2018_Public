package org.team1619.models.inputs.numeric.sim;

import org.team1619.models.inputs.numeric.NavxInput;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.EventBus;

public class SimNavxInput extends NavxInput {

	private SimNumericInputListener fListener;

	public SimNavxInput(EventBus eventBus, Object name, Config config) {
		super(name, config);

		fListener = new SimNumericInputListener(eventBus, name);
	}

	@Override
	protected double getHeadingDegrees() {
		return fListener.get();
	}
}
