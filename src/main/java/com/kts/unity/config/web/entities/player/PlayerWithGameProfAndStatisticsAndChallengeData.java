package com.kts.unity.config.web.entities.player;

import com.kts.unity.config.gamification.common.ScoresRankingEntry;
import com.kts.unity.config.web.entities.LevelsProgress;

public class PlayerWithGameProfAndStatisticsAndChallengeData {
    private Player player;
    private PlayerGameProfile gameProfile;
    private LevelsProgress levelsProgress;
    private PlayerGameStatistics gamesStatistics;
    private ScoresRankingEntry challengeData;
    private String facebookId;
    private static int numOfPages;
    private int nextRankProgress;

    public PlayerGameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(PlayerGameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public LevelsProgress getLevelsProgress() {
        return levelsProgress;
    }

    public void setLevelsProgress(LevelsProgress levelsProgress) {
        this.levelsProgress = levelsProgress;
    }
    
    public PlayerGameStatistics getGamesStatistics() {
        return gamesStatistics;
    }

    public void setGamesStatistics(PlayerGameStatistics gamesStatistics) {
        this.gamesStatistics = gamesStatistics;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public static int getNumOfPages() {
        return numOfPages;
    }

    public static void setNumOfPages(int numOfPages) {
        PlayerWithGameProfAndStatisticsAndChallengeData.numOfPages = numOfPages;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public int getNextRankProgress() {
        return nextRankProgress;
    }

    public void setNextRankProgress(int nextRankProgress) {
        this.nextRankProgress = nextRankProgress;
    }

    public ScoresRankingEntry getChallengeData() {
        return challengeData;
    }

    public void setChallengeData(ScoresRankingEntry challengeData) {
        this.challengeData = challengeData;
    }
    
    

    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(">Player ID : " + this.player.getId() + "; ");
        out.append("Player name : " + this.player.getName());
        
        return out.toString();
    }
    
    
    
}
