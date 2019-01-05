package org.team1619.models.behavior;

import com.google.common.collect.ImmutableSet;

public interface Behavior {
	void initialize();

	void update();

	void dispose();

	boolean isDone();

	ImmutableSet<String> getSubsystems();
}
