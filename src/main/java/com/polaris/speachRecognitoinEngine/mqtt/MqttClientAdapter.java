package com.polaris.speachRecognitoinEngine.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.polaris.speachRecognitoinEngine.SpeachRecognitoinEngine;

public class MqttClientAdapter {

	 private static final Logger logger = LogManager.getLogger(MqttClientAdapter.class); 
	
	private MqttConfiguration configuration;
	private MqttConnectOptions connOpts;
	private MqttClient client;
	
	// load configuration, create mqtt adapter
	public MqttClientAdapter(MqttConfiguration configuration) throws MqttException {
		logger.info("MqttClientAdapter configuration {}", configuration);
		if (configuration == null) {
			throw new IllegalArgumentException();
		}
		this.configuration = configuration;
		this.connOpts = new MqttConnectOptions();
		this.connOpts.setCleanSession(configuration.isCleanSession());
		this.connOpts.setAutomaticReconnect(configuration.isAutomaticReconnect());
		this.client = new MqttClient(configuration.getBroker(), configuration.getClientId());
	}

	public void publish(String content) {

		if(!client.isConnected()) {
			// TODO must connect befrore publish, should I connect only first time one
			logger.info("Connecting to broker: {}", configuration.getBroker());
			try {
				client.connect(connOpts);			
			} catch (Exception e) {
				throw new MqttClientException(e, MqttClientException.EXCEPTION_CODE_FAILED_TO_CONNECT);
			}
			logger.info("Connected");
		}

		logger.info("Publishing message: {}", content); 
		MqttMessage message = new MqttMessage(content.getBytes());
		message.setQos(configuration.getQos());

		try {
			client.publish(configuration.getTopic(), message);			
		} catch (Exception e) {
			try {
				close();				
			} catch (Exception closeException) {}
			throw new MqttClientException(e, MqttClientException.EXCEPTION_CODE_FAILED_TO_PUBLISH);
		}

		logger.info("Message published");
	}

	public void close() throws Exception {
		if (client != null) {
			client.disconnect();
			logger.info("Disconnected");
		}
	}

}
