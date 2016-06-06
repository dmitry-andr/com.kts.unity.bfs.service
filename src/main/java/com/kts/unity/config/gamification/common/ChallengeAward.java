package com.kts.unity.config.gamification.common;

public class ChallengeAward {
    private int playerId;
    private String awardsList;

    public String getAwardsList() {
        return awardsList;
    }
    public void setAwardsList(String awardsList) {
        this.awardsList = awardsList;
    }
    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
