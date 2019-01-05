package org.team1619.models.inputs.vector.sim;

import org.team1619.models.inputs.vector.AccelerometerInput;
import org.team1619.utilities.Config;
import org.team1619.shared.abstractions.EventBus;

import java.util.List;

public class SimAccelerationInput extends AccelerometerInput {

	private SimVectorInputListener fListener;

	public SimAccelerationInput(EventBus eventBus, Object name, Config config) {
		super(name, config);

		fListener = new SimVectorInputListener(eventBus, config);
	}

	@Override
	public List<Double> getAcceleration() {
		return fListener.get();
	}
}
