package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import org.springframework.core.io.Resource;

public class UploadAvatarsConfiguration extends ActionSupport{
    
    private File avatarsConfig;
    private String avatarsConfigContentType;
    private String avatarsConfigFileName;
    private String message;
    
    @Override
    public String execute() throws Exception {
            
        if(this.avatarsConfig != null){
            
            if(!avatarsConfigFileName.equals(ConfigParams.AVATARS_CONFIG_FILE_NAME)){
                this.message = "!!! File name must be : " + ConfigParams.AVATARS_CONFIG_FILE_NAME;
                return ActionConstants.SUCCESS;
            }            

            ConfigManager cfMgr = (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
            
            Resource avatarsConfigFileRes = AppContext.getContext().getResource(ConfigParams.APP_MOB_AVATARS_CONFIG_FILE_PATH_NAME);
            
            int status = cfMgr.updateAvatarConfigsWithFile(this.avatarsConfig, avatarsConfigFileRes.getFile().getPath(), 1);
            
            
            this.message = "Avatar config uploaded at path : " + avatarsConfigFileRes.getFile().getPath() + " with ststaus = " + status;
                        
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
    public File getAvatarsConfig() {
        return avatarsConfig;
    }
    public void setAvatarsConfig(File avatarsConfig) {
        this.avatarsConfig = avatarsConfig;
    }
    public String getAvatarsConfigContentType() {
        return avatarsConfigContentType;
    }
    public void setAvatarsConfigContentType(String avatarsConfigContentType) {
        this.avatarsConfigContentType = avatarsConfigContentType;
    }
    public String getAvatarsConfigFileName() {
        return avatarsConfigFileName;
    }
    public void setAvatarsConfigFileName(String avatarsConfigFileName) {
        this.avatarsConfigFileName = avatarsConfigFileName;
    }

}