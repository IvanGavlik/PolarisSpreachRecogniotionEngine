package com.polaris.speachRecognitoinEngine.mqtt;

public final class MqttConfiguration {
	
	private final String broker;
	private final String topic;
	private final String clientId;
	private final int qos;
	private final boolean cleanSession;
	private final boolean automaticReconnect;
	
	private MqttConfiguration(String broker, String topic, String clientId, int qos, boolean cleanSession, boolean automaticReconnect) {
		this.broker = broker;
		this.topic = topic;
		this.clientId = clientId;
		this.qos = qos;
		this.cleanSession = cleanSession;
		this.automaticReconnect = automaticReconnect;
	}

	public String getBroker() {
		return broker;
	}

	public String getTopic() {
		return topic;
	}

	public String getClientId() {
		return clientId;
	}

	public int getQos() {
		return qos;
	}

	public boolean isCleanSession() {
		return cleanSession;
	}
	
	public boolean isAutomaticReconnect() {
		return automaticReconnect;
	}
	
	private static MqttConfiguration configuration;  
	public static MqttConfiguration getInstance(String broker, String topic, String clientId, int qos, boolean cleanSession, boolean automaticReconnect) {
		if(configuration == null) {
			synchronized (MqttConfiguration.class) {
				if(configuration == null) {
					configuration = new MqttConfiguration(broker, topic, clientId, qos, cleanSession, automaticReconnect);
				}
			}
		}
		return configuration;
	}
	
	public static MqttConfiguration getInstance() {
		if(configuration == null) {
			synchronized (MqttConfiguration.class) {
				if(configuration == null) {
					throw new IllegalStateException();	
				}
			}
		}
		return configuration;
		
	}

	
	@Override
	public String toString() {
		return "MqttConfiguration [broker=" + broker + ", topic=" + topic + ", clientId=" + clientId + ", qos=" + qos
				+ ", cleanSession=" + cleanSession + ", automaticReconnect=" + automaticReconnect + "]";
	}
	
}
