package com.kts.unity.config.gcm.actions;

import com.kts.unity.config.gcm.GCMSenderManager;
import com.kts.unity.config.web.actions.ActionConstants;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class SendAllGcmMessages extends ActionSupport implements SessionAware {

    private Map session;
    private String message;
    private String paramGCMMsgText;

    @Override
    public String execute() {

        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {

            String status = GCMSenderManager.getInstance().sendBroadcastMessage(paramGCMMsgText);

            this.message = "Messages were sent successfully" + "<h3> Status :" + status + "</h3>";

            return ActionConstants.SUCCESS;
        } else {
            return ActionConstants.AUTHENTICATION;
        }
    }

    

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public String getParamGCMMsgText() {
        return paramGCMMsgText;
    }

    public void setParamGCMMsgText(String paramGCMMsgText) {
        this.paramGCMMsgText = paramGCMMsgText;
    }
    
}