package com.kts.unity.config.gamification.actions;

import com.kts.unity.config.gamification.ChallengeTypes;
import com.kts.unity.config.gamification.GamificationPlugin;
import com.kts.unity.config.web.actions.ActionConstants;
import com.kts.unity.config.web.utils.CommonUtils;
import com.kts.unity.config.web.utils.Dictionary;
import com.opensymphony.xwork2.ActionSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class SetTournamentCountdownTimerSettings extends ActionSupport implements SessionAware {

    private Map session;
    private String activeStatusTimerStrVal;
    private String endDateStrVal;
    private String selectedEndTime;

    @Override
    public String execute() {
        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
            if (("on".equalsIgnoreCase(activeStatusTimerStrVal)) && (endDateStrVal != null) && (!"".equals(endDateStrVal))) {
                Dictionary.putValue(GamificationPlugin.TOURNAMENT_END_DATE_TIME_ENABLED_KEY, "true");
                Dictionary.putValue(GamificationPlugin.TOURNAMENT_END_DATE_KEY, endDateStrVal);
                Dictionary.putValue(GamificationPlugin.TOURNAMENT_END_TIME_KEY, selectedEndTime);
                Dictionary.putValue(GamificationPlugin.TOURNAMENT_END_TIME_MILLIS_KEY, String.valueOf(CommonUtils.getTimeInMillis(endDateStrVal, selectedEndTime)));

                if(GamificationPlugin.getChallenges().get(ChallengeTypes.TIME_PERIOD_TOURNAMENT.getValue()).getIsActive() != 1){
                    GamificationPlugin.updateChallengeActiveStatus(ChallengeTypes.TIME_PERIOD_TOURNAMENT.getValue(), 1);
                }
            } else {
                Dictionary.removeValue(GamificationPlugin.TOURNAMENT_END_DATE_TIME_ENABLED_KEY);
                Dictionary.removeValue(GamificationPlugin.TOURNAMENT_END_DATE_KEY);
                Dictionary.removeValue(GamificationPlugin.TOURNAMENT_END_TIME_KEY);
                Dictionary.removeValue(GamificationPlugin.TOURNAMENT_END_TIME_MILLIS_KEY);
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

    public String getEndDateStrVal() {
        return endDateStrVal;
    }

    public void setEndDateStrVal(String endDateStrVal) {
        this.endDateStrVal = endDateStrVal;
    }

    public String getSelectedEndTime() {
        return selectedEndTime;
    }

    public void setSelectedEndTime(String selectedEndTime) {
        this.selectedEndTime = selectedEndTime;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
