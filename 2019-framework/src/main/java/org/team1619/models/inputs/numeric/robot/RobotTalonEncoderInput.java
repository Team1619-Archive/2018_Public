package org.team1619.models.inputs.numeric.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team1619.models.inputs.numeric.TalonEncoderInput;
import org.team1619.utilities.Config;

public class RobotTalonEncoderInput extends TalonEncoderInput {

	private final TalonSRX fMotor;

	public RobotTalonEncoderInput(Object name, Config config) {
		super(name, config);
		fMotor = new TalonSRX(fDeviceNumber);
		com.ctre.phoenix.motorcontrol.FeedbackDevice feedbackDevice;

		switch (fFeedbackDevice) {
			case QUAD_ENCODER:
				feedbackDevice = com.ctre.phoenix.motorcontrol.FeedbackDevice.QuadEncoder;
				break;
			default:
				feedbackDevice = com.ctre.phoenix.motorcontrol.FeedbackDevice.CTRE_MagEncoder_Absolute;
				break;
		}

		fMotor.configSelectedFeedbackSensor(feedbackDevice, 0, 10);
	}

	@Override
	public double getSensorPosition() {
		return fMotor.getSelectedSensorPosition(0) / fCountsPerUnit;
	}
}
