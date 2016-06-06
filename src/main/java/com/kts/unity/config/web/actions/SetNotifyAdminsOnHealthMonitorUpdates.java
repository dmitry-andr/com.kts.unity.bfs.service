package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.utils.Dictionary;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class SetNotifyAdminsOnHealthMonitorUpdates extends ActionSupport implements SessionAware {
    private Map session;
    private String notifyAdminsStatusStrVal;
    
    @Override
    public String execute() {
        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
            if ("on".equalsIgnoreCase(notifyAdminsStatusStrVal)){
                    Dictionary.putValue(ViewRemoteLogsPage.NOTIFY_ADMINS_LOG_HEALTH_MONITOR_KEY, "true");
            }else{
                Dictionary.removeValue(ViewRemoteLogsPage.NOTIFY_ADMINS_LOG_HEALTH_MONITOR_KEY);
            }
        }
        
        return ActionConstants.SUCCESS;
    }

    public String getNotifyAdminsStatusStrVal() {
        return notifyAdminsStatusStrVal;
    }

    public void setNotifyAdminsStatusStrVal(String notifyAdminsStatusStrVal) {
        this.notifyAdminsStatusStrVal = notifyAdminsStatusStrVal;
    }
    
    
    
    public Map getSession() {
        return session;
    }

    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
