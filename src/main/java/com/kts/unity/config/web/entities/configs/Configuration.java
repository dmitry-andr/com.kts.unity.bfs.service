package com.kts.unity.config.web.entities.configs;

import com.kts.unity.config.web.backend.utils.AppConfigurationTags;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    public static final String SHARP_SHOOTING_CONST = "sharpshootingConstMultiplyer";
    public static final String DAMAGE_CONST = "damageConstMultiplyer";
    public static final String SCORES_CONST_COEFFICENT = "scoresConstMultiplyer";
    
    private static Configuration configInstance = null;
    private String description;
    private ArrayList<ConfigEntry> entries = new ArrayList<ConfigEntry>();
    private Map<String, ConfigEntry> entriesMap = new HashMap<String, ConfigEntry>();
    private boolean initialized = false;
    private String xmlOutput;
    
    private String announcementText = null;
    private String announcementDate = null;
    
    private Configuration() {
    }

    public static Configuration getInstance() {
        if (configInstance != null) {
            return configInstance;
        } else {
            configInstance = new Configuration();
            return configInstance;
        }
    }
    
    public ConfigEntry getEntryFromMap(String entryName){        
        ConfigEntry entryInMap = this.entriesMap.get(entryName);
        if(entryInMap != null){
            return entryInMap;
        }else{//iterate through array and try to find necessary entry by its name
            for(int k = 0; k < this.entries.size(); k++){
                ConfigEntry entryInArray = this.entries.get(k);
                if(entryInArray.getName().equals(entryName)){
                    this.entriesMap.put(entryName, entryInArray);
                    return entryInArray;
                }
            }
        }
        return null;
    }

    public int updateXmlOutput() {
        int status = 0;

        StringBuilder configXML = new StringBuilder();
        configXML.append("<");
        configXML.append(AppConfigurationTags.ROOT_TAG);
        configXML.append(">");

        for (int k = 0; k < this.getEntries().size(); k++) {
            ConfigEntry entry = this.getEntries().get(k);
            configXML.append("<");
            configXML.append(entry.getName());
            configXML.append(">");
            
            configXML.append(entry.getValue());
            
            configXML.append("</");
            configXML.append(entry.getName());
            configXML.append(">");
        }

        configXML.append("</");
        configXML.append(AppConfigurationTags.ROOT_TAG);
        configXML.append(">");
        
        this.xmlOutput = configXML.toString();
        
        status = 1;
        
        this.updateAnnouncementFields();
        
        return status;
    }

    
    public void updateAnnouncementFields(){
        String announcementText = this.xmlOutput.split("<specMessage>")[1];
        announcementText = announcementText.split("</specMessage>")[0];
        this.announcementText = announcementText;
        
        String announcementDate = this.xmlOutput.split("<specMessageTime>")[1];
        announcementDate = announcementDate.split("</specMessageTime>")[0];
        this.announcementDate = announcementDate;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ConfigEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<ConfigEntry> entries) {
        this.entries = entries;
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

    public String getAnnouncementDate() {
        return announcementDate;
    }

    public String getAnnouncementText() {
        return announcementText;
    }
    
    

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Configuration\n");
        out.append("Description : ");
        out.append(this.description);
        for (int k = 0; k < this.entries.size(); k++) {
            out.append((k + 1) + " Config entry\n" + this.entries.get(k).toString() + "\n");
        }
        out.append("\n******************");

        return out.toString();
    }
}
