package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.entities.configs.WeaponConfiguration;

public class GoWeaponEditForm {
    
    private WeaponConfiguration config = null;
    
    public String execute() {

        this.config = WeaponConfiguration.getInstance();

        return ActionConstants.SUCCESS;
    }

    public WeaponConfiguration getConfig() {
        return config;
    }
    public void setConfig(WeaponConfiguration config) {
        this.config = config;
    }
 
    
    
}
