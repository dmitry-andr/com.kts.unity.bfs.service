package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.PlayerManager;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.entities.player.PlayerWithGameProfAndStatisticsAndChallengeData;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import java.util.ArrayList;


public class ViewPlayersListAdminPage {
    
    private ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> playersList;
    private String showAll;
    private int numberOfPlayers;
    
    
    public String execute(){        
        PlayerManager playerMgr = (PlayerManager) AppContext.getContext().getBean(AppSpringBeans.PLAYER_MANAGER_BEAN);
        
        this.numberOfPlayers = playerMgr.getNumberOfPlayersInSystem();
        
        if("true".equals(showAll)){
            this.playersList = playerMgr.getPlayersListWithGameProfiles(Settings.getMaxNumOfPlayersOnAdminPage());            
        }else{
            this.playersList = playerMgr.getPlayersListWithGameProfiles(Settings.getInitialNumOfPlayersOnAdminPage());
        }
                
        return ActionConstants.SUCCESS;
    }

    
    public ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> playersList) {
        this.playersList = playersList;
    }

    public String getShowAll() {
        return showAll;
    }

    public void setShowAll(String showAll) {
        this.showAll = showAll;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
    
    
}
