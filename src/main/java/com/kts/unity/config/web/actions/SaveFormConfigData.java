package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.entities.configs.Configuration;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.core.io.Resource;

public class SaveFormConfigData extends ActionSupport {

    private Configuration config = null;
    private String message;
    
    @Override
    public String execute() throws Exception {
        
        Map parameters = ActionContext.getContext().getParameters();
        Set entries = parameters.entrySet();
        
        Iterator iter = entries.iterator();
        System.out.println("Parameters entries : ");
        int counter = 0;
        while(iter.hasNext()){
            iter.next();
            String entryKey = "entry_value_" + counter;
            Object entryObj = parameters.get(entryKey);
            if(entryObj != null){
                String entryVal = ((String[])entryObj)[0];
                Configuration.getInstance().getEntries().get(counter).setValue(entryVal);
                counter++;
                System.out.println("Value for key : " + entryKey + " is " + entryVal);
            }else{
                break;
            }
        }
                
        this.config = Configuration.getInstance();

        
        Configuration.getInstance().updateXmlOutput();
        
        ConfigManager cfMgr = (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
        Resource mobileAppConfigFileRes = AppContext.getContext().getResource(ConfigParams.APP_MOB_CLIENT_CONFIG_FILE_PATH_NAME);
        int fileUpdateStatus = cfMgr.updateConfigFile(config, mobileAppConfigFileRes.getFile().getPath());

        this.message = "Successfuly updated values. File update status : " + fileUpdateStatus;

        return ActionConstants.SUCCESS;
    }

    
   
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }
}
