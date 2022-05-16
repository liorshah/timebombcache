package com.time.bomb.background;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.time.bomb.analyzer.AnalyzerProgram;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RandomEventTrigger implements Runnable{
	
	private final Map<Integer, RandomEventCommand> EVENT_COMMADS;
	
	public RandomEventTrigger(AnalyzerProgram analyzerProgram) {

		final Map<Integer, RandomEventCommand> events = new HashMap<>();
		
		events.put(1, ()->{
			log.info("EVENT : RandomEventTrigger : getAllUsersUncompletedTodos");
			analyzerProgram.getAllUsersUncompletedTodos();
		});
		
		events.put(2, ()->{
			Random rand = new Random();
			
			Integer userId = rand.nextInt(50);
			Integer threshold = rand.nextInt(30) + 300;
			
			log.info("EVENT : RandomEventTrigger : getUserAlbumsThatPhotosExceedThreshold : userId={}, threshold={}",userId,threshold);
			analyzerProgram.getUserAlbumsThatPhotosExceedThreshold(userId, threshold);
		});
		
		events.put(3, ()->{
			log.info("EVENT : RandomEventTrigger : getUserPostCommentersEmail");
			analyzerProgram.getUserPostCommentersEmail();
		});
		
		events.put(4, ()->{
			
			Random rand = new Random();
			
			Integer userId = rand.nextInt(50);
			log.info("EVENT : RandomEventTrigger : getUserToDoByUserId : userId={}",userId);
			analyzerProgram.getUserToDoByUserId(userId);
		});
		
		
		EVENT_COMMADS = Collections.unmodifiableMap(events);
		
	}
	
	@Override
	public void run() {
		
	    Random rand = new Random();
		EVENT_COMMADS.get(rand.nextInt(EVENT_COMMADS.size()) + 1).execute();

	}
	
	
}
