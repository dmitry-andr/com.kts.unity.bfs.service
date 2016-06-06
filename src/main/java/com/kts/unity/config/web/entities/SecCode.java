package com.kts.unity.config.web.entities;

public class SecCode {
    
    private int playerId;
    private String secCode;
    private int permanent;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getSecCode() {
        return secCode;
    }

    public void setSecCode(String secCode) {
        this.secCode = secCode;
    }

    public int getPermanent() {
        return permanent;
    }

    public void setPermanent(int permanent) {
        this.permanent = permanent;
    }
    
}
