package org.team1619.models.inputs.bool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.utilities.Config;

public abstract class DigitalInput extends BooleanInput {

	protected int fId;
	private boolean fPreviousValue;
	private boolean fValue;

	private static final Logger sLogger = LoggerFactory.getLogger(DigitalInput.class);

	public DigitalInput(Object name, Config config) {
		super(name, config);

		fId = config.getInt("id");
	}

	public abstract boolean getDigitalInputValue();

	@Override
	public void initialize() {
		fValue = getDigitalInputValue();
	}

	@Override
	public void update() {
		fPreviousValue = fValue;
		fValue = getDigitalInputValue();
	}

	@Override
	public boolean get() {
		return fIsInverted ? !fValue : fValue;
	}

	@Override
	public DeltaType getDelta() {
		if (!fPreviousValue == fValue) {
			return DeltaType.RISING_EDGE;
		}
		else if (fPreviousValue == !fValue) {
			return DeltaType.FALLING_EDGE;
		}
		else {
			return DeltaType.NO_DELTA;
		}
	}
}
