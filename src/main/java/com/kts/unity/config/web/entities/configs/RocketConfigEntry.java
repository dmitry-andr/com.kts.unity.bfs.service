package com.kts.unity.config.web.entities.configs;

public class RocketConfigEntry {
    private String id;
    private String name;
    private String shipType;
    private String thrust;
    private String rotationSpeed;
    private String mass;
    private String drag;
    private String angularDrag;
    private String cageLimit;
    private String hitPoints;
    private String reloadTime;
    private String beamVelocity;
    private String fuseTimer;
    private String rank;
    private String priceCoins;
    
    

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

    public String getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(String rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getThrust() {
        return thrust;
    }

    public void setThrust(String thrust) {
        this.thrust = thrust;
    }

    public String getAngularDrag() {
        return angularDrag;
    }

    public void setAngularDrag(String angularDrag) {
        this.angularDrag = angularDrag;
    }

    public String getDrag() {
        return drag;
    }

    public void setDrag(String drag) {
        this.drag = drag;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getCageLimit() {
        return cageLimit;
    }

    public void setCageLimit(String cageLimit) {
        this.cageLimit = cageLimit;
    }

    public String getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(String hitPoints) {
        this.hitPoints = hitPoints;
    }

    public String getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(String reloadTime) {
        this.reloadTime = reloadTime;
    }

    public String getBeamVelocity() {
        return beamVelocity;
    }

    public void setBeamVelocity(String beamVelocity) {
        this.beamVelocity = beamVelocity;
    }

    public String getFuseTimer() {
        return fuseTimer;
    }

    public void setFuseTimer(String fuseTimer) {
        this.fuseTimer = fuseTimer;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPriceCoins() {
        return priceCoins;
    }
    public void setPriceCoins(String priceCoins) {
        this.priceCoins = priceCoins;
    }
    
    

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        
        out.append(" id : " + this.id);
        out.append(" name : " + this.name);
        out.append(" ship type : " + this.shipType);
        
        return out.toString();
    }
    
    
    
    
}
