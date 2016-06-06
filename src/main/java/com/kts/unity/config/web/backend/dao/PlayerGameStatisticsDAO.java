package com.kts.unity.config.web.backend.dao;

import com.kts.unity.config.web.backend.utils.player.PlayerGameStatisticsTable;
import com.kts.unity.config.web.entities.player.PlayerGameStatistics;
import com.kts.unity.config.web.utils.ConfigParams;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class PlayerGameStatisticsDAO extends SimpleJdbcDaoSupport{
            
    public PlayerGameStatistics getPlayerGameStatistics(int playerId){
        
        String sql = "SELECT * FROM " + PlayerGameStatisticsTable.TBL_NAME + " WHERE " + PlayerGameStatisticsTable.PLAYER_ID + " = ?";

        PlayerGameStatistics playerGameStat = null;
        
        try{
            playerGameStat = (PlayerGameStatistics) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerId}, new PlayerGameStatisticsDAO.PlayerGameStatisticsRowMapper());
        }catch(IncorrectResultSizeDataAccessException ex){
            System.out.println("If this exception does not happen on regular basis - Ignore it !!!");
            ex.printStackTrace();
        }
        
        return playerGameStat;
    }
    
    
    public int deletePlayerGameStatistics(int playerId){
        int status = 0;
        
        String sql = "DELETE FROM " + PlayerGameStatisticsTable.TBL_NAME + " WHERE " + PlayerGameStatisticsTable.PLAYER_ID + " = ?";
        getSimpleJdbcTemplate().update(sql, playerId);
        status = 1;
        
        return status;        
    }
    
    
    public int updatePlayerGameStatistics(PlayerGameStatistics statistics){
        
        int status = 0;

        try{
            String playerStatsUpdateSql = "UPDATE " + PlayerGameStatisticsTable.TBL_NAME + " SET ";
            String columnName;
            for(int k = 0; k < ConfigParams.NUMBER_OF_RANKS; k++){
                columnName = PlayerGameStatisticsTable.KILLED_BY_RANK + (k + 1);
                playerStatsUpdateSql = playerStatsUpdateSql + columnName + " = ?, ";
            }
            for(int k = 0; k < ConfigParams.NUMBER_OF_RANKS; k++){
                columnName = PlayerGameStatisticsTable.KILLED_RANK + (k + 1);
                playerStatsUpdateSql = playerStatsUpdateSql + columnName + " = ? ";
                if(k != (ConfigParams.NUMBER_OF_RANKS - 1)){
                    playerStatsUpdateSql = playerStatsUpdateSql + ", ";
                }
            }        

            playerStatsUpdateSql = playerStatsUpdateSql + "WHERE " + PlayerGameStatisticsTable.PLAYER_ID + " = ?";


            int killedByindex = 0;
            int killedIndex = 0;
            getSimpleJdbcTemplate().update(
                    playerStatsUpdateSql,
                    statistics.getKilledbyrank()[killedByindex++],
                    statistics.getKilledbyrank()[killedByindex++],
                    statistics.getKilledbyrank()[killedByindex++],
                    statistics.getKilledbyrank()[killedByindex++],
                    statistics.getKilledbyrank()[killedByindex++],
                    statistics.getKilledrank()[killedIndex++],
                    statistics.getKilledrank()[killedIndex++],
                    statistics.getKilledrank()[killedIndex++],
                    statistics.getKilledrank()[killedIndex++],
                    statistics.getKilledrank()[killedIndex++],
                    statistics.getPlayerId());
            status = 1;

        }catch(Exception ex){            
            ex.printStackTrace();            
        }
        
        return status;
    }
    
    public int createPLayerGameStatisticsRecord(int playerId){
        int status = 0;
        
        try{
            String createPlayerStatsRecordSql = "INSERT INTO " + PlayerGameStatisticsTable.TBL_NAME + " ( " 
                    + PlayerGameStatisticsTable.PLAYER_ID + ", ";
            String columnName;
            for(int k = 0; k < ConfigParams.NUMBER_OF_RANKS; k++){
                columnName = PlayerGameStatisticsTable.KILLED_BY_RANK + (k + 1);
                createPlayerStatsRecordSql = createPlayerStatsRecordSql + columnName + ", ";
            }
            for(int k = 0; k < ConfigParams.NUMBER_OF_RANKS; k++){
                columnName = PlayerGameStatisticsTable.KILLED_RANK + (k + 1);
                createPlayerStatsRecordSql = createPlayerStatsRecordSql + columnName;
                if(k != (ConfigParams.NUMBER_OF_RANKS - 1)){
                    createPlayerStatsRecordSql = createPlayerStatsRecordSql + ", ";
                }
            }        

            createPlayerStatsRecordSql = createPlayerStatsRecordSql + ") VALUES(?,";//this param is for playerId field
            for(int k = 0; k < (ConfigParams.NUMBER_OF_RANKS * 2); k++){
                createPlayerStatsRecordSql = createPlayerStatsRecordSql + "?";
                if(k != ((ConfigParams.NUMBER_OF_RANKS * 2) - 1)){
                    createPlayerStatsRecordSql = createPlayerStatsRecordSql + ", ";
                }
            }
            createPlayerStatsRecordSql = createPlayerStatsRecordSql + ")";

            getSimpleJdbcTemplate().update(
                    createPlayerStatsRecordSql,
                    playerId,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0);
            status = 1;        

        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return status;
    }
    
    
    private class PlayerGameStatisticsRowMapper implements RowMapper{

        public PlayerGameStatisticsRowMapper() {
            super();
        }
        
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            PlayerGameStatistics gameStatistics = new PlayerGameStatistics(rs.getInt(PlayerGameStatisticsTable.PLAYER_ID));
            
            int[] killedbyrank = new int[ConfigParams.NUMBER_OF_RANKS];
            int[] killedrank = new int[ConfigParams.NUMBER_OF_RANKS];
            for(int k = 0; k < ConfigParams.NUMBER_OF_RANKS; k++){
                String killedbyrankColumnName = PlayerGameStatisticsTable.KILLED_BY_RANK + (k + 1);
                killedbyrank[k] = rs.getInt(killedbyrankColumnName);
                String killedrankColumnName = PlayerGameStatisticsTable.KILLED_RANK + (k + 1);
                killedrank[k] = rs.getInt(killedrankColumnName);
            }
            
            gameStatistics.setKilledbyrank(killedbyrank);
            gameStatistics.setKilledrank(killedrank);            
            
            return gameStatistics;
        }
        
    }
    
}
