package org.team1619.models.inputs.numeric;

import org.team1619.utilities.Config;

public abstract class TalonEncoderInput extends NumericInput {

	public enum FeedbackDevice {
		QUAD_ENCODER
	}

	protected final int fDeviceNumber;
	protected final FeedbackDevice fFeedbackDevice;
	protected final double fCountsPerUnit;
	protected final String fUnit;

	private double fSensorPosition;
	private double fDelta;

	public TalonEncoderInput(Object name, Config config) {
		super(name, config);
		fDeviceNumber = config.getInt("device_number");
		fFeedbackDevice = config.getEnum("feedback_device", FeedbackDevice.class);
		fCountsPerUnit = config.getDouble("counts_per_unit");
		fUnit = config.getString("unit");
	}

	@Override
	public void update() {
		double nextSensorPosition = fIsInverted ? -getSensorPosition() : getSensorPosition();
		fDelta = nextSensorPosition - fSensorPosition;
		fSensorPosition = nextSensorPosition;
	}

	@Override
	public void initialize() {

	}

	@Override
	public double get() {
		return fSensorPosition;
	}

	@Override
	public double getDelta() {
		return fDelta;
	}

	public abstract double getSensorPosition();
}
