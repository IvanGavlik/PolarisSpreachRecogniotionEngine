package com.polaris.speachRecognitoinEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.polaris.speachRecognitoinEngine.mqtt.MqttClientAdapter;
import com.polaris.speachRecognitoinEngine.mqtt.MqttConfiguration;
import com.polaris.speachRecognitoinEngine.speechResult.MqttSpeechResultEventListener;
import com.polaris.speachRecognitoinEngine.speechResult.SpeechResultEvent;
import com.polaris.speachRecognitoinEngine.speechResult.SpeechResultEventListener;
import com.polaris.speachRecognitoinEngine.speechResult.SpeechResultEventManager;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class SpeachRecognitoinEngine {

	private static final Logger logger = LogManager.getLogger(SpeachRecognitoinEngine.class);


	/**
	 * TODO: ->
	 * https://github.com/cmusphinx/sphinx4/blob/master/sphinx4-samples/src/main/java/edu/cmu/sphinx/demo/dialog/DialogDemo.java
	 * 
	 * @param argstt
	 * @throws Exception
	 */
	public static void startRecognition(final SpeechResultEventListener speechResultEventListener) throws Exception {

		logger.info("startRecognition");

		disableCMULogs();
		
		SpeechResultEventManager.getInstance().addSpeechResultEventListener(speechResultEventListener);

		//TODO add logic for selecting configuration 
		// enable selection of EN, IT, GERMAN ... or pass own
		Configuration configuration = new Configuration();
		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

		LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
		// Start recognition process pruning previously cached data.
		recognizer.startRecognition(true);

		while (true) {
			SpeechResult result = recognizer.getResult();
			String hypothesis = result.getHypothesis();

			SpeechResultEventManager.getInstance().publishEvent(new SpeechResultEvent(hypothesis));
			if (hypothesis.equals("stop")) {
				break;
			}
		}

		logger.info("stopRecognition");
		recognizer.stopRecognition();

	}

	//TODO must be configurable 
	private static void disableCMULogs() {
		java.util.logging.Logger cmRootLogger = java.util.logging.Logger.getLogger("default.config");
		cmRootLogger.setLevel(java.util.logging.Level.OFF);
		String conFile = System.getProperty("java.util.logging.config.file");
		if (conFile == null) {
			System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
		}
	}

	//TODO what to log in file what to console
	public static void main(String[] args) {

		System.out.println("Welcome to Polaris Speach recognition engine");
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));

			final String topic = reader.readLine();

			//default is mqtt
			// TODO configuration from properties or file 
			MqttConfiguration mqttConfiguration = MqttConfiguration.getInstance("tcp://broker.hivemq.com:1883", topic,
					"clientId-zU8LMleRAI", 0, true, true);
			SpeechResultEventListener srel =  new MqttSpeechResultEventListener(new MqttClientAdapter(mqttConfiguration));

			// Clients will use default or implement they own version of SpeechResultEventListener
			startRecognition(srel);

		} catch (Exception e) {
			System.out.println("Someting whent wrong " + e.getMessage());
			logger.error("Someting whent wrong {}", e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				System.out.println("Someting whent wrong" + e.getMessage());
				logger.error("Someting whent wrong can not close reader {}", e);
			}
		}

	}
}
