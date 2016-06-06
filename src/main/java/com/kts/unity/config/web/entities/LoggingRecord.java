package com.kts.unity.config.web.entities;

import java.util.Date;


public class LoggingRecord {
    private int recordId;
    private String remoteAppVersion;
    private String playerName;
    private Date transactionDate;
    private String transactionTime;
    private String logRegordText;

    
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public int getRecordId() {
        return recordId;
    }
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
    public Date getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    public String getTransactionTime() {
        return transactionTime;
    }
    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }
    public String getLogRegordText() {
        return logRegordText;
    }
    public void setLogRegordText(String logRegordText) {
        this.logRegordText = logRegordText;
    }

    public String getRemoteAppVersion() {
        return remoteAppVersion;
    }

    public void setRemoteAppVersion(String remoteAppVersion) {
        this.remoteAppVersion = remoteAppVersion;
    }
    
}
