package org.team1619.utilities;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;


public class Odometry {

	@Nullable
	private Vector fPosition;
	private double fLeftEncoder = 0;
	private double fRightEncoder = 0;
	private double fHeading = 0;

	public void initialize(double startLeftEncoder, double startRightEncoder, double startHeading) {
		initialize(Vector.of(0, 0), startLeftEncoder, startRightEncoder, startHeading);
	}

	public void initialize(Vector startPosition, double startLeftEncoder, double startRightEncoder, double startHeading) {
		fPosition = startPosition;

		fLeftEncoder = startLeftEncoder;
		fRightEncoder = startRightEncoder;
		fHeading = startHeading;
	}

	public void update(double leftEncoder, double rightEncoder, double heading) {
		double leftEncoderDelta = leftEncoder - fLeftEncoder;
		double rightEncoderDelta = rightEncoder - fRightEncoder;
		double headingDelta = heading - fHeading;

		if (headingDelta == 0) {
			return;
		}

		double leftR = leftEncoderDelta / headingDelta;
		double rightR = rightEncoderDelta / headingDelta;

		Vector leftVector = Vector.of(-leftR * (1 - Math.cos(headingDelta)), leftR * Math.sin(headingDelta));
		Vector rightVector = Vector.of(-rightR * (1 - Math.cos(headingDelta)), rightR * Math.sin(headingDelta));

		Vector deltaVector = leftVector.add(rightVector).scale(0.5).rotate(fHeading);
		checkNotNull(fPosition);
		fPosition = fPosition.add(deltaVector);

		fLeftEncoder = leftEncoder;
		fRightEncoder = rightEncoder;
		fHeading = heading;
	}

	public Vector getPosition() {
		checkNotNull(fPosition);
		return fPosition;
	}

	public static void main(String[] args) {
		final double PATH_RADIUS = 10.0;
		final double DRIVETRAIN_WIDTH = 2.0;
		final int PIE_SLICES = 99;

		Odometry odometry = new Odometry();
		odometry.initialize(11.0, -16.0, 0.0);

		String[] theoreticalPositions = new String[PIE_SLICES - 1];
		String[] odometryPositions = new String[PIE_SLICES - 1];

		for (int i = 1; i < PIE_SLICES; i++) {
			double heading = i * 2 * Math.PI / PIE_SLICES;
			double leftEncoder = heading * (PATH_RADIUS - DRIVETRAIN_WIDTH / 2.0);
			double rightEncoder = heading * (PATH_RADIUS + DRIVETRAIN_WIDTH / 2.0);

			odometry.update(11.0 + leftEncoder, -16.0 + rightEncoder, 0.0 + heading);

			theoreticalPositions[i - 1] = Vector.of((Math.cos(heading) * PATH_RADIUS) - 10, Math.sin(heading) * PATH_RADIUS).toString();
			odometryPositions[i - 1] = odometry.getPosition().toString();
		}

		System.out.println(String.join(",", theoreticalPositions));
		System.out.println(String.join(",", odometryPositions));
	}
}

