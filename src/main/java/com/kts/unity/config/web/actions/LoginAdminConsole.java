package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.AdminManager;
import com.kts.unity.config.web.entities.Admin;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class LoginAdminConsole extends ActionSupport implements SessionAware{

    private String userName;
    private String password;
    private String message;
    
    private Map session;
    
    @Override
    public String execute() throws Exception {

        
        AdminManager adminMngr = (AdminManager) AppContext.getContext().getBean(AppSpringBeans.ADMIN_MANAGER_BEAN);
        
        Admin adminAtLoginForm = new Admin();
        adminAtLoginForm.setName(this.userName);
        adminAtLoginForm.setPassword(this.password);
        
        if((this.userName == null) || (this.password == null)){
            this.message = "Fields cannot be empty !!!";
            return ActionConstants.OPERATION_FAILED;
        }
        
        int checkStatus = adminMngr.checkAdminCredentials(adminAtLoginForm);
        
        if(checkStatus == 1){
            session.put("authenticated", true);
            session.put("username", adminAtLoginForm.getName());
            return ActionConstants.SUCCESS;
        }else{
            this.message = "Wrong credentials!!!";
            return ActionConstants.OPERATION_FAILED;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map getSession() {
        return session;
    }
   
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
    
    
}
