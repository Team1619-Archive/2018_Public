package org.team1619.models.outputs.motors.robot;

import edu.wpi.first.wpilibj.Servo;
import org.team1619.utilities.Config;

import java.util.HashMap;
import java.util.Map;

public class RobotServo extends org.team1619.models.outputs.motors.Servo {

	private final Servo fServo;

	public RobotServo(Object name, Config config) {
		super(name, config);
		fServo = new Servo(fChannel);
	}

	@Override
	public void setHardware(OutputType outputType, double outputValue) {
		if(outputValue <0) {
			fServo.setDisabled();
		} else {
			fServo.set(outputValue);
		}
	}

	public double getSetpoint(){
		return fServo.getPosition();
	}

	@Override
	public Map<Integer, Double> getMotorCurrentValues(){
		Map<Integer, Double> motorCurrentValues = new HashMap<>();
		motorCurrentValues.put(fChannel, -1.0);
		return motorCurrentValues;
	}
}
