package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.PlayerManager;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.entities.player.PlayerWithPurchaseData;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import java.util.ArrayList;

public class ViewPurchaseTransactionsList {
    
    private ArrayList<PlayerWithPurchaseData> transactionsList;
    private String showAll;
    private int numberOfRecords;

    
    public String execute(){
        PlayerManager playerMgr =  (PlayerManager) AppContext.getContext().getBean(AppSpringBeans.PLAYER_MANAGER_BEAN);        
        
        this.numberOfRecords = playerMgr.getNumberOfTransactions();
        
        if("true".equals(showAll)){
            this.transactionsList = playerMgr.getPlayersWithPurchaseList(Settings.getMaxNumOfPlayersOnAdminPage());            
        }else{
            this.transactionsList = playerMgr.getPlayersWithPurchaseList(Settings.getInitialNumOfPlayersOnAdminPage());
        }
                
        return ActionConstants.SUCCESS;
    }
    
    
    
    
    
    public int getNumberOfRecords() {
        return numberOfRecords;
    }
    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }
    public String getShowAll() {
        return showAll;
    }
    public void setShowAll(String showAll) {
        this.showAll = showAll;
    }
    public ArrayList<PlayerWithPurchaseData> getTransactionsList() {
        return transactionsList;
    }
    public void setTransactionsList(ArrayList<PlayerWithPurchaseData> transactionsList) {
        this.transactionsList = transactionsList;
    }
    
    
}
