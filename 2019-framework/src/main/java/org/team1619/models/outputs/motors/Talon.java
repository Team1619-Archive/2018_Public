package org.team1619.models.outputs.motors;

import org.team1619.utilities.Config;

public abstract class Talon implements Motor {

	protected final Object fName;
	protected final int fDeviceNumber;
	protected final boolean fIsBrakeModeEnabled;
	protected final boolean fIsInverted;

	protected final boolean fIsCurrentLimitEnabled;
	protected final int fContinuousCurrentLimitAmps;
	protected final int fPeakCurrentLimitAmps;
	protected final int fPeakCurrentDurationMilliseconds;

	public Talon(Object name, Config config) {
		fName = name;

		fDeviceNumber = config.getInt("device_number");
		fIsBrakeModeEnabled = config.getBoolean("brake_mode_enabled", true);
		fIsInverted = config.getBoolean("inverted", false);

		fIsCurrentLimitEnabled = config.getBoolean("current_limit_enabled", false);
		fContinuousCurrentLimitAmps = config.getInt("continuous_current_limit_amps", 0);
		fPeakCurrentLimitAmps = config.getInt("peak_current_limit_amps", 0);
		fPeakCurrentDurationMilliseconds = config.getInt("peak_current_duration_milliseconds", 0);
	}


	public int getDeviceNumber() {
		return fDeviceNumber;
	}
}
