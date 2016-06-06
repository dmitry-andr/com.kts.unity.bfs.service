package com.kts.unity.config.web.entities.configs;

public class AvatarEntry {
    
    private String id;
    private String name;
    private String url;
    private String expoints;

    public String getExpoints() {
        return expoints;
    }
    public void setExpoints(String expoints) {
        this.expoints = expoints;
    }
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
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    
}
