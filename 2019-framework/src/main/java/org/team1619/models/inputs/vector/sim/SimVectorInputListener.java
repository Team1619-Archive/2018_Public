package org.team1619.models.inputs.vector.sim;

import com.google.common.eventbus.Subscribe;
import org.team1619.events.sim.SimVectorInputSetEvent;
import org.team1619.shared.abstractions.EventBus;

import java.util.Arrays;
import java.util.List;

public class SimVectorInputListener {

	private final Object fName;

	private List<Double> fValues = Arrays.asList(0.0, 0.0, 0.0);

	public SimVectorInputListener(EventBus eventBus, Object name) {
		eventBus.register(this);
		fName = name;
	}

	public List<Double> get() {
		return fValues;
	}

	@Subscribe
	public void onNumericInputSet(SimVectorInputSetEvent event) {
		if (event.name.equals(fName)) {
			fValues = event.values;
		}
	}
}
