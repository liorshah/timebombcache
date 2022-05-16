package com.time.bomb.app;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.eventbus.EventBus;
import com.time.bomb.analyzer.AnalyzerProgram;
import com.time.bomb.background.RandomCacheClear;
import com.time.bomb.background.RandomEventTrigger;
import com.time.bomb.cache.TimeBombCacheImpl;


public class TimeBombCacheProjectApplication {

	public static void main(String[] args) {
		

		TimeBombCacheImpl instance = TimeBombCacheImpl.getInstance();
		EventBus eventBus = instance.getEventBus();
		AnalyzerProgram analyzerProgram = new AnalyzerProgram();
		
		Runnable task1 = new RandomEventTrigger(analyzerProgram);
		Runnable task2 = new RandomCacheClear(eventBus);
		
		ScheduledThreadPoolExecutor threadPool  = new ScheduledThreadPoolExecutor(2);
		
        threadPool.scheduleAtFixedRate(task1, 2,2, TimeUnit.SECONDS);
 
        threadPool.scheduleAtFixedRate(task2, 1, 120, TimeUnit.SECONDS);
        
	}

}
