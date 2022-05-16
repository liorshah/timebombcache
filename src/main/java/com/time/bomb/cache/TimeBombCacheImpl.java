package com.time.bomb.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.eventbus.EventBus;
import com.time.bomb.event.EventListener;
import com.time.bomb.util.FetchResouceCommandManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeBombCacheImpl implements TimeBombCache<String, Object>{

	private static final int DEFAULT_TIME_BOMB_CACHE_EXPIRE_TIME = 60;
	private static final int DEFAULT_TIME_BOMB_MAXIMUM_SIZE = 10;
	
	private final LoadingCache<String, Object> cache;
	private final EventListener<String, Object> eventListener;
	private final EventBus eventBus;
	
	private static TimeBombCacheImpl bombCache = new TimeBombCacheImpl();
	
	public TimeBombCacheImpl() {
		cache = CacheBuilder.newBuilder()
				   .maximumSize(System.getenv("TIME_BOMB_MAXIMUM_SIZE") != null ? Integer.valueOf(System.getenv("TIME_BOMB_MAXIMUM_SIZE")): DEFAULT_TIME_BOMB_MAXIMUM_SIZE)
				   .expireAfterWrite(System.getenv("TIME_BOMB_CACHE_EXPIRE_TIME") != null ? Integer.valueOf(System.getenv("TIME_BOMB_CACHE_EXPIRE_TIME")): DEFAULT_TIME_BOMB_CACHE_EXPIRE_TIME, TimeUnit.SECONDS)
				   .removalListener(new RemovalListener<String, Object>() {

						@Override
						public void onRemoval(RemovalNotification<String, Object> notification) {
							if (notification.wasEvicted()) {
				                String cause = notification.getCause().name();
				                log.info("CACHE REMOVE : {}", cause);
				            }
						}
				   })
				   .build(new CacheLoader<String, Object>() {
					   @Override
						public Object load(String key) throws Exception {
							return FetchResouceCommandManager.RESOURCES.get(key.toUpperCase()).fetch(key);
						}
				   });
						   
		eventBus = new EventBus();
		eventListener = new EventListener<String, Object>(cache);
		eventBus.register(eventListener);
	}
	
	public static TimeBombCacheImpl getInstance() {
		return bombCache;
	}
	
	@Override
	public Object getData(String key) {
		try {
			return cache.get(key);
		} catch (ExecutionException e) {
			log.error("ERROR IN : TimeBombCacheImpl -> getData()" , e);
		}
		return null;
	}
	
	@Override
	public void clear() {
		cache.invalidateAll();
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}
}
