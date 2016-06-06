package com.kts.unity.config.web.entities;

import java.util.Date;

public class GameNavigationStatistics {

    private String applicationId;
    private String playerName;
    private Date dataCollectionDate;
    private String dataCollectionTime;
    private int numOfHangarVisits;
    private int numOfCoinsButtonPress;
    private int fbShareAcheivement;
    private int tournamentsButton;
    private int levelSquare;
    private int levelTriangle;
    private int levelHex;
    private int fbFriends;
    private int musicOffBtn;
    
    private int coins100;
    private int coins250;
    private int coins550;
    private int training;
    private int buy;
    private int avatar;
    private int ship;
    private int weapon;
    private int perks;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Date getDataCollectionDate() {
        return dataCollectionDate;
    }

    public void setDataCollectionDate(Date dataCollectionDate) {
        this.dataCollectionDate = dataCollectionDate;
    }

    public String getDataCollectionTime() {
        return dataCollectionTime;
    }

    public void setDataCollectionTime(String dataCollectionTime) {
        this.dataCollectionTime = dataCollectionTime;
    }

    public int getFbShareAcheivement() {
        return fbShareAcheivement;
    }

    public void setFbShareAcheivement(int fbShareAcheivement) {
        this.fbShareAcheivement = fbShareAcheivement;
    }

    public int getLevelHex() {
        return levelHex;
    }

    public void setLevelHex(int levelHex) {
        this.levelHex = levelHex;
    }

    public int getLevelSquare() {
        return levelSquare;
    }

    public void setLevelSquare(int levelSquare) {
        this.levelSquare = levelSquare;
    }

    public int getLevelTriangle() {
        return levelTriangle;
    }

    public void setLevelTriangle(int levelTriangle) {
        this.levelTriangle = levelTriangle;
    }

    public int getNumOfCoinsButtonPress() {
        return numOfCoinsButtonPress;
    }

    public void setNumOfCoinsButtonPress(int numOfCoinsButtonPress) {
        this.numOfCoinsButtonPress = numOfCoinsButtonPress;
    }

    public int getNumOfHangarVisits() {
        return numOfHangarVisits;
    }

    public void setNumOfHangarVisits(int numOfHangarVisits) {
        this.numOfHangarVisits = numOfHangarVisits;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getTournamentsButton() {
        return tournamentsButton;
    }

    public void setTournamentsButton(int tournamentsButton) {
        this.tournamentsButton = tournamentsButton;
    }

    public int getFbFriends() {
        return fbFriends;
    }

    public void setFbFriends(int fbFriends) {
        this.fbFriends = fbFriends;
    }

    public int getMusicOffBtn() {
        return musicOffBtn;
    }

    public void setMusicOffBtn(int musicOffBtn) {
        this.musicOffBtn = musicOffBtn;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getCoins100() {
        return coins100;
    }

    public void setCoins100(int coins100) {
        this.coins100 = coins100;
    }

    public int getCoins250() {
        return coins250;
    }

    public void setCoins250(int coins250) {
        this.coins250 = coins250;
    }

    public int getCoins550() {
        return coins550;
    }

    public void setCoins550(int coins550) {
        this.coins550 = coins550;
    }

    public int getPerks() {
        return perks;
    }

    public void setPerks(int perks) {
        this.perks = perks;
    }

    public int getShip() {
        return ship;
    }

    public void setShip(int ship) {
        this.ship = ship;
    }

    public int getTraining() {
        return training;
    }

    public void setTraining(int training) {
        this.training = training;
    }

    public int getWeapon() {
        return weapon;
    }

    public void setWeapon(int weapon) {
        this.weapon = weapon;
    }
    
}
