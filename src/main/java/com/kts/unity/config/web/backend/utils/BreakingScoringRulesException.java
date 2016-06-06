package com.kts.unity.config.web.backend.utils;

public class BreakingScoringRulesException extends Exception{
    
    private String errMessage;
    
    public BreakingScoringRulesException(){
        super();
    }
    
    public BreakingScoringRulesException(String message){
        super(message);
        this.errMessage = message;        
    }
    
    public String getError(){
        return this.errMessage;
    }
    
    
    
}
