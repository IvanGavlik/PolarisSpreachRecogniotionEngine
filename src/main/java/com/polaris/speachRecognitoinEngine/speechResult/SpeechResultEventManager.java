package com.polaris.speachRecognitoinEngine.speechResult;

import java.util.ArrayList;
import java.util.List;

public final class SpeechResultEventManager {
	
	private static SpeechResultEventManager instance = new SpeechResultEventManager();  
	
	private SpeechResultEventManager() {} 
	
	public static SpeechResultEventManager getInstance() {
		return instance;
	}
	
	
	private List<SpeechResultEventListener> listeners = new ArrayList<SpeechResultEventListener>();
	
	public void publishEvent(final SpeechResultEvent speechResultEvent) {
		listeners.stream().forEach(el -> {
			try {
				el.onSpeechResultEvent(speechResultEvent);
			} catch (Exception e) {
				throw new PublishSpeechResultEventException(e);
			}
		}); 
	} 
	
	public void addSpeechResultEventListener(SpeechResultEventListener speechResultEventListener) {
		listeners.add(speechResultEventListener);
	}
	
}
