package org.team1619.shared.abstractions;

import java.util.List;

public interface InputValues {
	boolean getBoolean(String name);

	double getNumeric(String name);

	List<Double> getVector(String name);

	void setBoolean(String name, boolean value);

	void setNumeric(String name, double value);

	void setVector(String name, List<Double> values);
}
