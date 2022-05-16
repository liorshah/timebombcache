package com.time.bomb.event;


import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.Subscribe;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventListener<K, V> {

    private final LoadingCache<K, V> cache;
    
	@Subscribe
    public void clearCacheEvent(ClearCacheEvent event) {
    	cache.invalidateAll();
    }
}