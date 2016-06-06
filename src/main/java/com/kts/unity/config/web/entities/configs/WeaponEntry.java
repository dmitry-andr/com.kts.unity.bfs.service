package com.kts.unity.config.web.entities.configs;

public class WeaponEntry {

    private String id;
    private String name;
    private String description;
    private String minrank;
    private String priceCoins;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMinrank() {
        return minrank;
    }
    public void setMinrank(String minrank) {
        this.minrank = minrank;
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
    public String getPriceCoins() {
        return priceCoins;
    }
    public void setPriceCoins(String pricecents) {
        this.priceCoins = pricecents;
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        
        out.append("id : " + this.id + " ; ");
        out.append("name : " + this.name + " ; ");
        out.append("price : " + this.priceCoins);
                
        return out.toString();
    }    
        
}
