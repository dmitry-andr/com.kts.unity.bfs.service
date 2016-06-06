package com.kts.unity.config.web.backend.dao;

import com.kts.unity.config.web.backend.utils.FacebookIdentityTable;
import com.restfb.types.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class FacebookIdentityDAO extends SimpleJdbcDaoSupport {

    public int createFbIdentity(int playerId, String facebookId) {
        int status = 0;
        String fbIdentityInsertSql = "INSERT INTO " + FacebookIdentityTable.TBL_NAME + "("
                + FacebookIdentityTable.PLAYER_ID + ","
                + FacebookIdentityTable.FACEBOOK_ID
                + ") VALUES(?,?)";

        getSimpleJdbcTemplate().update(
                fbIdentityInsertSql,
                playerId,
                facebookId);
        status = 1;

        return status;
    }
    
    public int removeFbIdentity(int playerId){
        int status = 0;
        
        String sql = "DELETE FROM " + FacebookIdentityTable.TBL_NAME + " WHERE " + FacebookIdentityTable.PLAYER_ID + " = ?";
        getSimpleJdbcTemplate().update(sql, playerId);
        status = 1;
        
        return status;
    }

    public String getFbIdForPlayer(int playerId) {
        String sql = "SELECT * FROM " + FacebookIdentityTable.TBL_NAME + " WHERE " + FacebookIdentityTable.PLAYER_ID + " = ?";

        FbIdentity playerFbIdentity = null;

        try {
            playerFbIdentity = (FbIdentity) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerId}, new FacebookIdentityDAO.FbIdentityRowMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            //add logging here !!!
            return null;
        }

        if (playerFbIdentity != null) {
            return playerFbIdentity.getFacebookId();
        } else {
            return null;
        }
    }

    public int getPlayerIdForFbId(String facebookId) {
        
        //Before reading a data, check if record for such facebook ID exists
        String sqlToCountIds = "SELECT COUNT(*) FROM "+ FacebookIdentityTable.TBL_NAME + " WHERE " + FacebookIdentityTable.FACEBOOK_ID + " = ?" ; 
	int total = getJdbcTemplate().queryForInt(sqlToCountIds, new Object[] { facebookId });
        if(total == 0){
            return -1;
        }
        
        
        String sql = "SELECT * FROM " + FacebookIdentityTable.TBL_NAME + " WHERE " + FacebookIdentityTable.FACEBOOK_ID + " = ?";
        FbIdentity playerFbIdentity = null;
        try {
            playerFbIdentity = (FbIdentity) this.getJdbcTemplate().queryForObject(sql, new Object[]{facebookId}, new FacebookIdentityDAO.FbIdentityRowMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {            
            ex.printStackTrace();
            return -1;
        } catch (CannotGetJdbcConnectionException ex) {
            ex.printStackTrace();
            return -1;
        }


        if (playerFbIdentity != null) {
            return playerFbIdentity.getPlayerId();
        } else {
            return -1;
        }
    }

    public int removeFbIdentityFromGame(int playerId) {
        int status = 0;

        String sql = "DELETE FROM " + FacebookIdentityTable.TBL_NAME + " WHERE " + FacebookIdentityTable.PLAYER_ID + " = ?";
        getSimpleJdbcTemplate().update(
                sql,
                playerId);
        status = 1;

        return status;
    }
    
    public Map<Integer, String> getFbIdentitiesForUSers(ArrayList<Integer> playersIds){
        
        if(playersIds == null){
            return null;
        }
        
        StringBuilder sqlWhereStatement = new StringBuilder();
        for(int k = 0; k < playersIds.size(); k++){
            //sqlWhereStatement.append("(" + PlayerTable.TBL_NAME +"."+PlayerTable.ID + " = ? AND " + PlayerGameProfileTable.TBL_NAME+"."+PlayerGameProfileTable.PROFILE_ID + " = ?)");
            sqlWhereStatement.append("(" + FacebookIdentityTable.TBL_NAME +"."+ FacebookIdentityTable.PLAYER_ID + " = "+ playersIds.get(k) + ")");
            if(k != (playersIds.size() - 1)){
                sqlWhereStatement.append(" OR ");
            }
        }
        
        String sql = "SELECT * FROM " + FacebookIdentityTable.TBL_NAME + " WHERE " + sqlWhereStatement.toString();
        
        List<Map<String,Object>> fbIdentities = this.getJdbcTemplate().queryForList(sql);
        if(fbIdentities != null){
            Map<Integer, String> fbIdents = new HashMap<Integer, String>();
            for(Map row : fbIdentities){
                fbIdents.put((Integer) row.get(FacebookIdentityTable.PLAYER_ID), (String) row.get(FacebookIdentityTable.FACEBOOK_ID));
            }
            
            return fbIdents;
        }else{
            return null;
        }
    }
    
    public ArrayList<Integer> getPlayerIdsForFacebookUSers(ArrayList<User> facebookUsers){
        
        if(facebookUsers == null){
            return null;
        }
        
        StringBuilder sqlWhereStatement = new StringBuilder();
        for(int k = 0; k < facebookUsers.size(); k++){
            //sqlWhereStatement.append("(" + PlayerTable.TBL_NAME +"."+PlayerTable.ID + " = ? AND " + PlayerGameProfileTable.TBL_NAME+"."+PlayerGameProfileTable.PROFILE_ID + " = ?)");
            
            sqlWhereStatement.append("(" + FacebookIdentityTable.TBL_NAME +"."+ FacebookIdentityTable.FACEBOOK_ID + " = "+ facebookUsers.get(k).getId() + ")");
            if(k != (facebookUsers.size() - 1)){
                sqlWhereStatement.append(" OR ");
            }
        }
        
        String sql = "SELECT * FROM " + FacebookIdentityTable.TBL_NAME + " WHERE " + sqlWhereStatement.toString();
        System.out.println("SQL getPlayerIdsForFacebookUSers :\n" + sql);
        
        List<Map<String,Object>> fbIdentities = this.getJdbcTemplate().queryForList(sql);
        if(fbIdentities != null){
            ArrayList<Integer> playerIds = new ArrayList<Integer>();
            for(Map row : fbIdentities){
                playerIds.add((Integer) row.get(FacebookIdentityTable.PLAYER_ID));
            }            
            return playerIds;
        }else{
            return null;
        }
    }
    

    private class FbIdentityRowMapper implements RowMapper {

        public FbIdentityRowMapper() {
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            FbIdentity fbIdent = new FbIdentity();
            fbIdent.setPlayerId(rs.getInt(FacebookIdentityTable.PLAYER_ID));
            fbIdent.setFacebookId(rs.getString(FacebookIdentityTable.FACEBOOK_ID));

            return fbIdent;
        }
    }

    private class FbIdentity {

        private int playerId;
        private String facebookId;

        public String getFacebookId() {
            return facebookId;
        }

        public void setFacebookId(String facebookId) {
            this.facebookId = facebookId;
        }

        public int getPlayerId() {
            return playerId;
        }

        public void setPlayerId(int playerId) {
            this.playerId = playerId;
        }
    }
}
