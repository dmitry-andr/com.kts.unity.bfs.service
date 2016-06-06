package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.entities.configs.RocketsConfiguration;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import org.springframework.core.io.Resource;

public class UploadRocketsConfiguration extends ActionSupport{

    private File rocketsConfig;
    private String rocketsConfigContentType;
    private String rocketsConfigFileName;
    private String message;    
    
    
    @Override
    public String execute() throws Exception {
        
        if(this.rocketsConfig != null){
            
            if(!rocketsConfigFileName.equals(ConfigParams.ROCKETS_CONFIG_FILE_NAME)){
                this.message = "!!! File name must be : " + ConfigParams.ROCKETS_CONFIG_FILE_NAME;
                return ActionConstants.SUCCESS;
            }            

            ConfigManager cfMgr = (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
            
            Resource rocketsConfigFileRes = AppContext.getContext().getResource(ConfigParams.APP_ROCKETS_CONFIG_FILE_PATH_NAME);
            
            int status = cfMgr.updateRocketsConfigsWithFile(this.rocketsConfig, rocketsConfigFileRes.getFile().getPath(), 1);
                        
            this.message = "Rockets config uploaded at path : " + rocketsConfigFileRes.getFile().getPath() + " with ststaus = " + status;
            
            System.out.println("Rockets !!!!!" + RocketsConfiguration.getInstance().toString());
            
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

    public File getRocketsConfig() {
        return rocketsConfig;
    }

    public void setRocketsConfig(File rocketsConfig) {
        this.rocketsConfig = rocketsConfig;
    }

    public String getRocketsConfigContentType() {
        return rocketsConfigContentType;
    }

    public void setRocketsConfigContentType(String rocketsConfigContentType) {
        this.rocketsConfigContentType = rocketsConfigContentType;
    }

    public String getRocketsConfigFileName() {
        return rocketsConfigFileName;
    }

    public void setRocketsConfigFileName(String rocketsConfigFileName) {
        this.rocketsConfigFileName = rocketsConfigFileName;
    }
    
}
