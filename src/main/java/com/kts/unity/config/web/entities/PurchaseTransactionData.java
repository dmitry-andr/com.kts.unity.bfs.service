package com.kts.unity.config.web.entities;

import java.util.Date;

public class PurchaseTransactionData {
    private int id;
    private Date transactionDate;
    private String transactionTime;
    private int playerId;
    private int operationType;
    private int purchasedItem;
    private int coinsBalanceChange;

    public int getCoinsBalanceChange() {
        return coinsBalanceChange;
    }
    public void setCoinsBalanceChange(int coinsBalanceChange) {
        this.coinsBalanceChange = coinsBalanceChange;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getOperationType() {
        return operationType;
    }
    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }
    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public int getPurchasedItem() {
        return purchasedItem;
    }
    public void setPurchasedItem(int purchasedItem) {
        this.purchasedItem = purchasedItem;
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
    
    
    
}
