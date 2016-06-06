package com.kts.unity.config.web.entities.configs;

public class BotsConfiguration {
    
    private static BotsConfiguration instance = null;
    
    private String description;    
    private String xmlOutput;
    
    private BotsConfiguration(){        
    }
    
    public static BotsConfiguration getInstance(){
        if(instance != null){
            return instance;
        }else{
            instance = new BotsConfiguration();
            return instance;
        }
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getXmlOutput() {
        return xmlOutput;
    }

    public void setXmlOutput(String xmlOutput) {
        this.xmlOutput = xmlOutput;
    }
}
