package org.motechproject.icappr.domain;

public class SideEffectQuestion {

    private String audioUrl;
    private String conceptName;
    private String affirmativeKeyPress;
    private String negativeKeyPress;
    
    public SideEffectQuestion(String audioUrl, String conceptName, String affirmativeKeyPress, String negativeKeyPress) {
        this.audioUrl = audioUrl;
        this.conceptName = conceptName;
        this.affirmativeKeyPress = affirmativeKeyPress;
        this.negativeKeyPress = negativeKeyPress;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getAffirmativeKeyPress() {
        return affirmativeKeyPress;
    }

    public void setAffirmativeKeyPress(String affirmativeKeyPress) {
        this.affirmativeKeyPress = affirmativeKeyPress;
    }

    public String getNegativeKeyPress() {
        return negativeKeyPress;
    }

    public void setNegativeKeyPress(String negativeKeyPress) {
        this.negativeKeyPress = negativeKeyPress;
    }
}
