package com.kts.unity.config.web.entities;

import java.util.Date;

public class GameUsageStatistics {
    private String applicationId;
    private String playerName;
    private Date dataCollectionDate;
    private String dataCollectionTime;
    private int usageTime;
    private int lastUsageTime;
    private int usedTimes;
    private int averageRespWait;
    private int maxRespWait;
    private int minRespWait;
    private int averPing;
    private int maxPing;
    private int minPing;
    private int averFps;
    private int maxFps;
    private int minFps;
    private int secondsOfUsageSPMode;
    private int numberOfSPGames;

        
    public String getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public int getAverageRespWait() {
        return averageRespWait;
    }
    public void setAverageRespWait(int averageRespWait) {
        this.averageRespWait = averageRespWait;
    }

    public Date getDataCollectionDate() {
        return dataCollectionDate;
    }
    public void setDataCollectionDate(Date dataCollectionDate) {
        this.dataCollectionDate = dataCollectionDate;
    }

    public int getMaxRespWait() {
        return maxRespWait;
    }
    public void setMaxRespWait(int maxRespWait) {
        this.maxRespWait = maxRespWait;
    }

    public int getUsageTime() {
        return usageTime;
    }
    public void setUsageTime(int usageTime) {
        this.usageTime = usageTime;
    }

    public int getMinRespWait() {
        return minRespWait;
    }

    public void setMinRespWait(int minRespWait) {
        this.minRespWait = minRespWait;
    }

    public int getAverFps() {
        return averFps;
    }

    public void setAverFps(int averFps) {
        this.averFps = averFps;
    }

    public int getAverPing() {
        return averPing;
    }

    public void setAverPing(int averPing) {
        this.averPing = averPing;
    }

    public int getMaxFps() {
        return maxFps;
    }

    public void setMaxFps(int maxFps) {
        this.maxFps = maxFps;
    }

    public int getMaxPing() {
        return maxPing;
    }

    public void setMaxPing(int maxPing) {
        this.maxPing = maxPing;
    }

    public int getMinFps() {
        return minFps;
    }

    public void setMinFps(int minFps) {
        this.minFps = minFps;
    }

    public int getMinPing() {
        return minPing;
    }

    public void setMinPing(int minPing) {
        this.minPing = minPing;
    }

    public int getLastUsageTime() {
        return lastUsageTime;
    }

    public void setLastUsageTime(int lastUsageTime) {
        this.lastUsageTime = lastUsageTime;
    }

    public int getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(int usedTimes) {
        this.usedTimes = usedTimes;
    }

    public int getNumberOfSPGames() {
        return numberOfSPGames;
    }

    public void setNumberOfSPGames(int numberOfSPGames) {
        this.numberOfSPGames = numberOfSPGames;
    }

    public int getSecondsOfUsageSPMode() {
        return secondsOfUsageSPMode;
    }

    public void setSecondsOfUsageSPMode(int secondsOfUsageSPMode) {
        this.secondsOfUsageSPMode = secondsOfUsageSPMode;
    }

    public String getDataCollectionTime() {
        return dataCollectionTime;
    }

    public void setDataCollectionTime(String dataCollectionTime) {
        this.dataCollectionTime = dataCollectionTime;
    }
    
    
}
