package org.usfirst.frc.team1619.robot.framework;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.usfirst.frc.team1619.robot.IO;
import org.usfirst.frc.team1619.robot.RobotState;
import org.usfirst.frc.team1619.robot.Subsystems;
import org.usfirst.frc.team1619.robot.framework.IO.In;
import org.usfirst.frc.team1619.robot.framework.IO.Out;
import org.usfirst.frc.team1619.robot.framework.state.Subsystem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class RobotBase extends IterativeRobot {

	private static final In in = In.getInstance();
	private static final Out out = Out.getInstance();
	private static final FrameData frameData = FrameData.getInstance();

	private RobotUpdate robotUpdate = new RobotUpdate(this);
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Override
	public void robotInit() {
		Subsystems.initialize();
		IO.initialize();

		// out.solenoids.enableCompressor();

		scheduler.scheduleAtFixedRate(this.robotUpdate, 8, 8, TimeUnit.MILLISECONDS);
	}

	@Override
	public void disabledInit() {
		this.robotUpdate.setMode(RobotMode.DISABLED);
	}

	@Override
	public void autonomousInit() {
		Subsystem.resetAll();
		this.robotUpdate.setMode(RobotMode.AUTONOMOUS);
	}

	@Override
	public void teleopInit() {
		Subsystem.resetAll();
		this.robotUpdate.setMode(RobotMode.TELEOP);
	}

	public void threadDisabledInit() {
		Subsystem.resetAll();
		out.disable();
	}

	public void threadAutonomousInit() {
		this.robotUpdate.setMode(RobotMode.AUTONOMOUS);
	}

	public void threadTeleopInit() {
		this.robotUpdate.setMode(RobotMode.TELEOP);
	}

	public void threadTestInit() {
	}

	public void threadUpdate(RobotMode mode) {
		in.update();
		RobotState.update();
		frameData.reset();

		if (mode == RobotMode.DISABLED) {
			RobotState.updateAuto();
		} else {
			Subsystem.updateAll();
			out.update();
		}
	}

}