package com.kts.unity.config.web.entities.configs;

import com.kts.unity.config.web.backend.utils.AvatarTags;
import java.util.ArrayList;

public class AvatarsConfiguration {

    private static AvatarsConfiguration instance = null;
    private String description;
    private ArrayList<AvatarEntry> entries = new ArrayList<AvatarEntry>();
    private boolean initialized = false;
    private String xmlOutput;

    private AvatarsConfiguration() {
    }

    public static AvatarsConfiguration getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new AvatarsConfiguration();
            return instance;
        }
    }

    public AvatarEntry weaponEntryForId(int weaponId) {
        for (int k = 0; k < this.entries.size(); k++) {
            if (Integer.parseInt(this.entries.get(k).getId()) == weaponId) {
                return this.entries.get(k);
            }
        }
        return null;
    }
    
    public int updateXmlOutput() {
        int status = 0;

        StringBuilder configXmlOutput = new StringBuilder();
        configXmlOutput.append("<" + AvatarTags.ROOT_TAG + ">");
        configXmlOutput.append("\n");
        configXmlOutput.append("<" + AvatarTags.DESCRIPTION + ">");
        configXmlOutput.append(this.getDescription());
        configXmlOutput.append("</" + AvatarTags.DESCRIPTION + ">");
        configXmlOutput.append("\n");

        for (int k = 0; k < this.getEntries().size(); k++) {
            configXmlOutput.append("<" + AvatarTags.ENTRY + (k + 1) + ">");


            configXmlOutput.append("\n");
            configXmlOutput.append("<" + AvatarTags.ID + ">");
            configXmlOutput.append(this.getEntries().get(k).getId());
            configXmlOutput.append("</" + AvatarTags.ID + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + AvatarTags.NAME + ">");
            configXmlOutput.append(this.getEntries().get(k).getName());
            configXmlOutput.append("</" + AvatarTags.NAME + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + AvatarTags.URL + ">");
            configXmlOutput.append(this.getEntries().get(k).getUrl());
            configXmlOutput.append("</" + AvatarTags.URL + ">");
            configXmlOutput.append("\n");
 
            configXmlOutput.append("<" + AvatarTags.EXPERIENCE_POINTS + ">");
            configXmlOutput.append(this.getEntries().get(k).getExpoints());
            configXmlOutput.append("</" + AvatarTags.EXPERIENCE_POINTS + ">");
            configXmlOutput.append("\n");            


            configXmlOutput.append("</" + AvatarTags.ENTRY + (k + 1) + ">");
            configXmlOutput.append("\n");
        }

        configXmlOutput.append("</" + AvatarTags.ROOT_TAG + ">");
        this.xmlOutput = configXmlOutput.toString();
        status = 1;

        return status;
    }    

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ArrayList<AvatarEntry> getEntries() {
        return entries;
    }
    public void setEntries(ArrayList<AvatarEntry> entries) {
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


    
    
    
    
}
