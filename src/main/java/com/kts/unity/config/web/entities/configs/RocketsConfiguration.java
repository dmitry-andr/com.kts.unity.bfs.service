package com.kts.unity.config.web.entities.configs;

import com.kts.unity.config.web.backend.utils.RocketsTags;
import java.util.ArrayList;

public class RocketsConfiguration {

    private static RocketsConfiguration instance;
    private String description;
    private ArrayList<RocketConfigEntry> entries = new ArrayList<RocketConfigEntry>();
    private boolean initialized = false;
    private String xmlOutput;

    private RocketsConfiguration() {
    }

    public static RocketsConfiguration getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new RocketsConfiguration();
            return instance;
        }
    }

    public RocketConfigEntry rocketConfigForRocketId(String rocketId){
        for(int k = 0; k < this.entries.size(); k++){
            if(this.entries.get(k).getId().equals(rocketId)){
                return this.entries.get(k);
            }
        }
        return null;
    }
    
    
    public int updateXmlOutput() {
        int status = 0;


        //RocketsConfiguration configuration = RocketsConfiguration.getInstance();

        StringBuilder configXmlOutput = new StringBuilder();
        configXmlOutput.append("<" + RocketsTags.ROOT_TAG + ">");
        configXmlOutput.append("\n");
        configXmlOutput.append("<" + RocketsTags.DESCRIPTION + ">");
        configXmlOutput.append(this.getDescription());
        configXmlOutput.append("</" + RocketsTags.DESCRIPTION + ">");
        configXmlOutput.append("\n");

        for (int k = 0; k < this.getEntries().size(); k++) {
            configXmlOutput.append("<" + RocketsTags.ROCKET_ENTRY + (k + 1) + ">");


            configXmlOutput.append("\n");
            configXmlOutput.append("<" + RocketsTags.ID + ">");
            configXmlOutput.append(this.getEntries().get(k).getId());
            configXmlOutput.append("</" + RocketsTags.ID + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.NAME + ">");
            configXmlOutput.append(this.getEntries().get(k).getName());
            configXmlOutput.append("</" + RocketsTags.NAME + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.SHIP_TYPE + ">");
            configXmlOutput.append(this.getEntries().get(k).getShipType());
            configXmlOutput.append("</" + RocketsTags.SHIP_TYPE + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.THRUST + ">");
            configXmlOutput.append(this.getEntries().get(k).getThrust());
            configXmlOutput.append("</" + RocketsTags.THRUST + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.ROTATION_SPEED + ">");
            configXmlOutput.append(this.getEntries().get(k).getRotationSpeed());
            configXmlOutput.append("</" + RocketsTags.ROTATION_SPEED + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.MASS + ">");
            configXmlOutput.append(this.getEntries().get(k).getMass());
            configXmlOutput.append("</" + RocketsTags.MASS + ">");
            configXmlOutput.append("\n");


            configXmlOutput.append("<" + RocketsTags.DRAG + ">");
            configXmlOutput.append(this.getEntries().get(k).getDrag());
            configXmlOutput.append("</" + RocketsTags.DRAG + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.ANGULAR_DRAG + ">");
            configXmlOutput.append(this.getEntries().get(k).getAngularDrag());
            configXmlOutput.append("</" + RocketsTags.ANGULAR_DRAG + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.CAGE_LIMIT + ">");
            configXmlOutput.append(this.getEntries().get(k).getCageLimit());
            configXmlOutput.append("</" + RocketsTags.CAGE_LIMIT + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.HIT_POINTS + ">");
            configXmlOutput.append(this.getEntries().get(k).getHitPoints());
            configXmlOutput.append("</" + RocketsTags.HIT_POINTS + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.RELOAD_TIME + ">");
            configXmlOutput.append(this.getEntries().get(k).getReloadTime());
            configXmlOutput.append("</" + RocketsTags.RELOAD_TIME + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.BEAM_VELOCITY + ">");
            configXmlOutput.append(this.getEntries().get(k).getBeamVelocity());
            configXmlOutput.append("</" + RocketsTags.BEAM_VELOCITY + ">");
            configXmlOutput.append("\n");

            configXmlOutput.append("<" + RocketsTags.FUSE_TIMER + ">");
            configXmlOutput.append(this.getEntries().get(k).getFuseTimer());
            configXmlOutput.append("</" + RocketsTags.FUSE_TIMER + ">");
            configXmlOutput.append("\n");
            
            configXmlOutput.append("<" + RocketsTags.RANK + ">");
            configXmlOutput.append(this.getEntries().get(k).getRank());
            configXmlOutput.append("</" + RocketsTags.RANK + ">");
            configXmlOutput.append("\n");
            
            configXmlOutput.append("<" + RocketsTags.PRICE_COINS + ">");
            configXmlOutput.append(this.getEntries().get(k).getPriceCoins());
            configXmlOutput.append("</" + RocketsTags.PRICE_COINS + ">");
            configXmlOutput.append("\n");


            configXmlOutput.append("</" + RocketsTags.ROCKET_ENTRY + (k + 1) + ">");
            configXmlOutput.append("\n");
        }

        configXmlOutput.append("</" + RocketsTags.ROOT_TAG + ">");
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

    public ArrayList<RocketConfigEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<RocketConfigEntry> entries) {
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

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Rockets configurations:\n");
        for (int k = 0; k < this.entries.size(); k++) {
            out.append((k + 1) + " Rocket entry\n" + this.entries.get(k).toString() + "\n");
        }
        out.append("\n******************");

        return out.toString();
    }
}
