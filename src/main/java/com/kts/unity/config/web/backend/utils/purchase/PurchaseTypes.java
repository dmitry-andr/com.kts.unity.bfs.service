package com.kts.unity.config.web.backend.utils.purchase;

public enum PurchaseTypes {
    COINS_PURCHASE(1), IN_APP_ITEM_PURCHASE(2), SUBSCRIPTION_PURCHASE(3);
    
    private final int value;

    private PurchaseTypes(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
    
}
