package com.time.bomb.background;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.common.eventbus.EventBus;
import com.time.bomb.cache.TimeBombCacheImpl;
import com.time.bomb.event.ClearCacheEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RandomCacheClear implements Runnable{
	
	private final Map<Integer, RandomEventCommand> CLEAR_CACHE;
	
	public RandomCacheClear(EventBus eventBus) {

		final Map<Integer, RandomEventCommand> events = new HashMap<>();
		
		events.put(1, ()->{
			log.info("EVENT : RandomCacheClear : clear cache via event");
			eventBus.post(new ClearCacheEvent("Clear Cache"));
		});
		
		events.put(2, ()->{
			log.info("EVENT : RandomCacheClear : clear cache via cache clear method");
			TimeBombCacheImpl.getInstance().clear();
		});
		
		CLEAR_CACHE = Collections.unmodifiableMap(events);
	}

	@Override
	public void run() {

	    Random rand = new Random();	
	    CLEAR_CACHE.get(rand.nextInt(CLEAR_CACHE.size()) + 1).execute();
	
	}
}
