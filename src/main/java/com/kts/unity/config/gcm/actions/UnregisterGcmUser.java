package com.kts.unity.config.gcm.actions;

import com.kts.unity.config.gcm.GCMManager;
import com.kts.unity.config.gcm.GCMUser;
import com.kts.unity.config.web.actions.ActionConstants;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.opensymphony.xwork2.ActionSupport;


public class UnregisterGcmUser extends ActionSupport{
    
    private String paramGcmRegId;
    
    @Override
    public String execute(){
        System.out.println("UnregisterGcmUser Passed params : "+ paramGcmRegId);
        if("".equals(paramGcmRegId)){
            return ActionConstants.ERROR;
        }
        
        GCMManager gcmMgr = (GCMManager) AppContext.getContext().getBean(AppSpringBeans.GCM_MANAGER_BEAN);
        
        GCMUser userToUnregister = gcmMgr.getGcmUserByGcmRegId(paramGcmRegId);
        
        if(userToUnregister != null){
            gcmMgr.deleteGcmRecord(userToUnregister.getRecordId());
        }
        
        return ActionConstants.SUCCESS;
    }

    public String getParamGcmRegId() {
        return paramGcmRegId;
    }
    public void setParamGcmRegId(String paramGcmRegId) {
        this.paramGcmRegId = paramGcmRegId;
    }
}
