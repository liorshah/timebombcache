package com.time.bomb.cache;

public interface TimeBombCache<K, V> {
	
	V getData(String key);
	
	void clear();

}
