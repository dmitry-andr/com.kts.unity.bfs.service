package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.utils.Settings;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class InitializeLoginScreen extends ActionSupport implements SessionAware {

    private Map session;
    private String message;

    @Override
    public String execute() throws Exception {
                
        if(Settings.isDevelopmentModeEnabled()){//this line enables to access app without authorization if according property set in "settings.properties" file
            session.put("authenticated", true);
            session.put("username", "developer");
        }
        
        
        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
            return ActionConstants.DEFAULT_PAGE;
        }

        return ActionConstants.SUCCESS;
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
    
    
}
