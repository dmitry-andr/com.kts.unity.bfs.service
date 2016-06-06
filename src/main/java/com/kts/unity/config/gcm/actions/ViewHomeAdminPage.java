package com.kts.unity.config.gcm.actions;

import com.kts.unity.config.gcm.GCMManager;
import com.kts.unity.config.gcm.GCMSenderManager;
import com.kts.unity.config.gcm.GCMUser;
import com.kts.unity.config.web.actions.ActionConstants;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import java.util.Date;
import java.util.List;

public class ViewHomeAdminPage {
    
    private List<GCMUser> gcmUsers;
    private int numberOfRecords;
    private static Boolean notifyPlayersAboutOpponentsOnline = null;
    private String notificationsPeriodHoursStrVal;
    private long daysToBroadcastRemain;
    private long hoursToBroadcastRemain;

    public String execute(){
        
        GCMManager gcmMgr = (GCMManager) AppContext.getContext().getBean(AppSpringBeans.GCM_MANAGER_BEAN);
        
        this.gcmUsers = gcmMgr.getGcmUsersList(GCMSenderManager.MULTICAST_SIZE);
        this.numberOfRecords = gcmMgr.getNumOfGCMUsers();
        notifyPlayersAboutOpponentsOnline = GCMSenderManager.getNotifyPlayersAboutOpponentsOnline();
        notificationsPeriodHoursStrVal = String.valueOf(GCMSenderManager.getBroadcastNotificationPeriodHours());
        
        //86400000; //24h * 60min * 60sec * 1000
        long currentTimeMillis = (new Date()).getTime();        
        long dateDiffMillis = (GCMSenderManager.getLastOnlineOpponentsBroadcastTimestampMillis() + GCMSenderManager.getBroadcastNotificationPeriodHours() * 3600000 - currentTimeMillis);
        daysToBroadcastRemain = (long) Math.floor(dateDiffMillis / 86400000);
        hoursToBroadcastRemain = (long) Math.floor(dateDiffMillis/(60 * 60 * 1000) % 24);
        
        
        return ActionConstants.SUCCESS;
    }
    
    
    public List<GCMUser> getGcmUsers() {
        return gcmUsers;
    }
    public void setGcmUsers(List<GCMUser> gcmUsers) {
        this.gcmUsers = gcmUsers;
    }
    public int getNumberOfRecords() {
        return numberOfRecords;
    }
    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }
    public boolean isNotifyPlayersAboutOpponentsOnline() {
        return notifyPlayersAboutOpponentsOnline;
    }
    public void setNotifyPlayersAboutOpponentsOnline(boolean notifyPlayersAbtOpponentsOnline) {
        notifyPlayersAboutOpponentsOnline = notifyPlayersAbtOpponentsOnline;
    }
    public String getNotificationsPeriodHoursStrVal() {
        return notificationsPeriodHoursStrVal;
    }

    public void setNotificationsPeriodHoursStrVal(String notificationsPeriodHoursStrVal) {
        this.notificationsPeriodHoursStrVal = notificationsPeriodHoursStrVal;
    }

    public long getDaysToBroadcastRemain() {
        return daysToBroadcastRemain;
    }

    public void setDaysToBroadcastRemain(int daysToBroadcastRemain) {
        this.daysToBroadcastRemain = daysToBroadcastRemain;
    }

    public long getHoursToBroadcastRemain() {
        return hoursToBroadcastRemain;
    }

    public void setHoursToBroadcastRemain(int hoursToBroadcastRemain) {
        this.hoursToBroadcastRemain = hoursToBroadcastRemain;
    }
    
}
