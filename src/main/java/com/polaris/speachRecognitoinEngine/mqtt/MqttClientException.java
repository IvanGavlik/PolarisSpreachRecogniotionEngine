package com.polaris.speachRecognitoinEngine.mqtt;

public class MqttClientException extends RuntimeException {
	
	public static final int EXCEPTION_CODE_FAILED_TO_CONNECT = 0; 
	public static final int EXCEPTION_CODE_FAILED_TO_PUBLISH = 0; 
	
	private final int exceptionCode;
	
	public MqttClientException(Exception e, int exCode) {
		super(e);
		this.exceptionCode = exCode;
	}

	public int getExceptionCode() {
		return this.exceptionCode;
	}
	
}
