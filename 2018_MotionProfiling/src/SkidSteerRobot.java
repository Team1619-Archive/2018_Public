
public class SkidSteerRobot {

	private double width;

	private double acceleration;
	private double deceleration;
	private double maxVelocity;

	private double angularAcceleration;

	public SkidSteerRobot(double width, double acceleration, double deceleration, double maxVelocity,
			double angularAcceleration) {
		this.width = width;

		this.acceleration = acceleration;
		this.deceleration = deceleration;
		this.maxVelocity = maxVelocity;

		this.angularAcceleration = angularAcceleration;
	}

	public double getWidth() {
		return this.width;
	}

	public double getAcceleration() {
		return this.acceleration;
	}

	public double getDeceleration() {
		return this.deceleration;
	}

	public double getMaxVelocity() {
		return this.maxVelocity;
	}
	
	public double getAngularAcceleration() {
		return this.angularAcceleration;
	}

}
