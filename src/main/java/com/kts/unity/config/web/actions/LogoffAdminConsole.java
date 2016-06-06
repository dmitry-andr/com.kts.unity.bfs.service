package com.kts.unity.config.web.actions;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class LogoffAdminConsole extends ActionSupport implements SessionAware{
    
    private Map session;

    @Override
    public String execute() throws Exception {
        session.put("authenticated", false);
        return ActionConstants.SUCCESS;
    }
    
    
    
    public Map getSession() {
        return session;
    }
   
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
