package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.entities.configs.Configuration;

public class GoParamsEditForm{
    
    private Configuration config = null;
    private String message;
    
    public String execute(){
        
        this.config = Configuration.getInstance();
                
        return ActionConstants.SUCCESS;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
}
