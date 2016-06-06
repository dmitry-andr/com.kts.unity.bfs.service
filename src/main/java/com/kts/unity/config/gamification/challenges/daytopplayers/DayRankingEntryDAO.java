package com.kts.unity.config.gamification.challenges.daytopplayers;

import com.kts.unity.config.gamification.common.ScoresRankingEntry;
import com.kts.unity.config.web.backend.utils.player.PlayerGameProfileTable;
import com.kts.unity.config.web.backend.utils.player.PlayerTable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class DayRankingEntryDAO extends SimpleJdbcDaoSupport {
    
    public int createDayRankingEntry(ScoresRankingEntry dayRankingEntry){
        int status = 0;
        String sqlInsertRankingEntry = "INSERT INTO " + DayRankingTable.TBL_NAME + "("
                + DayRankingTable.PLAYER_ID + ", "
                + DayRankingTable.LAST_ACTIVITY_TIMESTAMP + ", "
                + DayRankingTable.DAY_SCORES + ", "
                + DayRankingTable.POSITION_AT_LAST_GAME
                + ") VALUES(?,?,?,?)";
        
        getSimpleJdbcTemplate().update(
                sqlInsertRankingEntry,
                dayRankingEntry.getPlayerId(),
                dayRankingEntry.getLastActivityTime(),
                dayRankingEntry.getScores(),
                dayRankingEntry.getPositionAtLastGame());
        
        status = 1;        
                
        return status;
    }
    
    public int updateDayRankingEntry(ScoresRankingEntry dayRatingEntry){
        int status = 0;
        
        String dayRankingUpdateQryStr = "UPDATE " + DayRankingTable.TBL_NAME + " SET "
                + DayRankingTable.LAST_ACTIVITY_TIMESTAMP + " = ? ,"
                + DayRankingTable.DAY_SCORES + " = ? ,"
                + DayRankingTable.POSITION_AT_LAST_GAME + " = ? "
                + " WHERE " + DayRankingTable.PLAYER_ID + " = ?";

        getSimpleJdbcTemplate().update(
                dayRankingUpdateQryStr,
                dayRatingEntry.getLastActivityTime(),
                dayRatingEntry.getScores(),
                dayRatingEntry.getPositionAtLastGame(),
                dayRatingEntry.getPlayerId());
        status = 1;
        
        return status;
    }
    
    public ScoresRankingEntry getDayRankingEntryForId(int playerId){
        String sql = "SELECT * FROM " + DayRankingTable.TBL_NAME + " WHERE " + DayRankingTable.PLAYER_ID + " = ?";
        ScoresRankingEntry dayRankingEntry = null;
        try{
            dayRankingEntry = (ScoresRankingEntry) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerId}, new DayRankingEntryDAO.DayRankingEntryRowMapper());
        }catch(IncorrectResultSizeDataAccessException ex){            
        }

        return dayRankingEntry;
    }
    
    
    public List<ScoresRankingEntry> getEntryListForLastDayPeriod(){
        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PlayerGameProfileTable.TBL_NAME + ".*, " + DayRankingTable.TBL_NAME + ".*"
                + " FROM " + DayRankingTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + DayRankingTable.TBL_NAME + "." + DayRankingTable.PLAYER_ID + "=" + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                + " LEFT JOIN " + PlayerTable.TBL_NAME + " ON " + DayRankingTable.TBL_NAME + "." + DayRankingTable.PLAYER_ID  + "=" + PlayerTable.TBL_NAME + "." + PlayerTable.ID
                + " ORDER BY " + DayRankingTable.TBL_NAME + "." + DayRankingTable.DAY_SCORES + " DESC";

        List<Map<String, Object>> dayRatingDBData = this.getJdbcTemplate().queryForList(sql);
        
        if (dayRatingDBData != null) {
            List<ScoresRankingEntry> dayEntriesList = new ArrayList<ScoresRankingEntry>();
            for (Map row : dayRatingDBData) {
                ScoresRankingEntry tmpData = new ScoresRankingEntry();
                tmpData.setPlayerId((Integer) row.get(DayRankingTable.PLAYER_ID));
                tmpData.setPlayerName((String) row.get(PlayerTable.NAME));
                tmpData.setLastActivityTime((Long) row.get(DayRankingTable.LAST_ACTIVITY_TIMESTAMP));
                tmpData.setScores((Integer) row.get(DayRankingTable.DAY_SCORES));
                tmpData.setPositionAtLastGame((Integer) row.get(DayRankingTable.POSITION_AT_LAST_GAME));
                tmpData.setAvatar((String) row.get(PlayerGameProfileTable.AVATAR));
                tmpData.setRank((Integer) row.get(PlayerGameProfileTable.RANK));
                //plyers deleted only on awarding
                dayEntriesList.add(tmpData);
                
                /*
                long currentTimeMillis = (new Date()).getTime();
                if ((currentTimeMillis - tmpData.getLastActivityTime()) < 86400000) {//1day = 1d*24h*60min*60s*1000  millis
                    
                } else {//entry is obsolete - delete it
                    String sqlDelete = "DELETE FROM " + DayRankingTable.TBL_NAME + " WHERE " + DayRankingTable.PLAYER_ID + " = ?";
                    getSimpleJdbcTemplate().update(sqlDelete, tmpData.getPlayerId());
                }
                 */
            }
            
            return dayEntriesList;
        } else {
            return null;
        }
    }
    
    public int getPlayerPositionInDayRanking(int playerId) {
        String sql = "SELECT id FROM (SELECT *, @rownum:=@rownum + 1 AS id FROM " + DayRankingTable.TBL_NAME + ", (SELECT @rownum:=0) r ORDER BY " + DayRankingTable.DAY_SCORES + " DESC) d WHERE d." + DayRankingTable.PLAYER_ID + "=" + playerId;

        //System.out.println("SQL getPlayerPositionInWeekRanking : " + sql);

        int playerPosition = -1;
        try {
            playerPosition = this.getJdbcTemplate().queryForInt(sql);
        } catch (IncorrectResultSizeDataAccessException ex) {
            playerPosition = -1;
        }

        return playerPosition;
    }
    
    
    public int getNumberOfRecords(){
        String sql = "SELECT COUNT(" + DayRankingTable.PLAYER_ID + ") FROM " + DayRankingTable.TBL_NAME;
        int numOfRecords = this.getJdbcTemplate().queryForInt(sql);
        return numOfRecords;
    }
    
    public int clearAllDataForDayRanking(){
        int status = 0;
        
        String sql = "DELETE FROM " + DayRankingTable.TBL_NAME;
        this.getJdbcTemplate().update(sql);
        status = 1;
        
        return status;        
    }
    
    public int clearDataForPlayer(int playerId){
        int status = 0;
        
        String sql = "DELETE FROM " + DayRankingTable.TBL_NAME + " WHERE " + DayRankingTable.TBL_NAME + "." + DayRankingTable.PLAYER_ID + "=" + playerId; 
        this.getJdbcTemplate().update(sql);
        status = 1;
        
        return status;        
    }
    
    
    private class DayRankingEntryRowMapper implements RowMapper{
        
        public DayRankingEntryRowMapper(){
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScoresRankingEntry dayRankingEntry = new ScoresRankingEntry();
            
            dayRankingEntry.setPlayerId(rs.getInt(DayRankingTable.PLAYER_ID));
            dayRankingEntry.setLastActivityTime(rs.getLong(DayRankingTable.LAST_ACTIVITY_TIMESTAMP));
            dayRankingEntry.setScores(rs.getInt(DayRankingTable.DAY_SCORES));
            dayRankingEntry.setPositionAtLastGame(rs.getInt(DayRankingTable.POSITION_AT_LAST_GAME));
            
            return dayRankingEntry;
        }        
    }
    
    
}
