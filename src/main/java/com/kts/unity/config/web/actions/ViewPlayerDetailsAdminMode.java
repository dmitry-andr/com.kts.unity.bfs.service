package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.PlayerManager;
import com.kts.unity.config.web.entities.player.PlayerWithGameProfAndStatisticsAndChallengeData;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;

public class ViewPlayerDetailsAdminMode {

    private PlayerWithGameProfAndStatisticsAndChallengeData playerData;
    private String playerIdStringVal;

    public String execute() {

        PlayerManager playerMgr = (PlayerManager) AppContext.getContext().getBean(AppSpringBeans.PLAYER_MANAGER_BEAN);

        int playerId = Integer.parseInt(playerIdStringVal);
        
        playerData = playerMgr.getPlayerListWithGameDataAndStatistics(playerId);

        return ActionConstants.SUCCESS;
    }

    public PlayerWithGameProfAndStatisticsAndChallengeData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerWithGameProfAndStatisticsAndChallengeData playerData) {
        this.playerData = playerData;
    }

    public String getPlayerIdStringVal() {
        return playerIdStringVal;
    }

    public void setPlayerIdStringVal(String playerIdStringVal) {
        this.playerIdStringVal = playerIdStringVal;
    }
}
