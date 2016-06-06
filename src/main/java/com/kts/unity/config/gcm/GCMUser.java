package com.kts.unity.config.gcm;

import java.util.Date;

public class GCMUser {
    private int recordId;
    private String gcmRegistrationId;
    private String name;
    private String email;
    private Date timeStampDate;
    private String timeStampTime;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGcmRegistrationId() {
        return gcmRegistrationId;
    }

    public void setGcmRegistrationId(String gcmRegistrationId) {
        this.gcmRegistrationId = gcmRegistrationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Date getTimeStampDate() {
        return timeStampDate;
    }

    public void setTimeStampDate(Date timeStampDate) {
        this.timeStampDate = timeStampDate;
    }

    public String getTimeStampTime() {
        return timeStampTime;
    }

    public void setTimeStampTime(String timeStampTime) {
        this.timeStampTime = timeStampTime;
    }
    
    

    
    
    
}
