package org.team1619.shared.concretions;

import com.google.common.eventbus.AsyncEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.shared.abstractions.EventBus;

import javax.inject.Singleton;
import java.util.concurrent.Executors;

@Singleton
public class SharedEventBus implements EventBus {

	private static final Logger sLogger = LoggerFactory.getLogger(SharedEventBus.class);

	private com.google.common.eventbus.EventBus fEventBus;

	public SharedEventBus() {
		fEventBus = new AsyncEventBus(Executors.newFixedThreadPool(4));
	}

	@Override
	public void register(Object object) {
		sLogger.info("Registering object '{}'", object);

		fEventBus.register(object);
	}

	@Override
	public void post(Object object) {
		sLogger.debug("Posting object '{}'", object);

		fEventBus.post(object);
	}

	@Override
	public void unregister(Object object) {
		sLogger.info("Unregistering object '{}'", object);

		fEventBus.unregister(object);
	}


}
