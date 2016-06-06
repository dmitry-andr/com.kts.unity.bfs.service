package com.kts.unity.config.web.entities.configs;

public class ConfigEntry {
    private String name;
    private String value;
    private String description;
    private String displayName;

    public ConfigEntry(String name, String value, String displayName) {
        this.name = name;
        this.value = value;
        this.displayName = displayName;
    }

    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(this.displayName + " ; " + this.name + " : " + this.value);        
        return out.toString();
    }
        
}
