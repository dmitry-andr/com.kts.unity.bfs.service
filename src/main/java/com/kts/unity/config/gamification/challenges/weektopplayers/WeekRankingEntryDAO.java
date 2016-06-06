package com.kts.unity.config.gamification.challenges.weektopplayers;

import com.kts.unity.config.gamification.common.ScoresRankingEntry;
import com.kts.unity.config.web.backend.utils.player.PlayerGameProfileTable;
import com.kts.unity.config.web.backend.utils.player.PlayerTable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class WeekRankingEntryDAO extends SimpleJdbcDaoSupport {
    
    public int createWeekRankingEntry(ScoresRankingEntry weekRankingEntry){
        int status = 0;
        String sqlInsertRankingEntry = "INSERT INTO " + WeekRankingTable.TBL_NAME + "("
                + WeekRankingTable.PLAYER_ID + ", "
                + WeekRankingTable.LAST_ACTIVITY_TIMESTAMP + ", "
                + WeekRankingTable.WEEK_SCORES + ", "
                + WeekRankingTable.POSITION_AT_LAST_GAME
                + ") VALUES(?,?,?,?)";
        
        getSimpleJdbcTemplate().update(
                sqlInsertRankingEntry,
                weekRankingEntry.getPlayerId(),
                weekRankingEntry.getLastActivityTime(),
                weekRankingEntry.getScores(),
                weekRankingEntry.getPositionAtLastGame());
        
        status = 1;        
                
        return status;
    }
    
    public int updateWeekRankingEntry(ScoresRankingEntry weekRatingEntry){
        int status = 0;
        
        String weekRankingUpdateQryStr = "UPDATE " + WeekRankingTable.TBL_NAME + " SET "
                + WeekRankingTable.LAST_ACTIVITY_TIMESTAMP + " = ? ,"
                + WeekRankingTable.WEEK_SCORES + " = ? ,"
                + WeekRankingTable.POSITION_AT_LAST_GAME + " = ? "
                + " WHERE " + WeekRankingTable.PLAYER_ID + " = ?";

        getSimpleJdbcTemplate().update(
                weekRankingUpdateQryStr,
                weekRatingEntry.getLastActivityTime(),
                weekRatingEntry.getScores(),
                weekRatingEntry.getPositionAtLastGame(),
                weekRatingEntry.getPlayerId());
        status = 1;        
        
        return status;
    }
    
    public ScoresRankingEntry getWeekRankingEntryForId(int playerId){
        String sql = "SELECT * FROM " + WeekRankingTable.TBL_NAME + " WHERE " + WeekRankingTable.PLAYER_ID + " = ?";
        ScoresRankingEntry weekRankingEntry = null;
        try{
            weekRankingEntry = (ScoresRankingEntry) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerId}, new WeekRankingEntryDAO.WeekRankingEntryRowMapper());
        }catch(IncorrectResultSizeDataAccessException ex){            
        }

        return weekRankingEntry;
    }
    
    
    public List<ScoresRankingEntry> getEntryListForLastWeekPeriod(){
        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PlayerGameProfileTable.TBL_NAME + ".*, " + WeekRankingTable.TBL_NAME + ".*"
                + " FROM " + WeekRankingTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + WeekRankingTable.TBL_NAME + "." + WeekRankingTable.PLAYER_ID + "=" + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                + " LEFT JOIN " + PlayerTable.TBL_NAME + " ON " + WeekRankingTable.TBL_NAME + "." + WeekRankingTable.PLAYER_ID  + "=" + PlayerTable.TBL_NAME + "." + PlayerTable.ID
                + " ORDER BY " + WeekRankingTable.TBL_NAME + "." + WeekRankingTable.WEEK_SCORES + " DESC";

        List<Map<String, Object>> weekRatingDBData = this.getJdbcTemplate().queryForList(sql);
        
        if (weekRatingDBData != null) {
            List<ScoresRankingEntry> weekEntriesList = new ArrayList<ScoresRankingEntry>();
            for (Map row : weekRatingDBData) {
                ScoresRankingEntry tmpData = new ScoresRankingEntry();
                tmpData.setPlayerId((Integer) row.get(WeekRankingTable.PLAYER_ID));
                tmpData.setPlayerName((String) row.get(PlayerTable.NAME));
                tmpData.setLastActivityTime((Long) row.get(WeekRankingTable.LAST_ACTIVITY_TIMESTAMP));
                tmpData.setScores((Integer) row.get(WeekRankingTable.WEEK_SCORES));
                tmpData.setPositionAtLastGame((Integer) row.get(WeekRankingTable.POSITION_AT_LAST_GAME));
                tmpData.setAvatar((String) row.get(PlayerGameProfileTable.AVATAR));
                tmpData.setRank((Integer) row.get(PlayerGameProfileTable.RANK));
                
                weekEntriesList.add(tmpData);
                /*This is not used longer. All players are being cleared once tournament is over
                long currentTimeMillis = (new Date()).getTime();
                if ((currentTimeMillis - tmpData.getLastActivityTime()) < 604800000) {//1week = 7d*24h*60min*60s*1000  millis
                    weekEntriesList.add(tmpData);
                } else {//entry is obsolete - delete it
                    String sqlDelete = "DELETE FROM " + WeekRankingTable.TBL_NAME + " WHERE " + WeekRankingTable.PLAYER_ID + " = ?";
                    getSimpleJdbcTemplate().update(sqlDelete, tmpData.getPlayerId());
                }
                */
            }
            
            return weekEntriesList;
        } else {
            return null;
        }
    }
    
    public int getPlayerPositionInWeekRanking(int playerId) {
        String sql = "SELECT id FROM (SELECT *, @rownum:=@rownum + 1 AS id FROM " + WeekRankingTable.TBL_NAME + ", (SELECT @rownum:=0) r ORDER BY " + WeekRankingTable.WEEK_SCORES + " DESC) d WHERE d." + WeekRankingTable.PLAYER_ID + "=" + playerId;

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
        String sql = "SELECT COUNT(" + WeekRankingTable.PLAYER_ID + ") FROM " + WeekRankingTable.TBL_NAME;
        int numOfRecords = this.getJdbcTemplate().queryForInt(sql);
        return numOfRecords;
    }
    
    public int clearAllDataForWeekRanking(){
        int status = 0;
        
        String sql = "DELETE FROM " + WeekRankingTable.TBL_NAME;
        this.getJdbcTemplate().update(sql);
        status = 1;
        
        return status;        
    }
    
    public int clearDataForPlayer(int playerId){
        int status = 0;
        
        String sql = "DELETE FROM " + WeekRankingTable.TBL_NAME + " WHERE " + WeekRankingTable.TBL_NAME + "." + WeekRankingTable.PLAYER_ID + "=" + playerId;
        this.getJdbcTemplate().update(sql);
        status = 1;
        
        return status;        
    }
    
    private class WeekRankingEntryRowMapper implements RowMapper{
        
        public WeekRankingEntryRowMapper(){
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScoresRankingEntry weekRankingEntry = new ScoresRankingEntry();
            
            weekRankingEntry.setPlayerId(rs.getInt(WeekRankingTable.PLAYER_ID));
            weekRankingEntry.setLastActivityTime(rs.getLong(WeekRankingTable.LAST_ACTIVITY_TIMESTAMP));
            weekRankingEntry.setScores(rs.getInt(WeekRankingTable.WEEK_SCORES));
            weekRankingEntry.setPositionAtLastGame(rs.getInt(WeekRankingTable.POSITION_AT_LAST_GAME));
            
            return weekRankingEntry;
        }        
    }
    
}
