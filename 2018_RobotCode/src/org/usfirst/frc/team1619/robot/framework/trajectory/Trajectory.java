package org.usfirst.frc.team1619.robot.framework.trajectory;

public interface Trajectory {

	double distance(double time);
	
	double velocity(double time);
	
	double acceleration(double time);
	
	double heading(double time);
	
	boolean isDone(double time);

}
