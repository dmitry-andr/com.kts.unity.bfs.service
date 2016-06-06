package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.entities.configs.PerksConfiguration;

public class GoPerksEditForm {

    private PerksConfiguration config = null;

    public String execute() {

        this.config = PerksConfiguration.getInstance();

        return ActionConstants.SUCCESS;
    }

    public PerksConfiguration getConfig() {
        return config;
    }

    public void setConfig(PerksConfiguration config) {
        this.config = config;
    }

    
}
