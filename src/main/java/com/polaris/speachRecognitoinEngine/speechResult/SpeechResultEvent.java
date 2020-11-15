package com.polaris.speachRecognitoinEngine.speechResult;

public final class SpeechResultEvent {
	
	private String hypothesis;
	
	public SpeechResultEvent(String hypothesis) {
		this.hypothesis = hypothesis;
	}
	
    /**
     * @return string representation of the result.
     */
    public String getHypothesis() {
    	return this.hypothesis;
    }
}
