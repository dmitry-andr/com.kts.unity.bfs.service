package com.kts.unity.config.web.entities.player;

public class PlayerGameProfile {

    private int profileId;
    private String countryCode;
    private int shipType;
    private int weaponId;
    private int rank;
    private int scores;
    private String puchasedItemsList;
    //private int numberOfShopVisits;
    private int experiencePoints;
    private String achievementsList;
    private String avatar;
    private int coinsBalance;
    private int notifyAboutOnlinePlayers;
    

    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public int getProfileId() {
        return profileId;
    }
    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public int getScores() {
        return scores;
    }
    public void setScores(int scores) {
        this.scores = scores;
    }
    public int getShipType() {
        return shipType;
    }
    public void setShipType(int shipType) {
        this.shipType = shipType;
    }
    public int getWeaponId() {
        return weaponId;
    }
    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }
    public String getPuchasedItemsList() {
        return puchasedItemsList;
    }
    public void setPuchasedItemsList(String puchasedItemsList) {
        this.puchasedItemsList = puchasedItemsList;
    }
    /*
    public int getNumberOfShopVisits() {
        return numberOfShopVisits;
    }
    public void setNumberOfShopVisits(int numberOfShopVisits) {
        this.numberOfShopVisits = numberOfShopVisits;
    }
     */
    public int getExperiencePoints() {
        return experiencePoints;
    }
    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public String getAchievementsList() {
        return achievementsList;
    }
    public void setAchievementsList(String achievementsList) {
        this.achievementsList = achievementsList;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public int getCoinsBalance() {
        return coinsBalance;
    }
    public void setCoinsBalance(int coinsBalance) {
        this.coinsBalance = coinsBalance;
    }

    public int getNotifyAboutOnlinePlayers() {
        return notifyAboutOnlinePlayers;
    }

    public void setNotifyAboutOnlinePlayers(int notifyAboutOnlinePlayers) {
        this.notifyAboutOnlinePlayers = notifyAboutOnlinePlayers;
    }
    
    
        
    
    
    @Override
    public String toString() {
        
        StringBuilder out = new StringBuilder();
        
        out.append("Player Game Profile :");
        out.append(this.profileId);
        out.append(this.shipType);
        out.append(this.countryCode);
        out.append(this.rank);
        out.append(this.scores);
        
        return out.toString();
    }
    
    
    
    
}
