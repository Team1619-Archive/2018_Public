package org.team1619.shared.concretions;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.shared.abstractions.InputValues;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
public class SharedInputValues implements InputValues {

	private static final Logger sLogger = LoggerFactory.getLogger(SharedInputValues.class);

	private Map<String, Boolean> fBooleanInputs = new ConcurrentHashMap<>();
	private Map<String, Double> fNumericInputs = new ConcurrentHashMap<>();
	private Map<String, List<Double>> fVectorInputs = new ConcurrentHashMap<>();

	public boolean getBoolean(String name) {
		return fBooleanInputs.getOrDefault(name, false);
	}

	public double getNumeric(String name) {
		return fNumericInputs.getOrDefault(name, 0.0);
	}

	public List<Double> getVector(String name) {
		return fVectorInputs.getOrDefault(name, Arrays.asList(0.0, 0.0));
	}

	public void setBoolean(String name, boolean value) {
		sLogger.debug("Setting boolean input '{}' to {}", name, value);

		fBooleanInputs.put(name, value);
	}

	public void setNumeric(String name, double value) {
		sLogger.debug("Setting numeric input '{}' to {}", name, value);

		fNumericInputs.put(name, value);
	}

	public void setVector(String name, List<Double> values) {
		sLogger.debug("Setting vector input '{}' to {}", name, values.stream().map(number -> String.format("%.4f", number)).collect(Collectors.joining(",")));

		fVectorInputs.put(name, values);
	}
}
