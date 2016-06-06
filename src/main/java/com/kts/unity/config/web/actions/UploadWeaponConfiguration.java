package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import org.springframework.core.io.Resource;


public class UploadWeaponConfiguration extends ActionSupport{
    
    private File weaponConfig;
    private String weaponConfigContentType;
    private String weaponConfigFileName;
    private String message;
    
    @Override
    public String execute() throws Exception {
            
        if(this.weaponConfig != null){
            
            if(!weaponConfigFileName.equals(ConfigParams.WEAPON_CONFIG_FILE_NAME)){
                this.message = "!!! File name must be : " + ConfigParams.WEAPON_CONFIG_FILE_NAME;
                return ActionConstants.SUCCESS;
            }            

            ConfigManager cfMgr = (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
            
            Resource weaponConfigFileRes = AppContext.getContext().getResource(ConfigParams.APP_MOB_WEAPON_CONFIG_FILE_PATH_NAME);
            
            int status = cfMgr.updateWeaponConfigsWithFile(this.weaponConfig, weaponConfigFileRes.getFile().getPath(), 1);
            
            
            this.message = "Weapon config uploaded at path : " + weaponConfigFileRes.getFile().getPath() + " with ststaus = " + status;
                        
            return ActionConstants.SUCCESS;
        }else{
            this.message = "Please select configuration file !";
            return ActionConstants.SUCCESS;
        }    
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public File getWeaponConfig() {
        return weaponConfig;
    }

    public void setWeaponConfig(File weaponConfig) {
        this.weaponConfig = weaponConfig;
    }

    public String getWeaponConfigContentType() {
        return weaponConfigContentType;
    }

    public void setWeaponConfigContentType(String weaponConfigContentType) {
        this.weaponConfigContentType = weaponConfigContentType;
    }

    public String getWeaponConfigFileName() {
        return weaponConfigFileName;
    }

    public void setWeaponConfigFileName(String weaponConfigFileName) {
        this.weaponConfigFileName = weaponConfigFileName;
    }
    
}
