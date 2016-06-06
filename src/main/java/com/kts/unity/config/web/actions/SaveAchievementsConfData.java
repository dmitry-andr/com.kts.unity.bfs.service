package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.entities.configs.Achievement;
import com.kts.unity.config.web.entities.configs.AchievementsConfiguration;
import com.kts.unity.config.web.entities.configs.RocketConfigEntry;
import com.kts.unity.config.web.entities.configs.RocketsConfiguration;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.core.io.Resource;



public class SaveAchievementsConfData extends ActionSupport{
    
    private AchievementsConfiguration config;
    private String message;
    
    
    @Override
    public String execute() throws IOException{
        
        this.message = "Save method under construction !!!";
        
        ArrayList<Achievement> entriesTmp = new ArrayList<Achievement>();
        
        try {
            Map parameters = ActionContext.getContext().getParameters();
            Set entries = parameters.entrySet();
            Iterator iter = entries.iterator();
            
            int counter = 0;
            while (iter.hasNext()) {
                iter.next();

                Object idObj = parameters.get("id_value_" + counter);
                Object nameObj = parameters.get("name_value_" + counter);
                Object descrObj = parameters.get("achdescription_value_" + counter);
                Object iconObj = parameters.get("icon_value_" + counter);
                Object pointsObj = parameters.get("points_value_" + counter);
                Object listOrderObj = parameters.get("list_order_value_" + counter);
                Object awrdCoinsObj = parameters.get("awrdcoins_order_value_" + counter);
                Object stateObj = parameters.get("state_value_" + counter);
                
                
                if (idObj != null) {
                    Achievement achConfEntryTmp = new Achievement();
                    achConfEntryTmp.setId(((String[]) idObj)[0]);
                    achConfEntryTmp.setName(((String[]) nameObj)[0]);
                    achConfEntryTmp.setDescription(((String[]) descrObj)[0]);
                    achConfEntryTmp.setIcon(((String[]) iconObj)[0]);
                    achConfEntryTmp.setPoints(Integer.parseInt(((String[]) pointsObj)[0]));
                    achConfEntryTmp.setListOrder(Integer.parseInt(((String[]) listOrderObj)[0]));
                    achConfEntryTmp.setAwardingCoins(Integer.parseInt(((String[]) awrdCoinsObj)[0]));
                    achConfEntryTmp.setState(((String[]) stateObj)[0]);
                    
                    
                    System.out.println("Ach entry to be added to array : " + achConfEntryTmp.toString());
                   
                    entriesTmp.add(achConfEntryTmp);

                    counter++;
                } else {
                    break;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        if(entriesTmp.size() == AchievementsConfiguration.getInstance().getEntries().size()){
            
            AchievementsConfiguration.getInstance().setEntries(entriesTmp);
            AchievementsConfiguration.getInstance().updateXmlOutput();
            
            int saveFileStatus = 0;
            
            ConfigManager cfMgr = (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
            
            Resource achievementsConfigFileRes = AppContext.getContext().getResource(ConfigParams.APP_ACHIEVEMENTS_CONFIG_FILE_PATH_NAME);
                        
            
            saveFileStatus = cfMgr.updateAchievementsConfigFile(AchievementsConfiguration.getInstance(), achievementsConfigFileRes.getFile().getPath());
            if(saveFileStatus == 1){
                this.message = "Achievments configuration updated and saved in file.";
            }else{
                this.message = "Error while saving configuration file !!!";
            }
            
        }
        
        
        
        
        this.config = AchievementsConfiguration.getInstance();
        return ActionConstants.SUCCESS;
    }
    
    
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AchievementsConfiguration getConfig() {
        return config;
    }

    public void setConfig(AchievementsConfiguration config) {
        this.config = config;
    }
    
}
