package com.kts.unity.config.web.entities.configs;

import com.kts.unity.config.web.backend.utils.WeaponTags;
import java.util.ArrayList;

public class WeaponConfiguration {
    
private static WeaponConfiguration instance = null;

private String description;
private ArrayList<WeaponEntry> entries = new ArrayList<WeaponEntry>();
private boolean initialized = false;
private String xmlOutput;



    private WeaponConfiguration() {
    }

    public static WeaponConfiguration getInstance(){
        if(instance != null){
            return instance;
        }else{
            instance = new WeaponConfiguration();
            return instance;
        }
    }


    public WeaponEntry weaponEntryForId(int weaponId){
        for(int k = 0; k < this.entries.size(); k++){
            if(Integer.parseInt(this.entries.get(k).getId()) == weaponId){
                return this.entries.get(k);
            }
        }
        return null;
    }
    
    
    public int updateXmlOutput() {
        int status = 0;

        StringBuilder configXmlOutput = new StringBuilder();
        configXmlOutput.append("<" + WeaponTags.ROOT_TAG + ">");
        configXmlOutput.append("\n");
        configXmlOutput.append("<" + WeaponTags.DESCRIPTION + ">");
        configXmlOutput.append(this.getDescription());
        configXmlOutput.append("</" + WeaponTags.DESCRIPTION + ">");
        configXmlOutput.append("\n");

        for (int k = 0; k < this.getEntries().size(); k++) {
            configXmlOutput.append("<" + WeaponTags.WEAPON_ENTRY + (k + 1) + ">");


            configXmlOutput.append("\n");
            configXmlOutput.append("<" + WeaponTags.ID + ">");
            configXmlOutput.append(this.getEntries().get(k).getId());
            configXmlOutput.append("</" + WeaponTags.ID + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + WeaponTags.NAME + ">");
            configXmlOutput.append(this.getEntries().get(k).getName());
            configXmlOutput.append("</" + WeaponTags.NAME + ">");
            configXmlOutput.append("\n");
            
            configXmlOutput.append("<" + WeaponTags.DESCRIPTION + ">");
            configXmlOutput.append(this.getEntries().get(k).getDescription());
            configXmlOutput.append("</" + WeaponTags.DESCRIPTION + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + WeaponTags.MIN_RANK + ">");
            configXmlOutput.append(this.getEntries().get(k).getMinrank());
            configXmlOutput.append("</" + WeaponTags.MIN_RANK + ">");
            configXmlOutput.append("\n");
 
            configXmlOutput.append("<" + WeaponTags.PRICE_COINS + ">");
            configXmlOutput.append(this.getEntries().get(k).getPriceCoins());
            configXmlOutput.append("</" + WeaponTags.PRICE_COINS + ">");
            configXmlOutput.append("\n");            


            configXmlOutput.append("</" + WeaponTags.WEAPON_ENTRY + (k + 1) + ">");
            configXmlOutput.append("\n");
        }

        configXmlOutput.append("</" + WeaponTags.ROOT_TAG + ">");
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
    public ArrayList<WeaponEntry> getEntries() {
        return entries;
    }
    public void setEntries(ArrayList<WeaponEntry> entries) {
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
