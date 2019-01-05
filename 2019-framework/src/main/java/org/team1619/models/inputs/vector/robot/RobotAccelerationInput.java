package org.team1619.models.inputs.vector.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import org.team1619.models.inputs.vector.AccelerometerInput;
import org.team1619.utilities.Config;

import java.util.Arrays;
import java.util.List;

public class RobotAccelerationInput extends AccelerometerInput {

	private AHRS fNavx;

	public RobotAccelerationInput(Object name, Config config) {
		super(name, config);
		fNavx = new AHRS(SPI.Port.kMXP);
	}

	@Override
	public List<Double> getAcceleration() {
		double xAcceleration = fNavx.getRawAccelX();
		double yAcceleration = fNavx.getRawAccelY();
		double zAcceleration = fNavx.getRawAccelZ();

		return Arrays.asList(xAcceleration, yAcceleration, zAcceleration);
	}
}
