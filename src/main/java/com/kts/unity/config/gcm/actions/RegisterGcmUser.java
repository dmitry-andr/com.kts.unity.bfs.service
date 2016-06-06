package com.kts.unity.config.gcm.actions;

import com.kts.unity.config.gcm.GCMManager;
import com.kts.unity.config.gcm.GCMUser;
import com.kts.unity.config.web.actions.ActionConstants;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.opensymphony.xwork2.ActionSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RegisterGcmUser extends ActionSupport {

    private String paramGcmRegId;
    private String paramName;
    private String paramEmail;

    @Override
    public String execute() {

        System.out.println("Register GCM User Passed params : " + paramGcmRegId + " ; " + paramName + " ; " + paramEmail);
        if((paramGcmRegId == null) || (paramName == null) || (paramEmail == null)){
            return ActionConstants.ERROR;
        }
        if ("".equals(paramGcmRegId) || "".equals(paramName) || "".equals(paramEmail)) {
            return ActionConstants.ERROR;
        }
        
        GCMManager gcmMgr = (GCMManager) AppContext.getContext().getBean(AppSpringBeans.GCM_MANAGER_BEAN);

        paramGcmRegId = paramGcmRegId.trim();
        GCMUser gcmUserInSystem = gcmMgr.getGcmUserByGcmRegId(paramGcmRegId);

        if (gcmUserInSystem == null) {
            System.out.println("Registering new GCM user");
            GCMUser gcmUser = new GCMUser();
            gcmUser.setGcmRegistrationId(paramGcmRegId);
            gcmUser.setName(paramName);
            gcmUser.setEmail(paramEmail);

            Date currentDate = new Date();
            gcmUser.setTimeStampDate(currentDate);

            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            gcmUser.setTimeStampTime(df.format(currentDate));

            gcmMgr.createGcmRecord(gcmUser);
        } else {
            System.out.println("User with GCM id :" + paramGcmRegId + " already registered");
            Date currentDate = new Date();
            gcmUserInSystem.setTimeStampDate(currentDate);
            
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            gcmUserInSystem.setTimeStampTime(df.format(currentDate));
            
            gcmMgr.updateGcmRecord(gcmUserInSystem);
        }


        return ActionConstants.SUCCESS;
    }

    public String getParamEmail() {
        return paramEmail;
    }

    public void setParamEmail(String paramEmail) {
        this.paramEmail = paramEmail;
    }

    public String getParamGcmRegId() {
        return paramGcmRegId;
    }

    public void setParamGcmRegId(String paramGcmRegId) {
        this.paramGcmRegId = paramGcmRegId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
