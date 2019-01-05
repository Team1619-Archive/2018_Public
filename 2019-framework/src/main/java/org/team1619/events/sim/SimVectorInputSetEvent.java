package org.team1619.events.sim;

import java.util.List;

public class SimVectorInputSetEvent {

	public final String name;
	public final List<Double> values;

	public SimVectorInputSetEvent(String name, List<Double> values) {
		this.name = name;
		this.values = values;
	}
}
