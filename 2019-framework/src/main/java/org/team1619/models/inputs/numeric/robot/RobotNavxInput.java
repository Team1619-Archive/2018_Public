package org.team1619.models.inputs.numeric.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.inputs.numeric.NavxInput;
import org.team1619.utilities.Config;

public class RobotNavxInput extends NavxInput {

	private static final Logger sLogger = LoggerFactory.getLogger(RobotNavxInput.class);

	private AHRS fNavx;

	public RobotNavxInput(Object name, Config config) {
		super(name, config);

		fNavx = new AHRS(SPI.Port.kMXP);
		fNavx.zeroYaw();
	}

	@Override
	protected double getHeadingDegrees() {
		return fNavx.getFusedHeading();
	}
}
