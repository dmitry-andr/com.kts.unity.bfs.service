package com.kts.unity.config.web.backend.utils;

import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.ConfigParams;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class FacebookSocialPluginSettings {

    private String appid;
    private String appsecret;
    private String redirectURL;
    private String authorizeURL;
    private String accessTokenURL;
    private String accessTokenUrlFbApp;
    private String graphRootURL;
    private Boolean displaySiteNavigationBar;
    private String siteResourcesUrl;
    private String mobileSiteResourcesUrl;
    private String facebookAppServletName;
    private String friendsSiteURL;

    public int init() {
        int status = 0;

        Resource fbConfigResource = AppContext.getContext().getResource(ConfigParams.FB_SOCIAL_PLUGIN_CONFIG_PATH_NAME);
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(fbConfigResource);

            this.appid = props.getProperty("fb.appid");
            this.appsecret = props.getProperty("fb.appsecret");
            this.redirectURL = props.getProperty("fb.redirecturl");
            this.authorizeURL = MessageFormat.format(props.getProperty("fb.authorizeurl"), this.appid, this.redirectURL);
            this.accessTokenURL = MessageFormat.format(props.getProperty("fb.accesstokenurl"), this.appid, this.redirectURL, this.appsecret);
            this.accessTokenUrlFbApp = MessageFormat.format(props.getProperty("fb.applicationaccesstokenurl"), this.appid, this.appsecret);
            this.graphRootURL = props.getProperty("fb.graphrooturl");

            try {
                this.displaySiteNavigationBar = Boolean.parseBoolean(props.getProperty("fb.displaynavigbar"));
            } catch (Exception ex) {
                this.displaySiteNavigationBar = false;
                ex.printStackTrace();
            }

            siteResourcesUrl = props.getProperty("site_resources_url");
            mobileSiteResourcesUrl = props.getProperty("mobile_site_resources_url");
            facebookAppServletName = props.getProperty("fb_servlet_name");
            friendsSiteURL = props.getProperty("friends_site_url");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return status;
    }

    public String getAccessTokenURL(String code) {
        return accessTokenURL + "&code=" + code;
    }

    public String getAccessTokenUrlFbApp() {
        return accessTokenUrlFbApp;
    }

    public String getGraphRootURL() {
        return graphRootURL;
    }

    public String getAppid() {
        return appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public String getAuthorizeURL() {
        return authorizeURL;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public Boolean getDisplaySiteNavigationBar() {
        return displaySiteNavigationBar;
    }

    public String getSiteResourcesUrl() {
        return siteResourcesUrl;
    }

    public String getMobileSiteResourcesUrl() {
        return mobileSiteResourcesUrl;
    }

    public String getFacebookAppServletName() {
        return facebookAppServletName;
    }

    public String getFriendsSiteURL() {
        return friendsSiteURL;
    }
    
    

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("App ID : " + this.appid + "\n");
        out.append("App secret : " + this.appsecret + "\n");
        out.append("Redirect url : " + this.redirectURL + "\n");
        out.append("Auth url : " + this.authorizeURL + "\n");
        out.append("Access token url(without 'code' param) : " + this.accessTokenURL);
        out.append("more options available ... check source code :)");

        return out.toString();
    }
}
