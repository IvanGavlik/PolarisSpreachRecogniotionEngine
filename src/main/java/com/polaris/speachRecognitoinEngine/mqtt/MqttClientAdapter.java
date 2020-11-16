package com.polaris.speachRecognitoinEngine.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttClientAdapter {

	private MqttConfiguration configuration;
	private MqttConnectOptions connOpts;
	private MqttClient client;
	
	// load configuration, create mqtt adapter
	public MqttClientAdapter(MqttConfiguration configuration) throws MqttException {
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

		// TODO must connect befrore publish, should I connect on creating, only one
		System.out.println("Connecting to broker: " + configuration.getBroker());
		try {
			client.connect(connOpts);			
		} catch (Exception e) {
			throw new MqttClientException(e, MqttClientException.EXCEPTION_CODE_FAILED_TO_CONNECT);
		}
		System.out.println("Connected");

		System.out.println("Publishing message: " + content);
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

		System.out.println("Message published");
	}

	public void close() throws Exception {
		if (client != null) {
			client.disconnect();
			System.out.println("Disconnected");
		}
	}

}
