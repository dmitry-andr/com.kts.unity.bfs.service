package com.kts.unity.config.gamification;

public enum ChallengeTypes {
    WEEK("week"), DAY("day"), SNIPER_OF_WEEK("sniperOfWeek"), TIME_PERIOD_TOURNAMENT("tournamentForTimePeriod");
    
    private final String value;
    
    private ChallengeTypes(String val){
        this.value = val;        
    }

    public String getValue() {
        return value;
    }
}
