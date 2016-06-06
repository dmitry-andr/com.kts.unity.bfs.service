package com.kts.unity.config.web.entities;

import com.kts.unity.config.web.entities.player.PlayerWithGameProfAndStatisticsAndChallengeData;
import com.restfb.types.User;

public class FbUserWithGameData {
    
    private User fbUser;
    private PlayerWithGameProfAndStatisticsAndChallengeData gameData;
    private boolean online = false;

    public FbUserWithGameData(User fbUser, PlayerWithGameProfAndStatisticsAndChallengeData gameData) {
        this.fbUser = fbUser;
        this.gameData = gameData;
    }
    
    public User getFbUser() {
        return fbUser;
    }

    public void setFbUser(User fbUser) {
        this.fbUser = fbUser;
    }

    public PlayerWithGameProfAndStatisticsAndChallengeData getGameData() {
        return gameData;
    }

    public void setGameData(PlayerWithGameProfAndStatisticsAndChallengeData gameData) {
        this.gameData = gameData;
    }

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    
    

}
