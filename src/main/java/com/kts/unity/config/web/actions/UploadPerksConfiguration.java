package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import org.springframework.core.io.Resource;



public class UploadPerksConfiguration extends ActionSupport{
    
    private File perksConfig;
    private String perksConfigContentType;
    private String perksConfigFileName;
    private String message;
    
    @Override
    public String execute() throws Exception {
            
        if(this.perksConfig != null){
            
            if(!perksConfigFileName.equals(ConfigParams.PERKS_CONFIG_FILE_NAME)){
                this.message = "!!! File name must be : " + ConfigParams.PERKS_CONFIG_FILE_NAME;
                return ActionConstants.SUCCESS;
            }            

            ConfigManager cfMgr = (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
            
            Resource perksConfigFileRes = AppContext.getContext().getResource(ConfigParams.APP_MOB_PERKS_CONFIG_FILE_PATH_NAME);
            
            int status = cfMgr.updatePerksConfigsWithFile(this.perksConfig, perksConfigFileRes.getFile().getPath(), 1);
            
            
            this.message = "Perks config uploaded at path : " + perksConfigFileRes.getFile().getPath() + " with ststaus = " + status;
                        
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

    public File getPerksConfig() {
        return perksConfig;
    }

    public void setPerksConfig(File perksConfig) {
        this.perksConfig = perksConfig;
    }

    public String getPerksConfigContentType() {
        return perksConfigContentType;
    }

    public void setPerksConfigContentType(String perksConfigContentType) {
        this.perksConfigContentType = perksConfigContentType;
    }

    public String getPerksConfigFileName() {
        return perksConfigFileName;
    }

    public void setPerksConfigFileName(String perksConfigFileName) {
        this.perksConfigFileName = perksConfigFileName;
    }

    
}
