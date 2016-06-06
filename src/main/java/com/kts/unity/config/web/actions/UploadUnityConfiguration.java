package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import org.springframework.core.io.Resource;


public class UploadUnityConfiguration extends ActionSupport {

    private File rocketAppConfig;
    private String rocketAppConfigContentType;
    private String rocketAppConfigFileName;
    private String message;
    

    @Override
    public String execute() throws Exception {

        if (this.rocketAppConfig != null) {

            if (!rocketAppConfigFileName.equals(ConfigParams.CONFIG_FILE_NAME)) {
                this.message = "!!! File name must be : " + ConfigParams.CONFIG_FILE_NAME;
                return ActionConstants.SUCCESS;
            }

            ConfigManager cfMgr = (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
            
            Resource mobileAppConfigFileRes = AppContext.getContext().getResource(ConfigParams.APP_MOB_CLIENT_CONFIG_FILE_PATH_NAME);
                        
            int status = cfMgr.updateConfigWithFile(this.rocketAppConfig, mobileAppConfigFileRes.getFile().getPath(), 1);
            
            //Status message
            String statusMsg;
            if(status == 1){
                statusMsg = "Success";
            }else{
                statusMsg = "Failed (Check logs for ERROR) !!!";
            }

            this.message = "File uploaded at path : " + mobileAppConfigFileRes.getFile().getPath() + " | with ststaus=" + statusMsg;
            return ActionConstants.SUCCESS;
        } else {
            this.message = "Please select configuration file !";

            return ActionConstants.SUCCESS;
        }
    }


    public File getRocketAppConfig() {
        return rocketAppConfig;
    }

    public void setRocketAppConfig(File rocketAppConfig) {
        this.rocketAppConfig = rocketAppConfig;
    }

    public String getRocketAppConfigContentType() {
        return rocketAppConfigContentType;
    }

    public void setRocketAppConfigContentType(String rocketAppConfigContentType) {
        this.rocketAppConfigContentType = rocketAppConfigContentType;
    }

    public String getRocketAppConfigFileName() {
        return rocketAppConfigFileName;
    }

    public void setRocketAppConfigFileName(String rocketAppConfigFileName) {
        this.rocketAppConfigFileName = rocketAppConfigFileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
