package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import org.springframework.core.io.Resource;

public class UploadAchievementsConfiguration extends ActionSupport{

    private File achievmentsConfig;
    private String achievmentsConfigContentType;
    private String achievmentsConfigFileName;
    private String message;    
    
    
    @Override
    public String execute() throws Exception {
        
        if(this.achievmentsConfig != null){
            
            if(!achievmentsConfigFileName.equals(ConfigParams.ACHIEVEMENTS_CONFIG_FILE_NAME)){
                this.message = "!!! File name must be : " + ConfigParams.ACHIEVEMENTS_CONFIG_FILE_NAME;
                return ActionConstants.SUCCESS;
            }            

            ConfigManager cfMgr = (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
            
            Resource achConfigFileRes = AppContext.getContext().getResource(ConfigParams.APP_ACHIEVEMENTS_CONFIG_FILE_PATH_NAME);
            
            int status = cfMgr.updateAchievementsConfigsWithFile(this.achievmentsConfig, achConfigFileRes.getFile().getPath(), 1);
                        
            this.message = "Achievements config uploaded at path : " + achConfigFileRes.getFile().getPath() + " with ststaus = " + status;
            
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

    public File getAchievmentsConfig() {
        return achievmentsConfig;
    }

    public void setAchievmentsConfig(File achievmentsConfig) {
        this.achievmentsConfig = achievmentsConfig;
    }

    public String getAchievmentsConfigContentType() {
        return achievmentsConfigContentType;
    }

    public void setAchievmentsConfigContentType(String achievmentsConfigContentType) {
        this.achievmentsConfigContentType = achievmentsConfigContentType;
    }

    public String getAchievmentsConfigFileName() {
        return achievmentsConfigFileName;
    }

    public void setAchievmentsConfigFileName(String achievmentsConfigFileName) {
        this.achievmentsConfigFileName = achievmentsConfigFileName;
    }    
}
