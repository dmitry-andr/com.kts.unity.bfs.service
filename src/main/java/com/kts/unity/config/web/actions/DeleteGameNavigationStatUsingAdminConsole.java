package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.entities.GameNavigationStatistics;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class DeleteGameNavigationStatUsingAdminConsole extends ActionSupport implements SessionAware{
    
    private String statRecordIdStringVal;
    private ArrayList<GameNavigationStatistics> navigationStatistics;
    private int numberOfStatRecords;
    
    private Map session;
    private String message;
    
    
    @Override
    public String execute() throws Exception {
        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
                        
            ConfigManager confMgr =  (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);        
            
            int status = confMgr.removeNavigationStatisticsRecord(statRecordIdStringVal);
            if(status == 1){
                this.message = "Sataistics with ID : " + statRecordIdStringVal + " - was removed successfuly";
            }else{
                this.message = "Error while delteting statistics record with ID : " + statRecordIdStringVal;
            }
            
            
            this.navigationStatistics = confMgr.getNavigationStatisticsRecords(Settings.getInitialNumOfPlayersOnAdminPage());
            this.numberOfStatRecords = confMgr.getNumberOfNavigationStatisticsRecords();
            
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
    
    public String getStatRecordIdStringVal() {
        return statRecordIdStringVal;
    }

    public void setStatRecordIdStringVal(String statRecordIdStringVal) {
        this.statRecordIdStringVal = statRecordIdStringVal;
    }
    public ArrayList<GameNavigationStatistics> getNavigationStatistics() {
        return navigationStatistics;
    }

    public void setNavigationStatistics(ArrayList<GameNavigationStatistics> navigationStatistics) {
        this.navigationStatistics = navigationStatistics;
    }
    public int getNumberOfStatRecords() {
        return numberOfStatRecords;
    }

    public void setNumberOfStatRecords(int numberOfStatRecords) {
        this.numberOfStatRecords = numberOfStatRecords;
    }
}
