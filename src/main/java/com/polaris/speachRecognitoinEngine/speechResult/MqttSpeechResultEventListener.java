package com.polaris.speachRecognitoinEngine.speechResult;

import com.polaris.speachRecognitoinEngine.mqtt.MqttClient;

public final class MqttSpeechResultEventListener implements SpeechResultEventListener {
	
	private MqttClient mqttClient;
	
	public MqttSpeechResultEventListener() {
		this.mqttClient = new MqttClient();
	}  
	
	@Override
	public void onSpeechResultEvent(SpeechResultEvent speechResultEvent) {
		mqttClient.publish(speechResultEvent.getHypothesis());
	}


}
