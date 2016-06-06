package com.kts.unity.config.gcm;

import java.util.List;

public class GCMManager {
    private GCMUserDAO gcmUserDAO;    

    public int createGcmRecord(GCMUser gcmUser){
        return this.gcmUserDAO.createGCMRecord(gcmUser);
    }
    
    public int updateGcmRecord(GCMUser gcmUser){
        return this.gcmUserDAO.updateGCMRecord(gcmUser);
    }
    
    public int deleteGcmRecord(int gcmRecordId){
        return this.gcmUserDAO.deleteGcmUser(gcmRecordId);
    }
    
    public GCMUser getGcmUserByGcmRegId(String regId){
        return this.gcmUserDAO.getGcmUserByGcmRegistrationId(regId);
    }
    
    public List<GCMUser> getGcmUsersList(int maxNumOfRecords){
        
        return this.gcmUserDAO.getGcmUsers(maxNumOfRecords);
    }
    
    public List<GCMUser> getGcmUsersForNames(List<String> usersNames){
        
        return this.gcmUserDAO.getGcmUsersForNames(usersNames);
    }
    
    public int getNumOfGCMUsers(){
        return this.gcmUserDAO.getNumberOfGCMUsersInSystem();
    }
    
    
    public GCMUserDAO getGcmUserDAO() {
        return gcmUserDAO;
    }

    public void setGcmUserDAO(GCMUserDAO gcmUserDAO) {
        this.gcmUserDAO = gcmUserDAO;
    }
    
}
