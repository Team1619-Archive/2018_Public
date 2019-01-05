package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends IterativeRobot {

    TalonSRX driveLeft;
    TalonSRX driveLeftSlave;
    TalonSRX driveRight;
    TalonSRX driveRightSlave;
    XboxController controller;
    AHRS navx;

    private static final double countsPerInch = 24.53125;
    private double leftEncoder = 0;
    private double rightEncoder = 0;
    private double heading = 0;
    private Vector position;


    @Override
    public void robotInit() {
        driveLeft = new TalonSRX(1);
        driveLeftSlave = new TalonSRX(0);
        driveRight = new TalonSRX(14);
        driveRightSlave = new TalonSRX(15);

        controller = new XboxController(0);

        navx = new AHRS(SPI.Port.kMXP);
    }

    @Override
    public void teleopInit() {
        driveRight.setInverted(false);
        driveRightSlave.setInverted(false);
        driveLeft.setInverted(true);
        driveLeftSlave.setInverted(true);

        driveLeftSlave.set(ControlMode.Follower, 1);
        driveRightSlave.set(ControlMode.Follower, 14);

        driveLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        driveRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);

        leftEncoder = -driveLeft.getSelectedSensorPosition(0) / countsPerInch;
        rightEncoder = -driveRight.getSelectedSensorPosition(0) / countsPerInch;
        heading = 0;
        position = Vector.of(0, 0);

        navx.zeroYaw();
    }

    @Override
    public void teleopPeriodic() {
        double y = controller.getRawAxis(1);
        double z = -controller.getRawAxis(4);
        double leftPower = y + z;
        double rightPower = y - z;
        driveLeft.set(ControlMode.PercentOutput, leftPower);
        driveRight.set(ControlMode.PercentOutput, rightPower);

        double nextLeftEncoder = -driveLeft.getSelectedSensorPosition(0) / countsPerInch;
        double nextRightEncoder = -driveRight.getSelectedSensorPosition(0) / countsPerInch;
        double leftEncoderDelta = nextLeftEncoder - leftEncoder;
        double rightEncoderDelta = nextRightEncoder - rightEncoder;

        double nextHeading = (navx.getFusedHeading() * Math.PI) / 180;
        double headingDelta = nextHeading - heading;

        if (Math.abs(headingDelta) < 1E-10) {
            heading = nextHeading;
            leftEncoder = nextLeftEncoder;
            rightEncoder = nextRightEncoder;
        } else {
            double leftR = leftEncoderDelta / headingDelta;
            double rightR = rightEncoderDelta / headingDelta;

            Vector leftVector = Vector.of(leftR * Math.cos(headingDelta) - 1, leftR * Math.sin(headingDelta));
            Vector rightVector = Vector.of(rightR * Math.cos(headingDelta) - 1, rightR * Math.sin(headingDelta));

            Vector deltaVector = leftVector.add(rightVector).scale(0.5);

            position = position.add(deltaVector.rotate(nextHeading));

            heading = nextHeading;
            leftEncoder = nextLeftEncoder;
            rightEncoder = nextRightEncoder;
        }

        if (controller.getXButton()) {
            System.out.printf("(%.3f, %.3f)\n", position.x, position.y);
        }

    /*if(joystick.getRawButton(1)){
      driveLeft.set(ControlMode.PercentOutput,0.75);
    }
    else{
      driveLeft.set(ControlMode.PercentOutput,0);
    }

    if(joystick.getRawButton(2)){
      driveRight.set(ControlMode.PercentOutput,0.75);
    }
    else{
      driveRight.set(ControlMode.PercentOutput,0);
    }*/
    }
}
