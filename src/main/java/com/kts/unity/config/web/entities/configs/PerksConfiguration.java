package com.kts.unity.config.web.entities.configs;

import com.kts.unity.config.web.entities.configs.PerkEntry;
import com.kts.unity.config.web.backend.utils.PerksTags;
import java.util.ArrayList;

public class PerksConfiguration {
    
    private static PerksConfiguration instance = null;
    
    private String description;
    private ArrayList<PerkEntry> entries = new ArrayList<PerkEntry>();
    private boolean initialized = false;
    private String xmlOutput;
    

    private PerksConfiguration() {
    }
    
    public static PerksConfiguration getInstance(){
        if(instance != null){
            return instance;
        }else{
            instance = new PerksConfiguration();
            return instance;
        }
    }

    public PerkEntry perkEntryForId(String perkId){
        for(int k = 0; k < this.entries.size(); k++){
            if(this.entries.get(k).getId().equals(perkId)){
                return this.entries.get(k);
            }
        }
        return null;
    }
    
    public int updateXmlOutput() {
        int status = 0;

        StringBuilder configXmlOutput = new StringBuilder();
        configXmlOutput.append("<" + PerksTags.ROOT_TAG + ">");
        configXmlOutput.append("\n");
        configXmlOutput.append("<" + PerksTags.DESCRIPTION + ">");
        configXmlOutput.append(this.getDescription());
        configXmlOutput.append("</" + PerksTags.DESCRIPTION + ">");
        configXmlOutput.append("\n");

        for (int k = 0; k < this.getEntries().size(); k++) {
            configXmlOutput.append("<" + PerksTags.PERK_ENTRY + (k + 1) + ">");


            configXmlOutput.append("\n");
            configXmlOutput.append("<" + PerksTags.ID + ">");
            configXmlOutput.append(this.getEntries().get(k).getId());
            configXmlOutput.append("</" + PerksTags.ID + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + PerksTags.NAME + ">");
            configXmlOutput.append(this.getEntries().get(k).getName());
            configXmlOutput.append("</" + PerksTags.NAME + ">");
            configXmlOutput.append("\n");
            
            configXmlOutput.append("<" + PerksTags.DESCRIPTION + ">");
            configXmlOutput.append(this.getEntries().get(k).getDescription());
            configXmlOutput.append("</" + PerksTags.DESCRIPTION + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + PerksTags.MIN_RANK + ">");
            configXmlOutput.append(this.getEntries().get(k).getMinrank());
            configXmlOutput.append("</" + PerksTags.MIN_RANK + ">");
            configXmlOutput.append("\n");
 
            configXmlOutput.append("<" + PerksTags.PRICE_COINS + ">");
            configXmlOutput.append(this.getEntries().get(k).getPriceCoins());
            configXmlOutput.append("</" + PerksTags.PRICE_COINS + ">");
            configXmlOutput.append("\n");            


            configXmlOutput.append("</" + PerksTags.PERK_ENTRY + (k + 1) + ">");
            configXmlOutput.append("\n");
        }

        configXmlOutput.append("</" + PerksTags.ROOT_TAG + ">");
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
    public ArrayList<PerkEntry> getEntries() {
        return entries;
    }
    public void setEntries(ArrayList<PerkEntry> entries) {
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
