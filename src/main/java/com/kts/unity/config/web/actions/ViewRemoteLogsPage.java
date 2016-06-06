package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.entities.LoggingRecord;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.Dictionary;
import java.util.ArrayList;

public class ViewRemoteLogsPage {
    
    private ArrayList<LoggingRecord> logRecordsList;
    private String showAll;
    private int numberOfRecords;
    private Boolean notifyAdminsOnLogsIssues;
    public static String NOTIFY_ADMINS_LOG_HEALTH_MONITOR_KEY = "notifyAdminsHealthMonitor";
    
    public String execute(){
        ConfigManager configMgr =  (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
        
        this.numberOfRecords = configMgr.getNumberOfRemoteLogsRecords();
        
        if("true".equalsIgnoreCase(Dictionary.getValue(NOTIFY_ADMINS_LOG_HEALTH_MONITOR_KEY))){
            notifyAdminsOnLogsIssues = true;
        }
        
        if("true".equals(showAll)){
            this.logRecordsList = configMgr.getRemoteLogsList(Settings.getMaxNumOfPlayersOnAdminPage());
        }else{
            this.logRecordsList = configMgr.getRemoteLogsList(Settings.getInitialNumOfPlayersOnAdminPage());
        }
  
        return ActionConstants.SUCCESS;
    }

    
    public ArrayList<LoggingRecord> getLogRecordsList() {
        return logRecordsList;
    }
    public void setLogRecordsList(ArrayList<LoggingRecord> logRecordsList) {
        this.logRecordsList = logRecordsList;
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

    public Boolean getNotifyAdminsOnLogsIssues() {
        return notifyAdminsOnLogsIssues;
    }

    public void setNotifyAdminsOnLogsIssues(Boolean notifyAdminsOnLogsIssues) {
        this.notifyAdminsOnLogsIssues = notifyAdminsOnLogsIssues;
    }
    
}
