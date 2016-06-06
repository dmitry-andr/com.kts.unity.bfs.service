package com.kts.unity.config.gamification.actions;

import com.kts.unity.config.gamification.ChallengeTypes;
import com.kts.unity.config.gamification.GamificationPlugin;
import com.kts.unity.config.web.actions.ActionConstants;
import com.kts.unity.config.web.utils.CommonUtils;
import com.kts.unity.config.web.utils.Dictionary;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class SetDayTrnmntFinishTimeAndDate extends ActionSupport implements SessionAware {
    private Map session;
    private String activeStatusTimerStrVal;
    private String dayTimerNextDateDateStrVal;
    private String selectedEndTime;
    private String selectedNumOfDaysToDelay;
    private String finishDateStrVal;
    
    @Override
    public String execute() {
        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
            if (("on".equalsIgnoreCase(activeStatusTimerStrVal)) 
                    && (dayTimerNextDateDateStrVal != null) && (!"".equals(dayTimerNextDateDateStrVal))
                    && (finishDateStrVal != null) && (!"".equals(finishDateStrVal))
                    ) {
                Dictionary.putValue(GamificationPlugin.DAY_END_DATE_TIME_ENABLED_KEY, "true");
                Dictionary.putValue(GamificationPlugin.DAY_NEXT_AWARDING_DATE_KEY, dayTimerNextDateDateStrVal);
                Dictionary.putValue(GamificationPlugin.DAY_FINISH_DATE_KEY, finishDateStrVal);
                Dictionary.putValue(GamificationPlugin.DAY_END_TIME_KEY, selectedEndTime);
                Dictionary.putValue(GamificationPlugin.DAY_NUM_OF_DAYS_TO_DELAY_KEY, selectedNumOfDaysToDelay);
                Dictionary.putValue(GamificationPlugin.DAY_END_TIME_MILLIS_KEY, String.valueOf(CommonUtils.getTimeInMillis(dayTimerNextDateDateStrVal, selectedEndTime)));
                
                if(GamificationPlugin.getChallenges().get(ChallengeTypes.DAY.getValue()).getIsActive() != 1){
                    GamificationPlugin.updateChallengeActiveStatus(ChallengeTypes.DAY.getValue(), 1);
                }

            } else {
                GamificationPlugin.cleanDailyTrnmtTimeData();
            }

            return ActionConstants.SUCCESS;
        } else {
            return ActionConstants.AUTHENTICATION;
        }
    }
    
    
    public String getActiveStatusTimerStrVal() {
        return activeStatusTimerStrVal;
    }

    public void setActiveStatusTimerStrVal(String activeStatusTimerStrVal) {
        this.activeStatusTimerStrVal = activeStatusTimerStrVal;
    }

    public String getDayTimerNextDateDateStrVal() {
        return dayTimerNextDateDateStrVal;
    }

    public void setDayTimerNextDateDateStrVal(String endDateStrVal) {
        this.dayTimerNextDateDateStrVal = endDateStrVal;
    }

    public String getSelectedEndTime() {
        return selectedEndTime;
    }

    public void setSelectedEndTime(String selectedEndTime) {
        this.selectedEndTime = selectedEndTime;
    }

    public String getSelectedNumOfDaysToDelay() {
        return selectedNumOfDaysToDelay;
    }

    public void setSelectedNumOfDaysToDelay(String selectedNumOfWeeksToRepeat) {
        this.selectedNumOfDaysToDelay = selectedNumOfWeeksToRepeat;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public String getFinishDateStrVal() {
        return finishDateStrVal;
    }

    public void setFinishDateStrVal(String finishDateStrVal) {
        this.finishDateStrVal = finishDateStrVal;
    }
    
    
}