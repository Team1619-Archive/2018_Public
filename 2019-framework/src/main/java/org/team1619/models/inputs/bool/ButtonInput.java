package org.team1619.models.inputs.bool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.utilities.Config;

public abstract class ButtonInput extends BooleanInput {

	private static final Logger sLogger = LoggerFactory.getLogger(ButtonInput.class);

	protected final int fPort;
	protected final String fButton;

	private boolean fIsPressed = false;
	private DeltaType fDelta = DeltaType.NO_DELTA;

	public ButtonInput(Object name, Config config) {
		super(name, config);

		fPort = config.getInt("port");
		fButton = config.getString("button");
	}

	@Override
	public void update() {
		boolean nextIsPressed = fIsInverted ? !isPressed() : isPressed();

		if (nextIsPressed && !fIsPressed) {
			fDelta = DeltaType.RISING_EDGE;
		} else if (!nextIsPressed && fIsPressed) {
			fDelta = DeltaType.FALLING_EDGE;
		} else {
			fDelta = DeltaType.NO_DELTA;
		}

		fIsPressed = nextIsPressed;
	}

	@Override
	public void initialize() {

	}

	@Override
	public boolean get() {
		return fIsPressed;
	}

	@Override
	public DeltaType getDelta() {
		return fDelta;
	}

	public abstract boolean isPressed();
}
