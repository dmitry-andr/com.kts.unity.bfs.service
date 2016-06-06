package com.kts.unity.config.web.entities.player;

import com.kts.unity.config.web.utils.ConfigParams;


public class PlayerGameStatistics {
    private int playerId;
    private int[] killedbyrank = new int[ConfigParams.NUMBER_OF_RANKS];
    private int[] killedrank = new int [ConfigParams.NUMBER_OF_RANKS];
    
    
    public PlayerGameStatistics(int playerId) {
        this.playerId = playerId;
    }

    
    public int getPlayerId() {
        return playerId;
    }

    public int[] getKilledbyrank() {
        return killedbyrank;
    }

    public void setKilledbyrank(int[] killedbyrank) {
        this.killedbyrank = killedbyrank;
    }

    public int[] getKilledrank() {
        return killedrank;
    }

    public void setKilledrank(int[] killedrank) {
        this.killedrank = killedrank;
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("Player game statistics : \n");
        out.append(this.playerId + "\n");
        out.append(this.killedbyrank.toString() + " ; " + this.killedrank.toString());
        
        return out.toString();
    }
    
    
    

}
