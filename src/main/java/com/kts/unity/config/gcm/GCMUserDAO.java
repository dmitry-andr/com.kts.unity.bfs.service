package com.kts.unity.config.gcm;

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


public class GCMUserDAO extends SimpleJdbcDaoSupport {
    
    public int createGCMRecord(GCMUser gcmUser){
        int status = 0;
        String sqlInsertGcmRecord = "INSERT INTO " + GCMUsersTable.TBL_NAME + "("
                + GCMUsersTable.GCM_REGISTRATION_ID + ", "
                + GCMUsersTable.NAME + ", "
                + GCMUsersTable.EMAIL + ", "
                + GCMUsersTable.TIMESTAMP_DATE + ", "
                + GCMUsersTable.TIMESTAMP_TIME
                + ") VALUES(?,?,?,?,?)";
        
        getSimpleJdbcTemplate().update(
                sqlInsertGcmRecord,
                gcmUser.getGcmRegistrationId(),
                gcmUser.getName(),
                gcmUser.getEmail(),
                gcmUser.getTimeStampDate(),
                gcmUser.getTimeStampTime());
        status = 1;

        return status;
    }
    
    public int updateGCMRecord(GCMUser gcmUser){
        int status = 0;
        String sqlUpdateGcmRecord = "UPDATE " + GCMUsersTable.TBL_NAME + " SET "
                + GCMUsersTable.GCM_REGISTRATION_ID + " = ? , "
                + GCMUsersTable.NAME + "  = ? , "
                + GCMUsersTable.EMAIL + "  = ? , "
                + GCMUsersTable.TIMESTAMP_DATE + "  = ? , "
                + GCMUsersTable.TIMESTAMP_TIME + "  = ? "
                + " WHERE " + GCMUsersTable.RECORD_ID + " = ?";
        
        getSimpleJdbcTemplate().update(
                sqlUpdateGcmRecord,
                gcmUser.getGcmRegistrationId(),
                gcmUser.getName(),
                gcmUser.getEmail(),
                gcmUser.getTimeStampDate(),
                gcmUser.getTimeStampTime(),
                gcmUser.getRecordId());
        
        status = 1;

        return status;
    }
    
    public int deleteGcmUser(int recordId){
        int status = 0;

        String sql = "DELETE FROM " + GCMUsersTable.TBL_NAME + " WHERE " + GCMUsersTable.RECORD_ID + " = ?";
        getSimpleJdbcTemplate().update(sql, recordId);
        status = 1;

        return status;
    }
    
    public GCMUser getGcmUserByGcmRegistrationId(String gcmRegId){
        String sql = "SELECT * FROM " + GCMUsersTable.TBL_NAME + " WHERE " + GCMUsersTable.GCM_REGISTRATION_ID + " = ?";
        GCMUser gcmUser = null;
        try{
            gcmUser = (GCMUser) this.getJdbcTemplate().queryForObject(sql, new Object[]{gcmRegId}, new GCMUserDAO.GcmUserRowMapper());
        }catch(IncorrectResultSizeDataAccessException ex){            
        }

        return gcmUser;
    }
    
    public List<GCMUser> getGcmUsers(int numOfRecords) {
        String sql = "SELECT * FROM " + GCMUsersTable.TBL_NAME +                
                " ORDER BY " + GCMUsersTable.TIMESTAMP_DATE + " DESC, " + GCMUsersTable.TIMESTAMP_TIME + " DESC" +
                " LIMIT " + numOfRecords;
        
        ArrayList<GCMUser> gcmUsersList = null;

        try {
            List<Map<String,Object>> gcmObjsInDB = this.getJdbcTemplate().queryForList(sql);

            if (gcmObjsInDB != null) {
                gcmUsersList = new ArrayList<GCMUser>();
                for (Map row : gcmObjsInDB) {
                    GCMUser gcmUserTmp = new GCMUser();
                    gcmUserTmp.setRecordId((Integer) row.get(GCMUsersTable.RECORD_ID));
                    gcmUserTmp.setGcmRegistrationId((String) row.get(GCMUsersTable.GCM_REGISTRATION_ID));
                    gcmUserTmp.setName((String) row.get(GCMUsersTable.NAME));
                    gcmUserTmp.setEmail((String) row.get(GCMUsersTable.EMAIL));                    
                    gcmUserTmp.setTimeStampDate((Date) row.get(GCMUsersTable.TIMESTAMP_DATE));
                    gcmUserTmp.setTimeStampTime((String) row.get(GCMUsersTable.TIMESTAMP_TIME));
                    
                    gcmUsersList.add(gcmUserTmp);
                }
            }
        }catch(CannotGetJdbcConnectionException ex){
            ex.printStackTrace();
            return null;
        }

        return gcmUsersList;
    }//getGcmUsers
    
    public List<GCMUser> getGcmUsersForNames(List<String> userNames) {
        
        StringBuilder sqlInValues = new StringBuilder();

        sqlInValues.append("(");
        for (int k = 0; k < userNames.size(); k++) {
            sqlInValues.append("'" + userNames.get(k) + "'");
            if (k != (userNames.size() - 1)) {
                sqlInValues.append(",");
            }
        }
        sqlInValues.append(")");
        
        
        String sql = "SELECT * FROM " + GCMUsersTable.TBL_NAME +                
                " WHERE " + GCMUsersTable.TBL_NAME + "." + GCMUsersTable.NAME  + " IN " + sqlInValues.toString() +
                " GROUP BY " + GCMUsersTable.TBL_NAME + "." + GCMUsersTable.GCM_REGISTRATION_ID;
        
        ArrayList<GCMUser> gcmUsersList = null;

        try {
            List<Map<String,Object>> gcmObjsInDB = this.getJdbcTemplate().queryForList(sql);

            if (gcmObjsInDB != null) {
                gcmUsersList = new ArrayList<GCMUser>();
                for (Map row : gcmObjsInDB) {
                    GCMUser gcmUserTmp = new GCMUser();
                    gcmUserTmp.setRecordId((Integer) row.get(GCMUsersTable.RECORD_ID));
                    gcmUserTmp.setGcmRegistrationId((String) row.get(GCMUsersTable.GCM_REGISTRATION_ID));
                    gcmUserTmp.setName((String) row.get(GCMUsersTable.NAME));
                    gcmUserTmp.setEmail((String) row.get(GCMUsersTable.EMAIL));                    
                    gcmUserTmp.setTimeStampDate((Date) row.get(GCMUsersTable.TIMESTAMP_DATE));
                    gcmUserTmp.setTimeStampTime((String) row.get(GCMUsersTable.TIMESTAMP_TIME));
                    
                    gcmUsersList.add(gcmUserTmp);
                }
            }
        }catch(CannotGetJdbcConnectionException ex){
            ex.printStackTrace();
            return null;
        }

        return gcmUsersList;
    }//getGcmUsersForNames
    
    public int getNumberOfGCMUsersInSystem(){
        String sql = "SELECT COUNT(" + GCMUsersTable.TBL_NAME + "." + GCMUsersTable.RECORD_ID + ") FROM " + GCMUsersTable.TBL_NAME;
        int numberOfGCMRecords = -1;
        try {
            numberOfGCMRecords = this.getJdbcTemplate().queryForInt(sql);
        } catch (IncorrectResultSizeDataAccessException ex) {
            numberOfGCMRecords = -1;
        }

        return numberOfGCMRecords;
    }
    
    private class GcmUserRowMapper implements RowMapper{
        
        public GcmUserRowMapper(){
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            GCMUser gcmUser = new GCMUser();
            
            gcmUser.setRecordId(rs.getInt(GCMUsersTable.RECORD_ID));
            gcmUser.setGcmRegistrationId(rs.getString(GCMUsersTable.GCM_REGISTRATION_ID));
            gcmUser.setName(rs.getString(GCMUsersTable.NAME));
            gcmUser.setEmail(rs.getString(GCMUsersTable.EMAIL));
            gcmUser.setTimeStampDate(rs.getDate(GCMUsersTable.TIMESTAMP_DATE));
            gcmUser.setTimeStampTime(rs.getString(GCMUsersTable.TIMESTAMP_TIME));
            
            return gcmUser;
        }
    }//GcmUserRowMapper
    
    
}//GCMUserDAO