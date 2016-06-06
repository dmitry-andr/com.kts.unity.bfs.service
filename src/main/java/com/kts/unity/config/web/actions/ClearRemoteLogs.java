package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class ClearRemoteLogs extends ActionSupport implements SessionAware {

    private Map session;
    private String selectedNumOfRecToKeep;
    
        @Override
    public String execute() {

        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
            int numOfRowsToKeep = Integer.parseInt(selectedNumOfRecToKeep);
            
            ConfigManager configMgr =  (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
            
            int numberOfRecords = configMgr.getNumberOfRemoteLogsRecords();
            
            if(numOfRowsToKeep < numberOfRecords){
                configMgr.removeLogsRecords(numOfRowsToKeep);
            }
            
            
            return ActionConstants.SUCCESS;
        } else {
            return ActionConstants.AUTHENTICATION;
        }
    }

    public String getSelectedNumOfRecToKeep() {
        return selectedNumOfRecToKeep;
    }
    public void setSelectedNumOfRecToKeep(String selectedNumOfRecToKeep) {
        this.selectedNumOfRecToKeep = selectedNumOfRecToKeep;
    }
    public Map getSession() {
        return session;
    }
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
