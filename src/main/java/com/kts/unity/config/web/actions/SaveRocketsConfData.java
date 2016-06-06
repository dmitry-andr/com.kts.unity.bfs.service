package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.entities.configs.RocketConfigEntry;
import com.kts.unity.config.web.entities.configs.RocketsConfiguration;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.core.io.Resource;

public class SaveRocketsConfData extends ActionSupport {

    private RocketsConfiguration config;
    private String message;
    

    @Override
    public String execute() throws Exception{

        this.message = "Save method under construction !!!";

        ArrayList<RocketConfigEntry> entriesTmp = new ArrayList<RocketConfigEntry>();

        try {
            Map parameters = ActionContext.getContext().getParameters();
            Set entries = parameters.entrySet();
            Iterator iter = entries.iterator();
            
            int counter = 0;
            while (iter.hasNext()) {
                iter.next();

                Object idObj = parameters.get("id_value_" + counter);
                Object nameObj = parameters.get("name_value_" + counter);
                Object shipTypeObj = parameters.get("shipType_value_" + counter);
                Object thrustObj = parameters.get("thrust_value_" + counter);
                Object rotationSpeedObj = parameters.get("rotationSpeed_value_" + counter);
                Object massObj = parameters.get("mass_value_" + counter);
                Object dragObj = parameters.get("drag_value_" + counter);
                Object angularDragObj = parameters.get("angularDrag_value_" + counter);
                Object cageLimitObj = parameters.get("cageLimit_value_" + counter);
                Object hitPointsObj = parameters.get("hitPoints_value_" + counter);
                Object reloadTimeObj = parameters.get("reloadTime_value_" + counter);
                Object beamVelocityObj = parameters.get("beamVelocity_value_" + counter);
                Object fuseTimerObj = parameters.get("fuseTimer_value_" + counter);
                Object rankObj = parameters.get("rank_value_" + counter);
                Object priceCoinsObj = parameters.get("priceCoins_value_" + counter);
                
                if (idObj != null) {
                    RocketConfigEntry rocketConfEntryTmp = new RocketConfigEntry();
                    rocketConfEntryTmp.setId(((String[]) idObj)[0]);
                    rocketConfEntryTmp.setName(((String[]) nameObj)[0]);
                    rocketConfEntryTmp.setShipType(((String[]) shipTypeObj)[0]);
                    rocketConfEntryTmp.setThrust(((String[]) thrustObj)[0]);
                    rocketConfEntryTmp.setRotationSpeed(((String[]) rotationSpeedObj)[0]);
                    rocketConfEntryTmp.setMass(((String[]) massObj)[0]);
                    rocketConfEntryTmp.setDrag(((String[]) dragObj)[0]);
                    rocketConfEntryTmp.setAngularDrag(((String[]) angularDragObj)[0]);
                    rocketConfEntryTmp.setCageLimit(((String[]) cageLimitObj)[0]);
                    rocketConfEntryTmp.setHitPoints(((String[]) hitPointsObj)[0]);
                    rocketConfEntryTmp.setReloadTime(((String[]) reloadTimeObj)[0]);
                    rocketConfEntryTmp.setBeamVelocity(((String[]) beamVelocityObj)[0]);
                    rocketConfEntryTmp.setFuseTimer(((String[]) fuseTimerObj)[0]);
                    rocketConfEntryTmp.setRank(((String[]) rankObj)[0]);
                    rocketConfEntryTmp.setPriceCoins(((String[]) priceCoinsObj)[0]);                    
                   
                    entriesTmp.add(rocketConfEntryTmp);

                    counter++;
                } else {
                    break;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if(entriesTmp.size() == RocketsConfiguration.getInstance().getEntries().size()){
            
            RocketsConfiguration.getInstance().setEntries(entriesTmp);
            RocketsConfiguration.getInstance().updateXmlOutput();
            
            int saveFileStatus = 0;
            
            ConfigManager cfMgr = (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
            
            Resource rocketsConfigFileRes = AppContext.getContext().getResource(ConfigParams.APP_ROCKETS_CONFIG_FILE_PATH_NAME);
                        
            
            saveFileStatus = cfMgr.updateRocketsConfigFile(RocketsConfiguration.getInstance(), rocketsConfigFileRes.getFile().getPath());
            if(saveFileStatus == 1){
                this.message = "Configuration updated and saved in file.";
            }else{
                this.message = "Error while saving configuration file !!!";
            }
            
        }

        this.config = RocketsConfiguration.getInstance();

        return ActionConstants.SUCCESS;
    }

    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RocketsConfiguration getConfig() {
        return config;
    }

    public void setConfig(RocketsConfiguration config) {
        this.config = config;
    }
}
