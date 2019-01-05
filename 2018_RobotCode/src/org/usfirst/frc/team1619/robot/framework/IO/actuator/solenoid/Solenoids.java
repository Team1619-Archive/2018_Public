package org.usfirst.frc.team1619.robot.framework.IO.actuator.solenoid;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1619.robot.framework.util.Timer;

import edu.wpi.first.wpilibj.Compressor;

public class Solenoids {

	private class SingleSolenoidWrapper implements Solenoid {

		public edu.wpi.first.wpilibj.Solenoid solenoid;
		private boolean output = false;

		public SingleSolenoidWrapper(int id) {
			this.solenoid = new edu.wpi.first.wpilibj.Solenoid(id);
		}

		public void set(boolean output) {
			this.output = output;
		}

		public void flush() {
			this.solenoid.set(this.output);
		}

		public void reset() {
			this.output = false;
		}

	}

	private class DualSolenoidWrapper implements Solenoid {

		public edu.wpi.first.wpilibj.Solenoid onSolenoid, offSolenoid;
		private boolean first = true;
		private boolean output = false;
		private boolean delta = false;
		private Timer energizingTimer = new Timer();

		public DualSolenoidWrapper(int onID, int offID) {
			this.onSolenoid = new edu.wpi.first.wpilibj.Solenoid(onID);
			this.offSolenoid = new edu.wpi.first.wpilibj.Solenoid(offID);
		}

		public void set(boolean output) {
			if (!this.delta && output != this.output || this.first) {
				this.first = false;
				this.delta = true;
				this.output = output;
				this.energizingTimer.start(1000);
			}
		}

		public void flush() {
			if (this.delta) {
				(this.output ? this.offSolenoid : this.onSolenoid).set(false);
				this.delta = false;
			}

			if (this.energizingTimer.isStarted()) {
				edu.wpi.first.wpilibj.Solenoid solenoid = this.output ? this.onSolenoid : this.offSolenoid;
				if (this.energizingTimer.isDone()) {
					solenoid.set(false);
					this.energizingTimer.reset();
				} else {
					solenoid.set(true);
				}
			}
		}

		public void reset() {
			this.first = true;
			this.output = false;
			this.delta = false;
			this.energizingTimer.reset();
		}

	}

	private List<Solenoid> solenoids = new ArrayList<>();
	private Compressor compressor;

	public Solenoids() {
	}

	public int registerSingle(int id) {
		this.solenoids.add(new SingleSolenoidWrapper(id));
		return this.solenoids.size() - 1;
	}

	public int registerDual(int onID, int offID) {
		this.solenoids.add(new DualSolenoidWrapper(onID, offID));
		return this.solenoids.size() - 1;
	}

	public Solenoid get(int key) {
		assert key > -1 && key < this.solenoids.size() : "Invalid key";
		return this.solenoids.get(key);
	}

	public void update() {
		for (Solenoid solenoid : this.solenoids) {
			solenoid.flush();
		}
	}

	public void disable() {
		for (Solenoid solenoid : this.solenoids) {
			solenoid.reset();
		}
	}

	public void disableCompressor() {
		this.compressor.setClosedLoopControl(false);
		this.compressor.stop();
	}

	public void enableCompressor() {
		this.compressor = new Compressor(0);
		this.compressor.setClosedLoopControl(true);
	}

}
