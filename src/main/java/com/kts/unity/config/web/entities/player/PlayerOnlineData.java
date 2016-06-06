package com.kts.unity.config.web.entities.player;


public class PlayerOnlineData {
    private int playerId;
    private long lastActivityTime;

    public long getLastActivityTime() {
        return lastActivityTime;
    }
    public void setLastActivityTime(long lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }
    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    
}
