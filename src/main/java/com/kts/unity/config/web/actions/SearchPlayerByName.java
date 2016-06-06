package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.PlayerManager;
import com.kts.unity.config.web.entities.player.Player;
import com.kts.unity.config.web.entities.player.PlayerWithGameProfAndStatisticsAndChallengeData;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;

public class SearchPlayerByName {

    private PlayerWithGameProfAndStatisticsAndChallengeData playerData;
    private String playerNameStringVal;

    public String execute() {

        PlayerManager playerMgr = (PlayerManager) AppContext.getContext().getBean(AppSpringBeans.PLAYER_MANAGER_BEAN);

        Player player = playerMgr.getPlayerByName(playerNameStringVal);

        if (player != null) {            
            playerData = playerMgr.getPlayerListWithGameDataAndStatistics(player.getId());
        }


        return ActionConstants.SUCCESS;
    }

    public PlayerWithGameProfAndStatisticsAndChallengeData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerWithGameProfAndStatisticsAndChallengeData playerData) {
        this.playerData = playerData;
    }

    public String getPlayerNameStringVal() {
        return playerNameStringVal;
    }

    public void setPlayerNameStringVal(String playerNameStringVal) {
        this.playerNameStringVal = playerNameStringVal;
    }
}
