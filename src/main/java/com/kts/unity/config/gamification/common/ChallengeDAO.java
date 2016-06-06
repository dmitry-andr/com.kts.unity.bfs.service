package com.kts.unity.config.gamification.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class ChallengeDAO extends SimpleJdbcDaoSupport {
    
    public List<Challenge> getChallengeList(){
        String sql = "SELECT * FROM " + ChallengeTable.TBL_NAME;
        
        List<Map<String, Object>> challengesListDBData = this.getJdbcTemplate().queryForList(sql);
        
        if(challengesListDBData == null){
            return null;
        }
        
        List<Challenge> challengeEntriesList = new ArrayList<Challenge>();
        for(Map row : challengesListDBData){
            Challenge challenge = new Challenge();
            challenge.setId((String) row.get(ChallengeTable.ID));
            challenge.setTitle((String) row.get(ChallengeTable.TITLE));
            challenge.setIntroText((String) row.get(ChallengeTable.INTROTEXT));
            challenge.setIsActive((Integer) row.get(ChallengeTable.IS_ACTIVE));
            
            challengeEntriesList.add(challenge);
        }
        
        return challengeEntriesList;
    }
        
    public int updateActivityStatus(String challengeId, int activityStatusValue){
        int status = 0;
        String sqlUpdateActivityStatus = "UPDATE " + ChallengeTable.TBL_NAME + " SET "                
                + ChallengeTable.IS_ACTIVE + " = ? "
                + " WHERE " + ChallengeTable.ID + " = ?";
        
        getSimpleJdbcTemplate().update(
                sqlUpdateActivityStatus,
                activityStatusValue,
                challengeId);
        status = 1;
        
        return status;
    }
    
    
    
}
