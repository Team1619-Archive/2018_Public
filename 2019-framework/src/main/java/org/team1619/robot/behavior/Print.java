package org.team1619.robot.behavior;

import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.behavior.Behavior;
import org.team1619.utilities.Config;

public class Print implements Behavior {

	private static final Logger sLogger = LoggerFactory.getLogger(Print.class);
	private static final ImmutableSet<String> sSubsystems = ImmutableSet.of("ss_print");

	private final String fPrintString;


	public Print(Config config) {
		fPrintString = config.getString("print_string");
	}

	@Override
	public void initialize() {
		sLogger.debug("Print {} Init", fPrintString);
	}

	@Override
	public void update() {
		sLogger.trace("Print {} update", fPrintString);
	}

	@Override
	public void dispose() {
		sLogger.debug("Print {} Dispose", fPrintString);
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public ImmutableSet<String> getSubsystems() {
		return sSubsystems;
	}

}
