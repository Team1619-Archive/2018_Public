package org.team1619.models.outputs.solenoids.sim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.outputs.solenoids.SolenoidNitra;
import org.team1619.utilities.Config;

public class SimSolenoid extends SolenoidNitra {
    private static final Logger sLogger = LoggerFactory.getLogger(SimSolenoid.class);
    private boolean fOutput = false;

    public SimSolenoid(Object name, Config config) {
        super(name, config);
    }
    @Override
    public void setHardware(boolean output) {
        fOutput = output;
        sLogger.trace("{}", output);
    }
}
