package com.kts.unity.config.web.entities.configs;

public class Achievement {

    private String id;
    private String name;//Up to 100 characters
    private String description;//Up to 5000 characters
    private String icon;
    private int points;//this must be a multiple of 5 - this option for incremental achievements
    private int listOrder;
    private int awardingCoins;
    private String state;
    
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public int getListOrder() {
        return listOrder;
    }
    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public int getAwardingCoins() {
        return awardingCoins;
    }
    public void setAwardingCoins(int awardingCoins) {
        this.awardingCoins = awardingCoins;
    }
    
    

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("id:" + this.id);
        out.append(";name:" + this.name);
        
        return out.toString();
    }
    
    
}
