package org.usfirst.frc.team1619.robot.framework.IO.sensor;

import edu.wpi.first.wpilibj.DigitalInput;

public class DigitalInputSensor extends BooleanSensor {

	private DigitalInput sensor;
	private boolean inverted;

	public DigitalInputSensor(int id, boolean inverted) {
		this.sensor = new DigitalInput(id);
		this.inverted = inverted;
	}

	@Override
	protected boolean getValue() {
		return this.inverted ? !this.sensor.get() : this.sensor.get();
	}

}
