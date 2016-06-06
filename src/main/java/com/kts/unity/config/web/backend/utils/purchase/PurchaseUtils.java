package com.kts.unity.config.web.backend.utils.purchase;

public class PurchaseUtils {

    
    public static boolean isItemBought(int itemId, String purchasedItemsList){
        if(purchasedItemsList == null || "".equals(purchasedItemsList)){
            return false;
        }
        return purchasedItemsList.contains(String.valueOf(itemId));
    }
    
    
    
}
