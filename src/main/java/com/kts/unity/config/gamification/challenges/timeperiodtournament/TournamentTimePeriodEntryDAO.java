package com.kts.unity.config.gamification.challenges.timeperiodtournament;

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

public class TournamentTimePeriodEntryDAO extends SimpleJdbcDaoSupport {
    
    public int createTournamentRankingEntry(ScoresRankingEntry tournamentRankingEntry){
        int status = 0;
        String sqlInsertRankingEntry = "INSERT INTO " + TournamentTimePeriodTable.TBL_NAME + "("
                + TournamentTimePeriodTable.PLAYER_ID + ", "
                + TournamentTimePeriodTable.LAST_ACTIVITY_TIMESTAMP + ", "
                + TournamentTimePeriodTable.WEEK_SCORES + ", "
                + TournamentTimePeriodTable.POSITION_AT_LAST_GAME
                + ") VALUES(?,?,?,?)";
        
        getSimpleJdbcTemplate().update(
                sqlInsertRankingEntry,
                tournamentRankingEntry.getPlayerId(),
                tournamentRankingEntry.getLastActivityTime(),
                tournamentRankingEntry.getScores(),
                tournamentRankingEntry.getPositionAtLastGame());
        
        status = 1;        
                
        return status;
    }
    
    public int updateTournamentRankingEntry(ScoresRankingEntry tournamentRatingEntry){
        int status = 0;
        
        String weekRankingUpdateQryStr = "UPDATE " + TournamentTimePeriodTable.TBL_NAME + " SET "
                + TournamentTimePeriodTable.LAST_ACTIVITY_TIMESTAMP + " = ? ,"
                + TournamentTimePeriodTable.WEEK_SCORES + " = ? ,"
                + TournamentTimePeriodTable.POSITION_AT_LAST_GAME + " = ? "
                + " WHERE " + TournamentTimePeriodTable.PLAYER_ID + " = ?";

        getSimpleJdbcTemplate().update(
                weekRankingUpdateQryStr,
                tournamentRatingEntry.getLastActivityTime(),
                tournamentRatingEntry.getScores(),
                tournamentRatingEntry.getPositionAtLastGame(),
                tournamentRatingEntry.getPlayerId());
        status = 1;        
        
        return status;
    }
    
    public ScoresRankingEntry getTournamentRankingEntryForId(int playerId){
        String sql = "SELECT * FROM " + TournamentTimePeriodTable.TBL_NAME + " WHERE " + TournamentTimePeriodTable.PLAYER_ID + " = ?";
        ScoresRankingEntry tournamentRankingEntry = null;
        try{
            tournamentRankingEntry = (ScoresRankingEntry) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerId}, new TournamentTimePeriodEntryDAO.TournamentEntryRowMapper());
        }catch(IncorrectResultSizeDataAccessException ex){            
        }

        return tournamentRankingEntry;
    }
    
    
    public List<ScoresRankingEntry> getEntryListForTournament(){
        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PlayerGameProfileTable.TBL_NAME + ".*, " + TournamentTimePeriodTable.TBL_NAME + ".*"
                + " FROM " + TournamentTimePeriodTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + TournamentTimePeriodTable.TBL_NAME + "." + TournamentTimePeriodTable.PLAYER_ID + "=" + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                + " LEFT JOIN " + PlayerTable.TBL_NAME + " ON " + TournamentTimePeriodTable.TBL_NAME + "." + TournamentTimePeriodTable.PLAYER_ID  + "=" + PlayerTable.TBL_NAME + "." + PlayerTable.ID
                + " ORDER BY " + TournamentTimePeriodTable.TBL_NAME + "." + TournamentTimePeriodTable.WEEK_SCORES + " DESC";

                
        List<Map<String, Object>> tournamentRatingDBData = this.getJdbcTemplate().queryForList(sql);
        
        if (tournamentRatingDBData != null) {
            List<ScoresRankingEntry> tournamentEntriesList = new ArrayList<ScoresRankingEntry>();
            for (Map row : tournamentRatingDBData) {
                ScoresRankingEntry tmpData = new ScoresRankingEntry();
                tmpData.setPlayerId((Integer) row.get(TournamentTimePeriodTable.PLAYER_ID));
                tmpData.setPlayerName((String) row.get(PlayerTable.NAME));
                tmpData.setLastActivityTime((Long) row.get(TournamentTimePeriodTable.LAST_ACTIVITY_TIMESTAMP));
                tmpData.setScores((Integer) row.get(TournamentTimePeriodTable.WEEK_SCORES));
                tmpData.setPositionAtLastGame((Integer) row.get(TournamentTimePeriodTable.POSITION_AT_LAST_GAME));
                tmpData.setAvatar((String) row.get(PlayerGameProfileTable.AVATAR));
                tmpData.setRank((Integer) row.get(PlayerGameProfileTable.RANK));
                tournamentEntriesList.add(tmpData);
            }
            
            return tournamentEntriesList;
        } else {
            return null;
        }
    }
    
    public int getPlayerPositionInTournament(int playerId) {
        String sql = "SELECT id FROM (SELECT *, @rownum:=@rownum + 1 AS id FROM " + TournamentTimePeriodTable.TBL_NAME + ", (SELECT @rownum:=0) r ORDER BY " + TournamentTimePeriodTable.WEEK_SCORES + " DESC) d WHERE d." + TournamentTimePeriodTable.PLAYER_ID + "=" + playerId;

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
        String sql = "SELECT COUNT(" + TournamentTimePeriodTable.PLAYER_ID + ") FROM " + TournamentTimePeriodTable.TBL_NAME;
        int numOfRecords = this.getJdbcTemplate().queryForInt(sql);
        return numOfRecords;
    }
    
    public int clearAllDataForTournament(){
        int status = 0;
        
        String sql = "DELETE FROM " + TournamentTimePeriodTable.TBL_NAME;
        this.getJdbcTemplate().update(sql);
        status = 1;
        
        return status;        
    }
    
    public int clearDataForPlayer(int playerId){
        int status = 0;
        
        String sql = "DELETE FROM " + TournamentTimePeriodTable.TBL_NAME + " WHERE " + TournamentTimePeriodTable.TBL_NAME + "." + TournamentTimePeriodTable.PLAYER_ID + "=" + playerId;
        this.getJdbcTemplate().update(sql);
        status = 1;
        
        return status;        
    }
    
    private class TournamentEntryRowMapper implements RowMapper{
        
        public TournamentEntryRowMapper(){
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScoresRankingEntry tournamentRankingEntry = new ScoresRankingEntry();
            
            tournamentRankingEntry.setPlayerId(rs.getInt(TournamentTimePeriodTable.PLAYER_ID));
            tournamentRankingEntry.setLastActivityTime(rs.getLong(TournamentTimePeriodTable.LAST_ACTIVITY_TIMESTAMP));
            tournamentRankingEntry.setScores(rs.getInt(TournamentTimePeriodTable.WEEK_SCORES));
            tournamentRankingEntry.setPositionAtLastGame(rs.getInt(TournamentTimePeriodTable.POSITION_AT_LAST_GAME));
            
            return tournamentRankingEntry;
        }        
    }
    
}
