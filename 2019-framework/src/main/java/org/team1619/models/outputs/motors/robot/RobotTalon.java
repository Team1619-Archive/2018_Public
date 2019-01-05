package org.team1619.models.outputs.motors.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team1619.models.outputs.motors.Talon;
import org.team1619.utilities.Config;

import java.util.HashMap;
import java.util.Map;

public class RobotTalon extends Talon {

	private static final int CAN_TIMEOUT_MILLISECONDS = 10;

	private final TalonSRX fMotor;
	private double maxCurrent;

	public RobotTalon(Object name, Config config) {
		super(name, config);
		fMotor = new TalonSRX(fDeviceNumber);

		fMotor.setNeutralMode(fIsBrakeModeEnabled ? NeutralMode.Brake : NeutralMode.Coast);
		fMotor.enableCurrentLimit(fIsCurrentLimitEnabled);

		fMotor.configContinuousCurrentLimit(fContinuousCurrentLimitAmps, CAN_TIMEOUT_MILLISECONDS);
		fMotor.configPeakCurrentLimit(fPeakCurrentLimitAmps, CAN_TIMEOUT_MILLISECONDS);
		fMotor.configPeakCurrentDuration(fPeakCurrentDurationMilliseconds, CAN_TIMEOUT_MILLISECONDS);

		maxCurrent = 0.0;
	}

	@Override
	public void setHardware(OutputType outputType, double outputValue) {
		double adjustedOutput = fIsInverted ? -outputValue : outputValue;
		switch (outputType) {
			case PERCENT:
				fMotor.set(ControlMode.PercentOutput, adjustedOutput);
				break;
			case PID:
				fMotor.set(ControlMode.PercentOutput, 0.0);
				break;
			case SLAVE:
				fMotor.set(ControlMode.Follower, outputValue);
				break;
		}
	}

	@Override
	public Map<Integer, Double> getMotorCurrentValues(){
		Map<Integer, Double> motorCurrentValues = new HashMap<>();
		double motorCurrent = fMotor.getOutputCurrent();
		if(motorCurrent > maxCurrent){
			maxCurrent = motorCurrent;
		}
		motorCurrentValues.put(fDeviceNumber, motorCurrent);
		SmartDashboard.putNumber("Motor ID " + fDeviceNumber + " - Current = ", motorCurrent);
		SmartDashboard.putNumber("Motor ID " + fDeviceNumber + " - Max Current = ", maxCurrent);
		return motorCurrentValues;
	}
}
