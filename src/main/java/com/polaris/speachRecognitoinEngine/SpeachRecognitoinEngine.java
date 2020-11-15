package com.polaris.speachRecognitoinEngine;


import com.polaris.speachRecognitoinEngine.speechResult.MqttSpeechResultEventListener;
import com.polaris.speachRecognitoinEngine.speechResult.SpeechResultEvent;
import com.polaris.speachRecognitoinEngine.speechResult.SpeechResultEventManager;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;


public class SpeachRecognitoinEngine {


	private static SpeechResultEventManager speechResultEventManager = SpeechResultEventManager.getInstance();  
	 
	
	/**
	 * TODO: 
	 * 	-> https://github.com/cmusphinx/sphinx4/blob/master/sphinx4-samples/src/main/java/edu/cmu/sphinx/demo/dialog/DialogDemo.java
	 * @param args
	 * @throws Exception
	 */
	public static void startRecognition() throws Exception {
		
		speechResultEventManager.addSpeechResultEventListener(new MqttSpeechResultEventListener());
		
		Configuration configuration = new Configuration();

		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

		LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
		// Start recognition process pruning previously cached data.
		recognizer.startRecognition(true);

		while (true) {
			SpeechResult result = recognizer.getResult();
			// Pause recognition process. It can be resumed then with
			// startRecognition(false). 
			String hypothesis = result.getHypothesis();
			
			speechResultEventManager.publishEvent(new SpeechResultEvent(hypothesis));
			
			System.out.print("result::" + hypothesis);
			if (hypothesis.equals("one")) {
				break;
			}
		}
		recognizer.stopRecognition();

	}

}
