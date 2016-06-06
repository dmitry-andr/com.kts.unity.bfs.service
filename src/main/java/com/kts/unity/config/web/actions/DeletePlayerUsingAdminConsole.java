package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.PlayerManager;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.entities.player.PlayerWithGameProfAndStatisticsAndChallengeData;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;


public class DeletePlayerUsingAdminConsole extends ActionSupport implements SessionAware{

    private String playerIdStringVal;
    private ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> playersList;
    private int numberOfPlayers;
    
    private Map session;
    private String message;
    
    
    @Override
    public String execute() throws Exception {
        
        Boolean isAuthenticated = (Boolean) session.get("authenticated");
        if ((isAuthenticated != null) && (isAuthenticated.booleanValue())) {
            
            PlayerManager playerMgr =  (PlayerManager) AppContext.getContext().getBean(AppSpringBeans.PLAYER_MANAGER_BEAN);
            
            int playerId = Integer.parseInt(playerIdStringVal);
            
            int status = playerMgr.removePlayerWithProfileAndData(playerId);
            if(status == 1){
                this.message = "Player with ID : " + playerId + " - was removed successfuly";
            }else{
                this.message = "Error while delteting user with ID : " + playerId;
            }
            
            this.playersList = playerMgr.getPlayersListWithGameProfiles(Settings.getInitialNumOfPlayersOnAdminPage());
            this.numberOfPlayers = playerMgr.getNumberOfPlayersInSystem();
            return ActionConstants.SUCCESS;
        }else{
            return ActionConstants.AUTHENTICATION;
        } 
    }
    
    
    public Map getSession() {
        return session;
    }
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlayerIdStringVal() {
        return playerIdStringVal;
    }

    public void setPlayerIdStringVal(String playerIdStringVal) {
        this.playerIdStringVal = playerIdStringVal;
    }


    public ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> playersList) {
        this.playersList = playersList;
    }
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }        
    
}
