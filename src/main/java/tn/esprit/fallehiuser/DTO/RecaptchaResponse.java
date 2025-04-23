package tn.esprit.fallehiuser.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecaptchaResponse {
    private boolean success;
    private float score;
    private String action;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    private String hostname;

    @JsonProperty("error-codes")
    private String[] errorCodes;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public float getScore() {
        return score;
    }

    public String getAction() {
        return action;
    }

    public String getChallengeTs() {
        return challengeTs;
    }

    public String getHostname() {
        return hostname;
    }

    public String[] getErrorCodes() {
        return errorCodes;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setChallengeTs(String challengeTs) {
        this.challengeTs = challengeTs;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setErrorCodes(String[] errorCodes) {
        this.errorCodes = errorCodes;
    }
}
