package com.polaris.speachRecognitoinEngine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.polaris.speachRecognitoinEngine.mqtt.MqttClientAdapter;
import com.polaris.speachRecognitoinEngine.mqtt.MqttConfiguration;
import com.polaris.speachRecognitoinEngine.speechResult.MqttSpeechResultEventListener;
import com.polaris.speachRecognitoinEngine.speechResult.SpeechResultEvent;
import com.polaris.speachRecognitoinEngine.speechResult.SpeechResultEventManager;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;


public class SpeachRecognitoinEngine {

    private static final Logger logger = LogManager.getLogger(SpeachRecognitoinEngine.class); 

	private static SpeechResultEventManager speechResultEventManager = SpeechResultEventManager.getInstance();  
	 
	
	/**
	 * TODO: 
	 * 	-> https://github.com/cmusphinx/sphinx4/blob/master/sphinx4-samples/src/main/java/edu/cmu/sphinx/demo/dialog/DialogDemo.java
	 * @param argstt
	 * @throws Exception
	 */
	public static void startRecognition() throws Exception {
		
		logger.info("startRecognition");
		
		MqttConfiguration mqttConfiguration = MqttConfiguration.getInstance("tcp://broker.hivemq.com:1883", "testtopic/1", "clientId-zU8LMleRAI", 0, true, true);
		speechResultEventManager.addSpeechResultEventListener(new MqttSpeechResultEventListener(new MqttClientAdapter(mqttConfiguration)));
		
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
			if (hypothesis.equals("one")) {
				break;
			}
		}
		
		logger.info("stopRecognition");
		recognizer.stopRecognition();

	}

	public static void main(String[] args) throws Exception {
		SpeachRecognitoinEngine.startRecognition();
	}
}
