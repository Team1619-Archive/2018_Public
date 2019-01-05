package org.team1619.models.outputs.solenoids;

import org.team1619.utilities.Config;

public abstract class SolenoidNitra implements Solenoid {

    protected final Object fName;
    protected final int fDeviceNumber;

    public SolenoidNitra(Object name, Config config) {
        fName = name;
        fDeviceNumber = config.getInt("device_number");
    }
    public int getDeviceNumber() {
        return fDeviceNumber;
    }

}
