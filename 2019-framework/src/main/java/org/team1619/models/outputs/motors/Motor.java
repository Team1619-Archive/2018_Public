package org.team1619.models.outputs.motors;

import java.util.Map;

public interface Motor {

	enum OutputType {
		PERCENT,
		PID,
		SLAVE,
		SERVO
	}

	void setHardware(OutputType outputType, double outputValue);

	Map<Integer, Double> getMotorCurrentValues();
}
