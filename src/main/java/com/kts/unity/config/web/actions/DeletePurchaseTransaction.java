package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.PlayerManager;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.entities.player.PlayerWithGameProfAndStatisticsAndChallengeData;
import com.kts.unity.config.web.entities.player.PlayerWithPurchaseData;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class DeletePurchaseTransaction extends ActionSupport implements SessionAware{
    
    private ArrayList<PlayerWithPurchaseData> transactionsList;
    private int numberOfRecords;
    
    private Map session;
    private String message;
    private String transactionIdStrVal;
    
    
    @Override
    public String execute() throws Exception {
        
        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
            
            PlayerManager playerMgr =  (PlayerManager) AppContext.getContext().getBean(AppSpringBeans.PLAYER_MANAGER_BEAN);
            
            
            int transactionId = Integer.parseInt(transactionIdStrVal);
            
            System.out.println("Transaction ID : " + transactionId);
            
            int status = playerMgr.deleteTansaction(transactionId);
            if(status == 1){
                this.message = "Tansaction ID : " + transactionId + " - was removed successfuly";
            }else{
                this.message = "Error while delteting transaction with ID : " + transactionId;
            }
            
            this.transactionsList = playerMgr.getPlayersWithPurchaseList(Settings.getMaxNumOfPlayersOnAdminPage()); 
            
            this.numberOfRecords = playerMgr.getNumberOfTransactions();
                        
            
            return ActionConstants.SUCCESS;
        }else{
            return ActionConstants.AUTHENTICATION;
        } 
    }
    
    
    
    
    
    
    public Map getSession() {
        return session;
    }
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionIdStrVal() {
        return transactionIdStrVal;
    }

    public void setTransactionIdStrVal(String transactionIdStrVal) {
        this.transactionIdStrVal = transactionIdStrVal;
    }

    public ArrayList<PlayerWithPurchaseData> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(ArrayList<PlayerWithPurchaseData> transactionsList) {
        this.transactionsList = transactionsList;
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }
    
    
}
