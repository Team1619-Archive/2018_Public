package org.usfirst.frc.team1619.robot.framework.state;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Subsystem {

	private static Set<Subsystem> subsystems = new HashSet<>();

	public static void updateAll() {
		for (Subsystem subsystem : subsystems) {
			subsystem.update();
		}
	}

	public static void resetAll() {
		for (Subsystem subsystem : subsystems) {
			subsystem.reset();
		}
	}

	private static int subsystemCount = 0;

	private int id;

	private List<Wrapper> wrappers = new ArrayList<>();

	private Wrapper active = null;

	public Subsystem() {
		subsystems.add(this);

		this.id = ++subsystemCount;
	}

	public int getId() {
		return this.id;
	}

	public void addWrapper(Wrapper wrapper) {
		if (!wrapper.isSubsystemValid(this.id)) {
			throw new RuntimeException(
					"Invalid state " + wrapper.getClass().getCanonicalName() + " added to subsystem " + this.id);
		}

		this.wrappers.add(wrapper);
	}

	public void reset() {

		if (this.active != null) {
			this.active.getState().disposeState();
		}

		this.wrappers = new ArrayList<>();
		this.active = null;
	}

	public void update() {
		for (int i = 0; i < wrappers.size(); i++) {
			Wrapper wrapper = this.wrappers.get(i);
			if (wrapper == this.active) {
				if (this.active.isDoneState()) {
					if (this.active.getState() != null) {
						this.active.getState().disposeState();
					}

					this.active = null;
				} else {
					break;
				}
			} else if (wrapper.isReadyState()) {
				if (this.active != null) {

					if (this.active.getState() != null) {
						this.active.getState().disposeState();
					}
				}

				this.active = wrapper;
				this.active.prepareState();
				this.active.getState().initializeState(this.id);

				break;
			}
		}

		if (this.active != null) {
			this.active.getState().updateState(this.id);

		}
	}
}
