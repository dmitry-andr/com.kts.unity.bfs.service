package com.kts.unity.config.web.backend.dao;

import com.kts.unity.config.web.backend.utils.GameNavigationStatisticsTable;
import com.kts.unity.config.web.entities.GameNavigationStatistics;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class GameNavigationStatisticsDAO extends SimpleJdbcDaoSupport {

    public GameNavigationStatistics getGameNavigationStatistics(String applicationId) {
        String sql = "SELECT * FROM " + GameNavigationStatisticsTable.TBL_NAME + " WHERE " + GameNavigationStatisticsTable.APPLICATION_ID + " = ?";

        GameNavigationStatistics statistics = null;
        try {
            statistics = (GameNavigationStatistics) this.getJdbcTemplate().queryForObject(sql, new Object[]{applicationId}, new GameNavigationStatisticsDAO.NavigationStatisticsRowMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            System.out.println("If this exception does not happen on regular basis - Ignore it !!! ; ApplicationId :" + applicationId + "; exception : " + ex.toString());
            //ex.printStackTrace();
            return null;
        } catch (CannotGetJdbcConnectionException ex) {
            ex.printStackTrace();
            return null;
        }

        return statistics;
    }
    
    public GameNavigationStatistics getGameNavigationStatisticsForPlayer(String playerName) {
        String sql = "SELECT * FROM " + GameNavigationStatisticsTable.TBL_NAME + " WHERE " + GameNavigationStatisticsTable.PLAYER_NAME + " = ?";

        GameNavigationStatistics statistics = null;
        try {
            statistics = (GameNavigationStatistics) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerName}, new GameNavigationStatisticsDAO.NavigationStatisticsRowMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            System.out.println("If this exception does not happen on regular basis - Ignore it !!!; Player name : " + playerName);
            ex.printStackTrace();
            return null;
        } catch (CannotGetJdbcConnectionException ex) {
            ex.printStackTrace();
            return null;
        }

        return statistics;
    }

    public ArrayList<GameNavigationStatistics> getGameNavigationStatisticsList(int numOfRecords) {
        String sql = "SELECT * FROM " + GameNavigationStatisticsTable.TBL_NAME +                
                " ORDER BY " + GameNavigationStatisticsTable.TRANSACTION_DATE + " DESC, " + GameNavigationStatisticsTable.TRANSACTION_TIME + " DESC" +
                " LIMIT " + numOfRecords;
        
        ArrayList<GameNavigationStatistics> statList = null;

        try {
            List<Map<String,Object>> statObjs = this.getJdbcTemplate().queryForList(sql);

             statList = new ArrayList<GameNavigationStatistics>();

            if (statObjs != null) {
                for (Map row : statObjs) {
                    GameNavigationStatistics statTmp = new GameNavigationStatistics();
                    statTmp.setApplicationId((String) row.get(GameNavigationStatisticsTable.APPLICATION_ID));
                    statTmp.setPlayerName((String) row.get(GameNavigationStatisticsTable.PLAYER_NAME));
                    statTmp.setDataCollectionDate((Date) row.get(GameNavigationStatisticsTable.TRANSACTION_DATE));
                    statTmp.setDataCollectionTime((String) row.get(GameNavigationStatisticsTable.TRANSACTION_TIME));
                    statTmp.setNumOfHangarVisits((Integer) row.get(GameNavigationStatisticsTable.NUM_OF_HANGER_VISITS));
                    statTmp.setNumOfCoinsButtonPress((Integer) row.get(GameNavigationStatisticsTable.NUM_OF_COINS_BUTTON_PRESS));
                    statTmp.setFbShareAcheivement((Integer) row.get(GameNavigationStatisticsTable.NUM_OF_FB_SHARE_ACHIEVEMENT_BUTTON_PRESS));
                    statTmp.setTournamentsButton((Integer) row.get(GameNavigationStatisticsTable.NUM_OF_TOURNAMENTS_LIST_BUTTON_PRESS));
                    statTmp.setLevelSquare((Integer) row.get(GameNavigationStatisticsTable.NUM_OF_LVL_SQUARE_LAUNCHES));
                    statTmp.setLevelTriangle((Integer) row.get(GameNavigationStatisticsTable.NUM_OF_LVL_TRIANGLE_LAUNCHES));
                    statTmp.setLevelHex((Integer) row.get(GameNavigationStatisticsTable.NUM_OF_LVL_HEX_LAUNCHES));
                    statTmp.setFbFriends((Integer) row.get(GameNavigationStatisticsTable.NUM_OF_FB_INVITE_FRIENDS_BTN_PRESS));
                    statTmp.setMusicOffBtn((Integer) row.get(GameNavigationStatisticsTable.NUM_OF_MUSIC_OFF_BTN_PRESS));
                    
                    statTmp.setCoins100((Integer) row.get(GameNavigationStatisticsTable.COINS_100));
                    statTmp.setCoins250((Integer) row.get(GameNavigationStatisticsTable.COINS_250));
                    statTmp.setCoins550((Integer) row.get(GameNavigationStatisticsTable.COINS_550));
                    statTmp.setTraining((Integer) row.get(GameNavigationStatisticsTable.TRAINING));
                    statTmp.setBuy((Integer) row.get(GameNavigationStatisticsTable.BUY));
                    statTmp.setAvatar((Integer) row.get(GameNavigationStatisticsTable.AVATAR));
                    statTmp.setShip((Integer) row.get(GameNavigationStatisticsTable.SHIP));
                    statTmp.setWeapon((Integer) row.get(GameNavigationStatisticsTable.WEAPON));
                    statTmp.setPerks((Integer) row.get(GameNavigationStatisticsTable.PERKS));
                    
                    
                    statList.add(statTmp);
                }
            }
        }catch(CannotGetJdbcConnectionException ex){
            ex.printStackTrace();
            return null;
        }

        return statList;
    }

    public int createGameNavigationStatisticsRecord(GameNavigationStatistics statistics) {
        int status = 0;
        try{
            String usageStatInsertSql = "INSERT INTO " + GameNavigationStatisticsTable.TBL_NAME + "("
                    + GameNavigationStatisticsTable.APPLICATION_ID + ","
                    + GameNavigationStatisticsTable.PLAYER_NAME + ","
                    + GameNavigationStatisticsTable.TRANSACTION_DATE + ","
                    + GameNavigationStatisticsTable.TRANSACTION_TIME + ","
                    + GameNavigationStatisticsTable.NUM_OF_HANGER_VISITS + ","
                    + GameNavigationStatisticsTable.NUM_OF_COINS_BUTTON_PRESS + ","
                    + GameNavigationStatisticsTable.NUM_OF_FB_SHARE_ACHIEVEMENT_BUTTON_PRESS + ","
                    + GameNavigationStatisticsTable.NUM_OF_TOURNAMENTS_LIST_BUTTON_PRESS + ","
                    + GameNavigationStatisticsTable.NUM_OF_LVL_SQUARE_LAUNCHES + ","
                    + GameNavigationStatisticsTable.NUM_OF_LVL_TRIANGLE_LAUNCHES + ","
                    + GameNavigationStatisticsTable.NUM_OF_LVL_HEX_LAUNCHES + ","
                    + GameNavigationStatisticsTable.NUM_OF_FB_INVITE_FRIENDS_BTN_PRESS + ","
                    + GameNavigationStatisticsTable.NUM_OF_MUSIC_OFF_BTN_PRESS + ","
                    
                    + GameNavigationStatisticsTable.COINS_100 + ","
                    + GameNavigationStatisticsTable.COINS_250 + ","
                    + GameNavigationStatisticsTable.COINS_550 + ","
                    + GameNavigationStatisticsTable.TRAINING + ","
                    + GameNavigationStatisticsTable.BUY + ","
                    + GameNavigationStatisticsTable.AVATAR + ","
                    + GameNavigationStatisticsTable.SHIP + ","
                    + GameNavigationStatisticsTable.WEAPON + ","
                    + GameNavigationStatisticsTable.PERKS
                    
                    
                    + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            getSimpleJdbcTemplate().update(
                    usageStatInsertSql,
                    statistics.getApplicationId(),
                    statistics.getPlayerName(),
                    statistics.getDataCollectionDate(),
                    statistics.getDataCollectionTime(),
                    statistics.getNumOfHangarVisits(),
                    statistics.getNumOfCoinsButtonPress(),
                    statistics.getFbShareAcheivement(),
                    statistics.getTournamentsButton(),
                    statistics.getLevelSquare(),
                    statistics.getLevelTriangle(),
                    statistics.getLevelHex(),
                    statistics.getFbFriends(),
                    statistics.getMusicOffBtn(),
                    
                    statistics.getCoins100(),
                    statistics.getCoins250(),
                    statistics.getCoins550(),
                    statistics.getTraining(),
                    statistics.getBuy(),
                    statistics.getAvatar(),
                    statistics.getShip(),
                    statistics.getWeapon(),
                    statistics.getPerks()
                    
                    );
            status = 1;
            
        }catch(Exception ex){
            ex.printStackTrace();            
        }
        
        return status;
    }

    
public int delteGameNavigationStatisticsRecord(String gameNavigationStatRecordId){
        int status = 0;
        
        String sql = "DELETE FROM " + GameNavigationStatisticsTable.TBL_NAME + " WHERE " + GameNavigationStatisticsTable.APPLICATION_ID + " = ?";
        getSimpleJdbcTemplate().update(sql, gameNavigationStatRecordId);
        status = 1;
        
        return status;
}
    
    
    public int updateGameNavigationStatisticsRecord(GameNavigationStatistics statistics) {

        int status = 0;
        try{
            String navStatUpdateSql = "UPDATE " + GameNavigationStatisticsTable.TBL_NAME + " SET "
                    + GameNavigationStatisticsTable.TRANSACTION_DATE + " = ?,"
                    + GameNavigationStatisticsTable.TRANSACTION_TIME + " = ?,"
                    + GameNavigationStatisticsTable.NUM_OF_HANGER_VISITS + " = ?,"
                    + GameNavigationStatisticsTable.NUM_OF_COINS_BUTTON_PRESS + " = ?,"
                    + GameNavigationStatisticsTable.NUM_OF_FB_SHARE_ACHIEVEMENT_BUTTON_PRESS + " = ?,"
                    + GameNavigationStatisticsTable.NUM_OF_TOURNAMENTS_LIST_BUTTON_PRESS + " = ?,"
                    + GameNavigationStatisticsTable.NUM_OF_LVL_SQUARE_LAUNCHES + " = ?,"
                    + GameNavigationStatisticsTable.NUM_OF_LVL_TRIANGLE_LAUNCHES + " = ?, "
                    + GameNavigationStatisticsTable.NUM_OF_LVL_HEX_LAUNCHES + " = ? , "
                    + GameNavigationStatisticsTable.NUM_OF_FB_INVITE_FRIENDS_BTN_PRESS + " = ?, "
                    + GameNavigationStatisticsTable.NUM_OF_MUSIC_OFF_BTN_PRESS + " = ?, "
                    
                    + GameNavigationStatisticsTable.COINS_100 + " = ?, "
                    + GameNavigationStatisticsTable.COINS_250 + " = ?, "
                    + GameNavigationStatisticsTable.COINS_550 + " = ?, "
                    + GameNavigationStatisticsTable.TRAINING + " = ?, "
                    + GameNavigationStatisticsTable.BUY + " = ?, "
                    + GameNavigationStatisticsTable.AVATAR + " = ?, "
                    + GameNavigationStatisticsTable.SHIP + " = ?, "
                    + GameNavigationStatisticsTable.WEAPON + " = ?, "
                    + GameNavigationStatisticsTable.PERKS + " = ?"
                    
                    
                    + " WHERE " + GameNavigationStatisticsTable.APPLICATION_ID + " = ?";

            getSimpleJdbcTemplate().update(
                    navStatUpdateSql,
                    statistics.getDataCollectionDate(),
                    statistics.getDataCollectionTime(),
                    statistics.getNumOfHangarVisits(),
                    statistics.getNumOfCoinsButtonPress(),
                    statistics.getFbShareAcheivement(),
                    statistics.getTournamentsButton(),
                    statistics.getLevelSquare(),
                    statistics.getLevelTriangle(),
                    statistics.getLevelHex(),
                    statistics.getFbFriends(),
                    statistics.getMusicOffBtn(),

                    statistics.getCoins100(),
                    statistics.getCoins250(),
                    statistics.getCoins550(),
                    statistics.getTraining(),
                    statistics.getBuy(),
                    statistics.getAvatar(),
                    statistics.getShip(),
                    statistics.getWeapon(),
                    statistics.getPerks(),
                    
                    
                    
                    
                    
                    statistics.getApplicationId());
            status = 1;

        }catch(Exception ex){            
            ex.printStackTrace();            
        }
                
        return status;
    }
    
    
    public int getNumberOfRecords(){
        String sql ="SELECT COUNT(" + GameNavigationStatisticsTable.TBL_NAME + "." + GameNavigationStatisticsTable.APPLICATION_ID + ") FROM " + GameNavigationStatisticsTable.TBL_NAME;
        int numberOfRecords = -1;
        try {
            numberOfRecords = this.getJdbcTemplate().queryForInt(sql);
        } catch (IncorrectResultSizeDataAccessException ex) {
            numberOfRecords = -1;
        }
        
        return numberOfRecords;
    }

    private class NavigationStatisticsRowMapper implements RowMapper {

        public NavigationStatisticsRowMapper() {
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            GameNavigationStatistics statistics = new GameNavigationStatistics();

            statistics.setApplicationId(rs.getString(GameNavigationStatisticsTable.APPLICATION_ID));
            statistics.setPlayerName(rs.getString(GameNavigationStatisticsTable.PLAYER_NAME));
            statistics.setDataCollectionDate(rs.getDate(GameNavigationStatisticsTable.TRANSACTION_DATE));
            statistics.setDataCollectionTime(rs.getString(GameNavigationStatisticsTable.TRANSACTION_TIME));
            statistics.setNumOfHangarVisits(rs.getInt(GameNavigationStatisticsTable.NUM_OF_HANGER_VISITS));
            statistics.setNumOfCoinsButtonPress(rs.getInt(GameNavigationStatisticsTable.NUM_OF_COINS_BUTTON_PRESS));
            statistics.setFbShareAcheivement(rs.getInt(GameNavigationStatisticsTable.NUM_OF_FB_SHARE_ACHIEVEMENT_BUTTON_PRESS));
            statistics.setTournamentsButton(rs.getInt(GameNavigationStatisticsTable.NUM_OF_TOURNAMENTS_LIST_BUTTON_PRESS));
            statistics.setLevelSquare(rs.getInt(GameNavigationStatisticsTable.NUM_OF_LVL_SQUARE_LAUNCHES));
            statistics.setLevelTriangle(rs.getInt(GameNavigationStatisticsTable.NUM_OF_LVL_TRIANGLE_LAUNCHES));
            statistics.setLevelHex(rs.getInt(GameNavigationStatisticsTable.NUM_OF_LVL_HEX_LAUNCHES));
            statistics.setFbFriends(rs.getInt(GameNavigationStatisticsTable.NUM_OF_FB_INVITE_FRIENDS_BTN_PRESS));
            statistics.setMusicOffBtn(rs.getInt(GameNavigationStatisticsTable.NUM_OF_MUSIC_OFF_BTN_PRESS));
            
            statistics.setCoins100(rs.getInt(GameNavigationStatisticsTable.COINS_100));
            statistics.setCoins250(rs.getInt(GameNavigationStatisticsTable.COINS_250));
            statistics.setCoins550(rs.getInt(GameNavigationStatisticsTable.COINS_550));
            statistics.setTraining(rs.getInt(GameNavigationStatisticsTable.TRAINING));
            statistics.setBuy(rs.getInt(GameNavigationStatisticsTable.BUY));
            statistics.setAvatar(rs.getInt(GameNavigationStatisticsTable.AVATAR));
            statistics.setShip(rs.getInt(GameNavigationStatisticsTable.SHIP));
            statistics.setWeapon(rs.getInt(GameNavigationStatisticsTable.WEAPON));
            statistics.setPerks(rs.getInt(GameNavigationStatisticsTable.PERKS));
            
            return statistics;
        }
    }
}
