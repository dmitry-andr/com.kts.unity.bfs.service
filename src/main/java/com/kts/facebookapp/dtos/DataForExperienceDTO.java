package com.kts.facebookapp.dtos;

public class DataForExperienceDTO {

    int time;
    int killerHits;
    int killerShots;
    float killerDamage;
    int playerHits;
    int playerShots;
    float playerDamage;

    public float getKillerDamage() {
        return killerDamage;
    }

    public void setKillerDamage(float killerDamage) {
        this.killerDamage = killerDamage;
    }

    public int getKillerHits() {
        return killerHits;
    }

    public void setKillerHits(int killerHits) {
        this.killerHits = killerHits;
    }

    public int getKillerShots() {
        return killerShots;
    }

    public void setKillerShots(int killerShots) {
        this.killerShots = killerShots;
    }

    public float getPlayerDamage() {
        return playerDamage;
    }

    public void setPlayerDamage(float playerDamage) {
        this.playerDamage = playerDamage;
    }

    public int getPlayerHits() {
        return playerHits;
    }

    public void setPlayerHits(int playerHits) {
        this.playerHits = playerHits;
    }

    public int getPlayerShots() {
        return playerShots;
    }

    public void setPlayerShots(int playerShots) {
        this.playerShots = playerShots;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    
    
}
