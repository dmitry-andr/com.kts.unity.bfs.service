package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.entities.configs.RocketsConfiguration;

public class GoRocketsEditForm {
    
    private RocketsConfiguration config = null;

    public String execute(){
        
        this.config = RocketsConfiguration.getInstance();
                
        return ActionConstants.SUCCESS;
    }

    public RocketsConfiguration getConfig() {
        return config;
    }

    public void setConfig(RocketsConfiguration config) {
        this.config = config;
    }
    
    
}
