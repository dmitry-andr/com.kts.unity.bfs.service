package com.kts.facebookapp;

import com.kts.unity.config.web.backend.PlayerManager;
import com.kts.unity.config.web.backend.utils.FacebookSocialPluginSettings;
import com.kts.unity.config.web.entities.player.Player;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

public class AuthorizeWithSecurityTokenAction extends ActionSupport implements SessionAware, ParameterAware, ServletRequestAware {

    private String pluginSettingsRedirectUrl;
    
    private Map session;
    private Map parameters;
    private HttpServletRequest request;

    @Override
    public String execute() throws Exception {
        
        FacebookSocialPluginSettings pluginSettings = (FacebookSocialPluginSettings) AppContext.getContext().getBean(AppSpringBeans.FB_SOCIAL_PLUGIN_SETTINGS_BEAN);        
        request.setAttribute(ConfigParams.FBAPP_HOME_URL_LBL, pluginSettings.getRedirectURL());
        pluginSettingsRedirectUrl = pluginSettings.getRedirectURL();

        String playerInURL = getRequestParameterValue("usernm");
        String tokenInURL = getRequestParameterValue("authtoken");
        
        if((tokenInURL == null) || ("".equals(tokenInURL) || (playerInURL == null) || ("".equals(playerInURL)))){
            request.setAttribute(ConfigParams.FBAPP_MSG_LBL, "Wrong or expired security link ! Please request new one");
            return "reqest_new_token";
        }
        
        if(tokenInURL.length() <= (ConfigParams.LENGTH_FOR_EXTRA_PART_AUTO_GENERATED_PASSWORD_TOKEN_LENGTH + ConfigParams.AUTO_GENERATED_PASSWORD_LENGTH)){
            request.setAttribute(ConfigParams.FBAPP_MSG_LBL, "Wrong or expired security link ! Please request new one");
            return "reqest_new_token";
        }
        
        String password = tokenInURL.substring(ConfigParams.LENGTH_FOR_EXTRA_PART_AUTO_GENERATED_PASSWORD_TOKEN_LENGTH, (ConfigParams.LENGTH_FOR_EXTRA_PART_AUTO_GENERATED_PASSWORD_TOKEN_LENGTH + ConfigParams.AUTO_GENERATED_PASSWORD_LENGTH));
        
        PlayerManager playerMngr = (PlayerManager) AppContext.getContext().getBean(AppSpringBeans.PLAYER_MANAGER_BEAN);
        Player playerForEnteredName = playerMngr.getPlayerByName(playerInURL);
        if(password.equals(playerForEnteredName.getPassword())){
            session.put(ConfigParams.FBAPP_PLAYER_ID_LBL, playerForEnteredName.getId());
            return "success";
        }else{
            request.setAttribute(ConfigParams.FBAPP_MSG_LBL, "Wrong or expired security link ! Please request new one");
            return "reqest_new_token";
        }
    }

    
    
    private String getRequestParameterValue(String paramName) {
        Object varr = getParameters().get(paramName);
        if (varr == null) {
            return null;
        }
        return ((String[]) varr)[0];
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
    
    public String getPluginSettingsRedirectUrl() {
        return pluginSettingsRedirectUrl;
    }    
    
    
}
