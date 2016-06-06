package com.kts.unity.config.web.entities.player;

import com.kts.unity.config.web.entities.PurchaseTransactionData;

public class PlayerWithPurchaseData {
    private Player player;
    private PurchaseTransactionData purchaseData;

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public PurchaseTransactionData getPurchaseData() {
        return purchaseData;
    }
    public void setPurchaseData(PurchaseTransactionData purchaseData) {
        this.purchaseData = purchaseData;
    }
    
    
}
