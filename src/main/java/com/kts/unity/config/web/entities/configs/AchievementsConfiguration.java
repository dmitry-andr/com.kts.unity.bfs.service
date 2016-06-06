package com.kts.unity.config.web.entities.configs;

import com.kts.unity.config.web.backend.utils.AchievementTags;
import java.util.ArrayList;

public class AchievementsConfiguration {
    private static AchievementsConfiguration instance;
    private String description;
    private ArrayList<Achievement> entries = new ArrayList<Achievement>();
    private boolean initialized = false;
    private String xmlOutput;
    
    private AchievementsConfiguration(){        
    }
    
    public static AchievementsConfiguration getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new AchievementsConfiguration();
            return instance;
        }
    }
    
    public Achievement getAchievementForId(String achId){
        for(int k = 0; k < this.entries.size(); k++){
            if(this.entries.get(k).getId().equals(achId)){
                return this.entries.get(k);
            }            
        }
        return null;
    }
    
    

    public int updateXmlOutput() {
        int status = 0;


        StringBuilder configXmlOutput = new StringBuilder();
        configXmlOutput.append("<" + AchievementTags.ROOT_TAG + ">");
        configXmlOutput.append("\n");
        configXmlOutput.append("<" + AchievementTags.DESCRIPTION + ">");
        configXmlOutput.append(this.getDescription());
        configXmlOutput.append("</" + AchievementTags.DESCRIPTION + ">");
        configXmlOutput.append("\n");

        for (int k = 0; k < this.getEntries().size(); k++) {
            configXmlOutput.append("<" + AchievementTags.ENTRY + (k + 1) + ">");


            configXmlOutput.append("\n");
            configXmlOutput.append("<" + AchievementTags.ID + ">");
            configXmlOutput.append(this.getEntries().get(k).getId());
            configXmlOutput.append("</" + AchievementTags.ID + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + AchievementTags.NAME + ">");
            configXmlOutput.append(this.getEntries().get(k).getName());
            configXmlOutput.append("</" + AchievementTags.NAME + ">");
            configXmlOutput.append("\n");
            
            configXmlOutput.append("<" + AchievementTags.ACH_DESCRIPTION + ">");
            configXmlOutput.append(this.getEntries().get(k).getDescription());
            configXmlOutput.append("</" + AchievementTags.ACH_DESCRIPTION + ">");
            configXmlOutput.append("\n");
            
            configXmlOutput.append("<" + AchievementTags.ICON + ">");
            configXmlOutput.append(this.getEntries().get(k).getIcon());
            configXmlOutput.append("</" + AchievementTags.ICON + ">");
            configXmlOutput.append("\n");
            
            configXmlOutput.append("<" + AchievementTags.POINTS + ">");
            configXmlOutput.append(this.getEntries().get(k).getPoints());
            configXmlOutput.append("</" + AchievementTags.POINTS + ">");
            configXmlOutput.append("\n");
            
            configXmlOutput.append("<" + AchievementTags.LIST_ORDER + ">");
            configXmlOutput.append(this.getEntries().get(k).getListOrder());
            configXmlOutput.append("</" + AchievementTags.LIST_ORDER + ">");
            configXmlOutput.append("\n");
            
            configXmlOutput.append("<" + AchievementTags.AWARDING_COINS + ">");
            configXmlOutput.append(this.getEntries().get(k).getAwardingCoins());
            configXmlOutput.append("</" + AchievementTags.AWARDING_COINS + ">");
            configXmlOutput.append("\n");
            
            configXmlOutput.append("<" + AchievementTags.STATE + ">");
            configXmlOutput.append(this.getEntries().get(k).getState());
            configXmlOutput.append("</" + AchievementTags.STATE + ">");
            configXmlOutput.append("\n");
            

            configXmlOutput.append("</" + AchievementTags.ENTRY + (k + 1) + ">");
            configXmlOutput.append("\n");
        }

        configXmlOutput.append("</" + AchievementTags.ROOT_TAG + ">");
        this.xmlOutput = configXmlOutput.toString();
        status = 1;

        return status;
    }    
    
    
    
    public ArrayList<Achievement> getEntries() {
        return entries;
    }
    public void setEntries(ArrayList<Achievement> entries) {
        this.entries = entries;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
    public String getXmlOutput() {
        return xmlOutput;
    }   
    
}
