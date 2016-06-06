package com.kts.unity.config.gamification.actions;

import com.kts.unity.config.gamification.GamificationPlugin;
import com.kts.unity.config.gamification.common.Challenge;
import com.kts.unity.config.web.actions.ActionConstants;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;


public class UpdateChallengeActiveStatus extends ActionSupport implements SessionAware {
    private Map session;
    
    private String challengeId;
    
    @Override
    public String execute() {

        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
            
            System.out.println("Challenge Id passed : " + challengeId);
            
            Challenge selectedChallenge = GamificationPlugin.getChallenges().get(challengeId);
            if(selectedChallenge != null){
                int currentActiveStatus = selectedChallenge.getIsActive();
                if(currentActiveStatus == 0){
                    currentActiveStatus = 1;
                }else{
                    currentActiveStatus = 0;
                    GamificationPlugin.clearDataForChallenge(challengeId);                    
                }
                GamificationPlugin.updateChallengeActiveStatus(challengeId, currentActiveStatus);
            }
            
            return ActionConstants.SUCCESS;
        } else {
            return ActionConstants.AUTHENTICATION;
        }
        
    }

    public String getChallengeId() {
        return challengeId;
    }
    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }
        
    public Map getSession() {
        return session;
    }
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
