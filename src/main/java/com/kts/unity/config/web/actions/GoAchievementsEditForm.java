package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.entities.configs.AchievementsConfiguration;

public class GoAchievementsEditForm {
    
    private AchievementsConfiguration config;
    
    public String execute(){
        
        this.config = AchievementsConfiguration.getInstance();        
                
        return ActionConstants.SUCCESS;
    }

    
    public AchievementsConfiguration getConfig() {
        return config;
    }

    public void setConfig(AchievementsConfiguration config) {
        this.config = config;
    }
}
