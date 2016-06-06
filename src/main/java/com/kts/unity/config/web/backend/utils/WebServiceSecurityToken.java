package com.kts.unity.config.web.backend.utils;

public class WebServiceSecurityToken {
    private String tokenId;
    private long tokenCreationTimeMillis;

    public long getTokenCreationTimeMillis() {
        return tokenCreationTimeMillis;
    }

    public void setTokenCreationTimeMillis(long tokenCreationTimeMillis) {
        this.tokenCreationTimeMillis = tokenCreationTimeMillis;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    
    
}
