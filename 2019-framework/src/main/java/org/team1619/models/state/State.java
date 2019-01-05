package org.team1619.models.state;

import com.google.common.collect.ImmutableSet;

public interface State {
	void initialize();

	void update();

	void dispose();

	boolean isDone();

	ImmutableSet<String> getSubsystems();

	Object getName();
}
