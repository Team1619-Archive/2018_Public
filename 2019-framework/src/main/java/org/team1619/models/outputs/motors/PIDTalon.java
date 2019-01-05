package org.team1619.models.outputs.motors;

import org.team1619.utilities.Config;
import org.team1619.utilities.PID;
import org.team1619.shared.abstractions.InputValues;
import org.team1619.shared.abstractions.ModelFactory;
import org.team1619.utilities.YamlConfigParser;

import java.util.HashMap;
import java.util.Map;

public abstract class PIDTalon implements Motor {

	protected Object fName;
	protected InputValues fSharedInputValues;
	protected Map<String, PID> fPID = new HashMap<>();

	public PIDTalon(Object name, Config config, YamlConfigParser parser, InputValues inputValues, ModelFactory modelFactory) {
		fName = name;
		fSharedInputValues = inputValues;

		double p = config.getDouble("p");
		double i = config.getDouble("i");
		double d = config.getDouble("d");

	}

}
