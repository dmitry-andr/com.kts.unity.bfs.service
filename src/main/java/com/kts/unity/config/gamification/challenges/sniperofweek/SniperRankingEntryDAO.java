package com.kts.unity.config.gamification.challenges.sniperofweek;

import com.kts.unity.config.gamification.common.ScoresRankingEntry;
import com.kts.unity.config.gamification.common.SniperRankingEntry;
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

public class SniperRankingEntryDAO extends SimpleJdbcDaoSupport {
    
        public int createSniperRankingEntry(SniperRankingEntry sniperRankingEntry){
        int status = 0;
        String sqlInsertRankingEntry = "INSERT INTO " + SniperRankingTable.TBL_NAME + "("
                + SniperRankingTable.PLAYER_ID + ", "
                + SniperRankingTable.LAST_ACTIVITY_TIMESTAMP + ", "
                + SniperRankingTable.SHOOTING_HISTORY + ", "
                + SniperRankingTable.ACCURACY_SCORES + ", "
                + SniperRankingTable.POSITION_AT_LAST_GAME
                + ") VALUES(?,?,?,?,?)";
        
        getSimpleJdbcTemplate().update(
                sqlInsertRankingEntry,
                sniperRankingEntry.getPlayerId(),
                sniperRankingEntry.getLastActivityTime(),
                sniperRankingEntry.getShootingHistory(),
                sniperRankingEntry.getScores(),
                sniperRankingEntry.getPositionAtLastGame());
        
        status = 1;
                
        return status;
    }
    
    public int updateSniperRankingEntry(SniperRankingEntry sniperRankingEntry){
        int status = 0;
        
        String sniperRankingUpdateQryStr = "UPDATE " + SniperRankingTable.TBL_NAME + " SET "
                + SniperRankingTable.LAST_ACTIVITY_TIMESTAMP + " = ? ,"
                + SniperRankingTable.SHOOTING_HISTORY + " = ? ,"
                + SniperRankingTable.ACCURACY_SCORES + " = ? ,"
                + SniperRankingTable.POSITION_AT_LAST_GAME + " = ? "
                + " WHERE " + SniperRankingTable.PLAYER_ID + " = ?";

        getSimpleJdbcTemplate().update(
                sniperRankingUpdateQryStr,
                sniperRankingEntry.getLastActivityTime(),
                sniperRankingEntry.getShootingHistory(),
                sniperRankingEntry.getScores(),
                sniperRankingEntry.getPositionAtLastGame(),
                sniperRankingEntry.getPlayerId());
        status = 1;
        
        return status;
    }
    
    public SniperRankingEntry getSniperRankingEntryForId(int playerId){
        String sql = "SELECT * FROM " + SniperRankingTable.TBL_NAME + " WHERE " + SniperRankingTable.PLAYER_ID + " = ?";
        SniperRankingEntry sniperRankingEntry = null;
        try{
            sniperRankingEntry = (SniperRankingEntry) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerId}, new SniperRankingEntryDAO.SniperRankingEntryRowMapper());
        }catch(IncorrectResultSizeDataAccessException ex){            
        }

        return sniperRankingEntry;
    }
    
    
    public List<ScoresRankingEntry> getEntryListForLastWeekPeriod(){
        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PlayerGameProfileTable.TBL_NAME + ".*, " + SniperRankingTable.TBL_NAME + ".*"
                + " FROM " + SniperRankingTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + SniperRankingTable.TBL_NAME + "." + SniperRankingTable.PLAYER_ID + "=" + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                + " LEFT JOIN " + PlayerTable.TBL_NAME + " ON " + SniperRankingTable.TBL_NAME + "." + SniperRankingTable.PLAYER_ID  + "=" + PlayerTable.TBL_NAME + "." + PlayerTable.ID
                + " ORDER BY " + SniperRankingTable.TBL_NAME + "." + SniperRankingTable.ACCURACY_SCORES + " DESC";

        List<Map<String, Object>> dayRatingDBData = this.getJdbcTemplate().queryForList(sql);
        
        if (dayRatingDBData != null) {
            List<ScoresRankingEntry> sniperOfWeekEntriesList = new ArrayList<ScoresRankingEntry>();
            for (Map row : dayRatingDBData) {
                ScoresRankingEntry tmpData = new ScoresRankingEntry();
                tmpData.setPlayerId((Integer) row.get(SniperRankingTable.PLAYER_ID));
                tmpData.setPlayerName((String) row.get(PlayerTable.NAME));
                tmpData.setLastActivityTime((Long) row.get(SniperRankingTable.LAST_ACTIVITY_TIMESTAMP));
                //tmpData.setShootingHistory((String) row.get(SniperRankingTable.SHOOTING_HISTORY));
                tmpData.setScores((Integer) row.get(SniperRankingTable.ACCURACY_SCORES));
                tmpData.setPositionAtLastGame((Integer) row.get(SniperRankingTable.POSITION_AT_LAST_GAME));
                tmpData.setAvatar((String) row.get(PlayerGameProfileTable.AVATAR));
                tmpData.setRank((Integer) row.get(PlayerGameProfileTable.RANK));
                
                long currentTimeMillis = (new Date()).getTime();
                if ((currentTimeMillis - tmpData.getLastActivityTime()) < 604800000) {//1week = 7d*24h*60min*60s*1000  millis
                    sniperOfWeekEntriesList.add(tmpData);
                } else {//entry is obsolete - delete it
                    String sqlDelete = "DELETE FROM " + SniperRankingTable.TBL_NAME + " WHERE " + SniperRankingTable.PLAYER_ID + " = ?";
                    getSimpleJdbcTemplate().update(sqlDelete, tmpData.getPlayerId());
                }
            }
            
            return sniperOfWeekEntriesList;
        } else {
            return null;
        }
    }
    
    public int getPlayerPositionInSniperRanking(int playerId) {
        String sql = "SELECT id FROM (SELECT *, @rownum:=@rownum + 1 AS id FROM " + SniperRankingTable.TBL_NAME + ", (SELECT @rownum:=0) r ORDER BY " + SniperRankingTable.ACCURACY_SCORES + " DESC) d WHERE d." + SniperRankingTable.PLAYER_ID + "=" + playerId;

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
        String sql = "SELECT COUNT(" + SniperRankingTable.PLAYER_ID + ") FROM " + SniperRankingTable.TBL_NAME;
        int numOfRecords = this.getJdbcTemplate().queryForInt(sql);
        return numOfRecords;
    }
    
    public int clearAllDataForSniperRanking(){
        int status = 0;
        
        String sql = "DELETE FROM " + SniperRankingTable.TBL_NAME;
        this.getJdbcTemplate().update(sql);
        status = 1;
        
        return status;        
    }
    
    public int clearDataForPlayer(int playerId){
        int status = 0;
        
        String sql = "DELETE FROM " + SniperRankingTable.TBL_NAME + " WHERE " + SniperRankingTable.TBL_NAME + "." + SniperRankingTable.PLAYER_ID + "=" + playerId; 
        this.getJdbcTemplate().update(sql);
        status = 1;
        
        return status;        
    }
    
    private class SniperRankingEntryRowMapper implements RowMapper{
        
        public SniperRankingEntryRowMapper(){
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            SniperRankingEntry sniperRankingEntry = new SniperRankingEntry();
            
            sniperRankingEntry.setPlayerId(rs.getInt(SniperRankingTable.PLAYER_ID));
            sniperRankingEntry.setLastActivityTime(rs.getLong(SniperRankingTable.LAST_ACTIVITY_TIMESTAMP));
            sniperRankingEntry.setShootingHistory(rs.getString(SniperRankingTable.SHOOTING_HISTORY));
            sniperRankingEntry.setScores(rs.getInt(SniperRankingTable.ACCURACY_SCORES));
            sniperRankingEntry.setPositionAtLastGame(rs.getInt(SniperRankingTable.POSITION_AT_LAST_GAME));
            
            return sniperRankingEntry;
        }        
    }
    
    
    
}
