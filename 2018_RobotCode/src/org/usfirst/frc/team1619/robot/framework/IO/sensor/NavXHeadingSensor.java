package org.usfirst.frc.team1619.robot.framework.IO.sensor;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

public class NavXHeadingSensor extends NumericSensor {

	private AHRS navx;

	public NavXHeadingSensor() {
		super(false);
		this.navx = new AHRS(SPI.Port.kMXP);
	}

	public void zero() {
		this.navx.zeroYaw();
	}

	@Override
	protected double getValue() {
		return 360.0 - this.navx.getFusedHeading();

	}

}
