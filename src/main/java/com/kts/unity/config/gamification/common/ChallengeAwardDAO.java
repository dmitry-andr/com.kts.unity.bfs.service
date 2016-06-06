package com.kts.unity.config.gamification.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class ChallengeAwardDAO extends SimpleJdbcDaoSupport {
    
    public int createAwardRecordForPlayer(ChallengeAward challengeAward){
        int status = 0;
        String sqlInsertAwardRecord = "INSERT INTO " + ChallengeAwardTable.TBL_NAME + "("
                + ChallengeAwardTable.ID + ", "
                + ChallengeAwardTable.AWARDS_LIST
                + ") VALUES(?,?)";
        
        getSimpleJdbcTemplate().update(
                sqlInsertAwardRecord,
                challengeAward.getPlayerId(),
                challengeAward.getAwardsList());
        
        status = 1;

        return status;
    }
    
    public int updateChallengeAwardsRecord(ChallengeAward awards){
        int status = 0;
        String sqlUpdateChallengeAwardRecord = "UPDATE " + ChallengeAwardTable.TBL_NAME + " SET "                
                + ChallengeAwardTable.AWARDS_LIST + "  = ? "                
                + " WHERE " + ChallengeAwardTable.ID + " = ?";
        
        getSimpleJdbcTemplate().update(
                sqlUpdateChallengeAwardRecord,
                awards.getAwardsList(),
                awards.getPlayerId());
        
        status = 1;

        return status;
    }
    
    public ChallengeAward getChallengeAwardsForPlayer(int playerId){
        String sql = "SELECT * FROM " + ChallengeAwardTable.TBL_NAME + " WHERE " + ChallengeAwardTable.TBL_NAME + "." + ChallengeAwardTable.ID + " = ?";
        
        ChallengeAward challengesAward = null;
        try{
            challengesAward = (ChallengeAward) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerId}, new ChallengeAwardDAO.ChallengeAwardRowMapper());
        }catch(IncorrectResultSizeDataAccessException ex){            
        }
        
        return challengesAward;
    }
    
    
    
    
    private class ChallengeAwardRowMapper implements RowMapper{
        
        public ChallengeAwardRowMapper(){
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            ChallengeAward challengeAward = new ChallengeAward();
            
            challengeAward.setPlayerId(rs.getInt(ChallengeAwardTable.ID));
            challengeAward.setAwardsList(rs.getString(ChallengeAwardTable.AWARDS_LIST));
                        
            return challengeAward;
        }
    }//ChallengeAwardRowMapper    
}
