package org.usfirst.frc.team1619.robot.framework;

public class RobotUpdate implements Runnable {

	private RobotMode mode = RobotMode.INITIALIZED;
	private boolean delta = false;
	private long counter = 0;

	private RobotBase robotBase;

	public RobotUpdate(RobotBase robotBase) {
		this.robotBase = robotBase;
	}

	public void setMode(RobotMode mode) {
		if (this.mode == mode) {
			return;
		}

		this.mode = mode;
		this.delta = true;
	}

	@Override
	public void run() {
		if (this.delta) {
			switch (this.mode) {
				case DISABLED:
					this.robotBase.threadDisabledInit();
					break;
				case AUTONOMOUS:
					this.robotBase.threadAutonomousInit();
					break;
				case TELEOP:
					this.robotBase.threadTeleopInit();
					break;
				case TEST:
					this.robotBase.threadTestInit();
					break;
			}

			this.delta = false;
		}

		this.robotBase.threadUpdate(this.mode);
	}

}
