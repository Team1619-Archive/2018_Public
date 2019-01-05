package org.team1619.models.outputs.solenoids.robot;
import org.team1619.models.outputs.solenoids.SolenoidNitra;
import org.team1619.utilities.Config;


public class RobotSolenoid extends SolenoidNitra {

    private final edu.wpi.first.wpilibj.Solenoid fWpiSolenoid;

    public RobotSolenoid(Object name, Config config) {
        super(name, config);
        fWpiSolenoid = new edu.wpi.first.wpilibj.Solenoid(fDeviceNumber);
    }


    @Override
    public void setHardware(boolean output) {
        fWpiSolenoid.set(output);
    }

}