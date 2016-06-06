package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.entities.GameUsageStatistics;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;


public class DeleteGameUsageStatUsingAdminConsole extends ActionSupport implements SessionAware{
    
    private String statRecordIdStringVal;
    private ArrayList<GameUsageStatistics> usageStatistics;
    private int numberOfStatRecords;
    
    private Map session;
    private String message;

    @Override
    public String execute() throws Exception {
        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
                        
            ConfigManager confMgr =  (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);        
            
            int status = confMgr.removeUsageStatisticsRecord(statRecordIdStringVal);
            if(status == 1){
                this.message = "Sataistics with ID : " + statRecordIdStringVal + " - was removed successfuly";
            }else{
                this.message = "Error while delteting statistics record with ID : " + statRecordIdStringVal;
            }
            
            
            this.usageStatistics = confMgr.getUsageStatisticsRecords(Settings.getInitialNumOfPlayersOnAdminPage());
            this.numberOfStatRecords = confMgr.getNumberOfStatisticsRecords();
            
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

    public ArrayList<GameUsageStatistics> getUsageStatistics() {
        return usageStatistics;
    }

    public void setUsageStatistics(ArrayList<GameUsageStatistics> usageStatistics) {
        this.usageStatistics = usageStatistics;
    }

    
    public long getCurrentHeapSize() {
        return Runtime.getRuntime().totalMemory();
    }

    public long getFreeHeapSize() {
        return Runtime.getRuntime().freeMemory();
    }

    public long getMaxHeapSize() {
        return Runtime.getRuntime().maxMemory();
    }
    public int getNumberOfStatRecords() {
        return numberOfStatRecords;
    }

    public void setNumberOfStatRecords(int numberOfStatRecords) {
        this.numberOfStatRecords = numberOfStatRecords;
    }
}
