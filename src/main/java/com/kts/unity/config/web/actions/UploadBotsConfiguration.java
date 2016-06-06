package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import org.springframework.core.io.Resource;

public class UploadBotsConfiguration extends ActionSupport{
    
    private File botsConfig;
    private String botsConfigContentType;
    private String botsConfigFileName;
    private String message;
        
    @Override
    public String execute() throws Exception {
        
        if(this.botsConfig != null){
            
            if(!botsConfigFileName.equals(ConfigParams.BOTS_CONFIG_FILE_NAME)){
                this.message = "!!! File name must be : " + ConfigParams.BOTS_CONFIG_FILE_NAME;
                return ActionConstants.SUCCESS;
            }            

            ConfigManager cfMgr = (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
            
            Resource botConfigFileRes = AppContext.getContext().getResource(ConfigParams.APP_BOTS_CONFIG_FILE_PATH_NAME);
            
            int status = cfMgr.updateBotsConfigsWithFile(this.botsConfig, botConfigFileRes.getFile().getPath(), 1);
                        
            this.message = "Bots config uploaded at path : " + botConfigFileRes.getFile().getPath() + " with ststaus = " + status;
            
            return ActionConstants.SUCCESS;
        }else{
            this.message = "Please select configuration file !";
            return ActionConstants.SUCCESS;
        }
    }

    
    public File getBotsConfig() {
        return botsConfig;
    }
    public void setBotsConfig(File botsConfig) {
        this.botsConfig = botsConfig;
    }
    public String getBotsConfigContentType() {
        return botsConfigContentType;
    }
    public void setBotsConfigContentType(String botsConfigContentType) {
        this.botsConfigContentType = botsConfigContentType;
    }
    public String getBotsConfigFileName() {
        return botsConfigFileName;
    }
    public void setBotsConfigFileName(String botsConfigFileName) {
        this.botsConfigFileName = botsConfigFileName;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
