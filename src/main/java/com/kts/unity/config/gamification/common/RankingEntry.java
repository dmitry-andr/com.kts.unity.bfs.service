package com.kts.unity.config.gamification.common;

public abstract class RankingEntry {

    private int playerId;
    private String playerName;
    private String avatar;
    private int rank;
    private long lastActivityTime;
    private int positionAtLastGame;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPositionAtLastGame() {
        return positionAtLastGame;
    }

    public void setPositionAtLastGame(int positionAtLastGame) {
        this.positionAtLastGame = positionAtLastGame;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public void setLastActivityTime(long lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
