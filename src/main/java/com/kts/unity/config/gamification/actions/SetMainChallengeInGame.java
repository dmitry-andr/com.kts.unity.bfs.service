package com.kts.unity.config.gamification.actions;

import com.kts.unity.config.gamification.GamificationPlugin;
import com.kts.unity.config.gamification.common.Challenge;
import com.kts.unity.config.web.actions.ActionConstants;
import com.kts.unity.config.web.utils.Dictionary;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class SetMainChallengeInGame extends ActionSupport implements SessionAware {
    private Map session;
    private String message;    
    private String selectedChallenge;
    
    
    @Override
    public String execute() {

        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
            
            if(!"none".equals(selectedChallenge)){
                Challenge selectedChallg = GamificationPlugin.getChallenges().get(selectedChallenge);
                if(selectedChallg.getIsActive() != 0){
                    Dictionary.putValue(GamificationPlugin.ACTIVE_CHALLENGE_IN_GAME_KEY, selectedChallenge);
                }                
            }else{//admin selected "none" option - remove active challenge from Dictionary
                Dictionary.removeValue(GamificationPlugin.ACTIVE_CHALLENGE_IN_GAME_KEY);
            }
            
            return ActionConstants.SUCCESS;
        } else {
            return ActionConstants.AUTHENTICATION;
        }
        
    }

    
    public String getSelectedChallenge() {
        return selectedChallenge;
    }
    public void setSelectedChallenge(String selectedChallenge) {
        this.selectedChallenge = selectedChallenge;
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
}
