package com.polaris.speachRecognitoinEngine.speechResult;

import com.polaris.speachRecognitoinEngine.mqtt.MqttClientAdapter;

public final class MqttSpeechResultEventListener implements SpeechResultEventListener {

	private MqttClientAdapter mqttClient;

	public MqttSpeechResultEventListener(MqttClientAdapter mqttClient) {
		this.mqttClient = mqttClient;
	}

	@Override
	public void onSpeechResultEvent(SpeechResultEvent speechResultEvent) {
		mqttClient.publish(speechResultEvent.getHypothesis());
	}


}
