package com.kts.unity.config.gcm.actions;

import com.kts.unity.config.gcm.GCMSenderManager;
import com.kts.unity.config.web.actions.ActionConstants;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;


public class SaveNotifyPlayersOnlineOpponents extends ActionSupport implements SessionAware {
    private Map session;
    private String notifyPlayersOnlineUsersStrVal;
    private String notificationsPeriodHoursStrVal;
    
    @Override
    public String execute() {

        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
                        
            try{
                int hoursInForm = Integer.parseInt(notificationsPeriodHoursStrVal);
                if((hoursInForm > 0) && (hoursInForm < ConfigParams.MAX_PERIOD_OF_BROADCAST_PLAYERS_NOTIFICATION_DAYS)){
                    GCMSenderManager.setBroadcastNotificationPeriodHours(hoursInForm);
                }                
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            if("on".equalsIgnoreCase(notifyPlayersOnlineUsersStrVal)){
                GCMSenderManager.setNotifyPlayersAboutOpponentsOnline(true);                
            }else{
                GCMSenderManager.setNotifyPlayersAboutOpponentsOnline(false);
            }
            
            
            return ActionConstants.SUCCESS;
        } else {
            return ActionConstants.AUTHENTICATION;
        }
    }

    public String getNotifyPlayersOnlineUsersStrVal() {
        return notifyPlayersOnlineUsersStrVal;
    }

    public void setNotifyPlayersOnlineUsersStrVal(String notifyPlayersOnlineUsersStrVal) {
        this.notifyPlayersOnlineUsersStrVal = notifyPlayersOnlineUsersStrVal;
    }

    public String getNotificationsPeriodHoursStrVal() {
        return notificationsPeriodHoursStrVal;
    }

    public void setNotificationsPeriodHoursStrVal(String notificationsPeriodHoursStrVal) {
        this.notificationsPeriodHoursStrVal = notificationsPeriodHoursStrVal;
    }

    
    public Map getSession() {
        return session;
    }
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
