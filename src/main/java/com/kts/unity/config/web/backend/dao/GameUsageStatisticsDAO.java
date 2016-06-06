package com.kts.unity.config.web.backend.dao;

import com.kts.unity.config.web.backend.utils.GameUsageStatisticsTable;
import com.kts.unity.config.web.entities.GameUsageStatistics;
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

public class GameUsageStatisticsDAO extends SimpleJdbcDaoSupport {

    public GameUsageStatistics getGameUsageStatistics(String applicationId) {
        String sql = "SELECT * FROM " + GameUsageStatisticsTable.TBL_NAME + " WHERE " + GameUsageStatisticsTable.APPLICATION_ID + " = ?";

        GameUsageStatistics statistics = null;
        try {
            statistics = (GameUsageStatistics) this.getJdbcTemplate().queryForObject(sql, new Object[]{applicationId}, new GameUsageStatisticsDAO.UsageStatisticsRowMapper());
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
    
    public GameUsageStatistics getGameUsageStatisticsForPlayer(String playerName) {
        String sql = "SELECT * FROM " + GameUsageStatisticsTable.TBL_NAME + " WHERE " + GameUsageStatisticsTable.PLAYER_NAME + " = ?";

        GameUsageStatistics statistics = null;
        try {
            statistics = (GameUsageStatistics) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerName}, new GameUsageStatisticsDAO.UsageStatisticsRowMapper());
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

    public ArrayList<GameUsageStatistics> getGameUsageStatisticsList(int numOfRecords) {
        String sql = "SELECT * FROM " + GameUsageStatisticsTable.TBL_NAME +                
                " ORDER BY " + GameUsageStatisticsTable.TRANSACTION_DATE + " DESC, " + GameUsageStatisticsTable.TRANSACTION_TIME + " DESC" +
                " LIMIT " + numOfRecords;
        
        ArrayList<GameUsageStatistics> statList = null;

        try {
            List<Map<String,Object>> statObjs = this.getJdbcTemplate().queryForList(sql);

             statList = new ArrayList<GameUsageStatistics>();

            if (statObjs != null) {
                for (Map row : statObjs) {
                    GameUsageStatistics statTmp = new GameUsageStatistics();
                    statTmp.setApplicationId((String) row.get(GameUsageStatisticsTable.APPLICATION_ID));
                    statTmp.setPlayerName((String) row.get(GameUsageStatisticsTable.PLAYER_NAME));
                    statTmp.setDataCollectionDate((Date) row.get(GameUsageStatisticsTable.TRANSACTION_DATE));
                    statTmp.setDataCollectionTime((String) row.get(GameUsageStatisticsTable.TRANSACTION_TIME));
                    statTmp.setUsageTime((Integer) row.get(GameUsageStatisticsTable.USAGE_TIME));
                    statTmp.setLastUsageTime((Integer) row.get(GameUsageStatisticsTable.LAST_USAGE_TIME));
                    statTmp.setUsedTimes((Integer) row.get(GameUsageStatisticsTable.USED_TIMES));
                    statTmp.setAverageRespWait((Integer) row.get(GameUsageStatisticsTable.AVER_TRANSACT_TIME));
                    statTmp.setMaxRespWait((Integer) row.get(GameUsageStatisticsTable.MAX_TRANSACT_TIME));
                    statTmp.setMinRespWait((Integer) row.get(GameUsageStatisticsTable.MIN_TRANSACT_TIME));
                    statTmp.setAverPing((Integer) row.get(GameUsageStatisticsTable.AVER_PING));
                    statTmp.setMaxPing((Integer) row.get(GameUsageStatisticsTable.MAX_PING));
                    statTmp.setMinPing((Integer) row.get(GameUsageStatisticsTable.MIN_PING));
                    statTmp.setAverFps((Integer) row.get(GameUsageStatisticsTable.AVER_FPS));
                    statTmp.setMaxFps((Integer) row.get(GameUsageStatisticsTable.MAX_FPS));
                    statTmp.setMinFps((Integer) row.get(GameUsageStatisticsTable.MIN_FPS));
                    statTmp.setSecondsOfUsageSPMode((Integer) row.get(GameUsageStatisticsTable.SEC_USAGE_SP_MODE));
                    statTmp.setNumberOfSPGames((Integer) row.get(GameUsageStatisticsTable.TIMES_USAGE_SP_MODE));

                    statList.add(statTmp);
                }
            }
        }catch(CannotGetJdbcConnectionException ex){
            ex.printStackTrace();
            return null;
        }

        return statList;
    }

    public int createGameUsageStatRecord(GameUsageStatistics statistics) {
        int status = 0;
        try{
            String usageStatInsertSql = "INSERT INTO " + GameUsageStatisticsTable.TBL_NAME + "("
                    + GameUsageStatisticsTable.APPLICATION_ID + ","
                    + GameUsageStatisticsTable.PLAYER_NAME + ","
                    + GameUsageStatisticsTable.TRANSACTION_DATE + ","
                    + GameUsageStatisticsTable.TRANSACTION_TIME + ","
                    + GameUsageStatisticsTable.USAGE_TIME + ","
                    + GameUsageStatisticsTable.LAST_USAGE_TIME + ","
                    + GameUsageStatisticsTable.USED_TIMES + ","
                    + GameUsageStatisticsTable.AVER_TRANSACT_TIME + ","
                    + GameUsageStatisticsTable.MAX_TRANSACT_TIME + ","
                    + GameUsageStatisticsTable.MIN_TRANSACT_TIME + ","
                    + GameUsageStatisticsTable.AVER_PING + ","
                    + GameUsageStatisticsTable.MAX_PING + ","
                    + GameUsageStatisticsTable.MIN_PING + ","
                    + GameUsageStatisticsTable.AVER_FPS + ","
                    + GameUsageStatisticsTable.MAX_FPS + ","
                    + GameUsageStatisticsTable.MIN_FPS + ","
                    + GameUsageStatisticsTable.SEC_USAGE_SP_MODE + ","
                    + GameUsageStatisticsTable.TIMES_USAGE_SP_MODE
                    + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            getSimpleJdbcTemplate().update(
                    usageStatInsertSql,
                    statistics.getApplicationId(),
                    statistics.getPlayerName(),
                    statistics.getDataCollectionDate(),
                    statistics.getDataCollectionTime(),
                    statistics.getUsageTime(),
                    statistics.getLastUsageTime(),
                    statistics.getUsedTimes(),
                    statistics.getAverageRespWait(),
                    statistics.getMaxRespWait(),
                    statistics.getMinRespWait(),
                    statistics.getAverPing(),
                    statistics.getMaxPing(),
                    statistics.getMinPing(),
                    statistics.getAverFps(),
                    statistics.getMaxFps(),
                    statistics.getMinFps(),
                    statistics.getSecondsOfUsageSPMode(),
                    statistics.getNumberOfSPGames());
            status = 1;
            
        }catch(Exception ex){
            ex.printStackTrace();            
        }
        
        return status;
    }

    
public int delteGameUsageStatRecord(String gameUsageRecordId){
        int status = 0;
        
        String sql = "DELETE FROM " + GameUsageStatisticsTable.TBL_NAME + " WHERE " + GameUsageStatisticsTable.APPLICATION_ID + " = ?";
        getSimpleJdbcTemplate().update(sql, gameUsageRecordId);
        status = 1;
        
        return status;
}
    
    
    public int updateGameUsageStatRecord(GameUsageStatistics statistics) {

        int status = 0;
        try{
            String usageStatUpdateSql = "UPDATE " + GameUsageStatisticsTable.TBL_NAME + " SET "
                    + GameUsageStatisticsTable.TRANSACTION_DATE + " = ?,"
                    + GameUsageStatisticsTable.TRANSACTION_TIME + " = ?,"
                    + GameUsageStatisticsTable.USAGE_TIME + " = ?,"
                    + GameUsageStatisticsTable.LAST_USAGE_TIME + " = ?,"
                    + GameUsageStatisticsTable.USED_TIMES + " = ?,"
                    + GameUsageStatisticsTable.AVER_TRANSACT_TIME + " = ?,"
                    + GameUsageStatisticsTable.MAX_TRANSACT_TIME + " = ?,"
                    + GameUsageStatisticsTable.MIN_TRANSACT_TIME + " = ?, "
                    + GameUsageStatisticsTable.AVER_PING + " = ?, "
                    + GameUsageStatisticsTable.MAX_PING + " = ?, "
                    + GameUsageStatisticsTable.MIN_PING + " = ?, "
                    + GameUsageStatisticsTable.AVER_FPS + " = ?, "
                    + GameUsageStatisticsTable.MAX_FPS + " = ?, "
                    + GameUsageStatisticsTable.MIN_FPS + " = ?, "
                    + GameUsageStatisticsTable.SEC_USAGE_SP_MODE + " = ?, "
                    + GameUsageStatisticsTable.TIMES_USAGE_SP_MODE + " = ?"
                    + " WHERE " + GameUsageStatisticsTable.APPLICATION_ID + " = ?";

            getSimpleJdbcTemplate().update(
                    usageStatUpdateSql,
                    statistics.getDataCollectionDate(),
                    statistics.getDataCollectionTime(),
                    statistics.getUsageTime(),
                    statistics.getLastUsageTime(),
                    statistics.getUsedTimes(),
                    statistics.getAverageRespWait(),
                    statistics.getMaxRespWait(),
                    statistics.getMinRespWait(),
                    statistics.getAverPing(),
                    statistics.getMaxPing(),
                    statistics.getMinPing(),
                    statistics.getAverFps(),
                    statistics.getMaxFps(),
                    statistics.getMinFps(),
                    statistics.getSecondsOfUsageSPMode(),
                    statistics.getNumberOfSPGames(),
                    statistics.getApplicationId());
            status = 1;

        }catch(Exception ex){            
            ex.printStackTrace();            
        }
                
        return status;
    }
    
    
    public int getNumberOfRecords(){
        String sql ="SELECT COUNT(" + GameUsageStatisticsTable.TBL_NAME + "." + GameUsageStatisticsTable.APPLICATION_ID + ") FROM " + GameUsageStatisticsTable.TBL_NAME;
        int numberOfRecords = -1;
        try {
            numberOfRecords = this.getJdbcTemplate().queryForInt(sql);
        } catch (IncorrectResultSizeDataAccessException ex) {
            numberOfRecords = -1;
        }
        
        return numberOfRecords;
    }

    private class UsageStatisticsRowMapper implements RowMapper {

        public UsageStatisticsRowMapper() {
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            GameUsageStatistics statistics = new GameUsageStatistics();

            statistics.setApplicationId(rs.getString(GameUsageStatisticsTable.APPLICATION_ID));
            statistics.setPlayerName(rs.getString(GameUsageStatisticsTable.PLAYER_NAME));
            statistics.setDataCollectionDate(rs.getDate(GameUsageStatisticsTable.TRANSACTION_DATE));
            statistics.setDataCollectionTime(rs.getString(GameUsageStatisticsTable.TRANSACTION_TIME));
            statistics.setUsageTime(rs.getInt(GameUsageStatisticsTable.USAGE_TIME));
            statistics.setLastUsageTime(rs.getInt(GameUsageStatisticsTable.LAST_USAGE_TIME));
            statistics.setUsedTimes(rs.getInt(GameUsageStatisticsTable.USED_TIMES));
            statistics.setAverageRespWait(rs.getInt(GameUsageStatisticsTable.AVER_TRANSACT_TIME));
            statistics.setMaxRespWait(rs.getInt(GameUsageStatisticsTable.MAX_TRANSACT_TIME));
            statistics.setMinRespWait(rs.getInt(GameUsageStatisticsTable.MIN_TRANSACT_TIME));
            statistics.setAverPing(rs.getInt(GameUsageStatisticsTable.AVER_PING));
            statistics.setMaxPing(rs.getInt(GameUsageStatisticsTable.MAX_PING));
            statistics.setMinPing(rs.getInt(GameUsageStatisticsTable.MIN_PING));
            statistics.setAverFps(rs.getInt(GameUsageStatisticsTable.AVER_FPS));
            statistics.setMaxFps(rs.getInt(GameUsageStatisticsTable.MAX_FPS));
            statistics.setMinFps(rs.getInt(GameUsageStatisticsTable.MIN_FPS));
            statistics.setSecondsOfUsageSPMode(rs.getInt(GameUsageStatisticsTable.SEC_USAGE_SP_MODE));
            statistics.setNumberOfSPGames(rs.getInt(GameUsageStatisticsTable.TIMES_USAGE_SP_MODE));

            return statistics;
        }
    }
}
