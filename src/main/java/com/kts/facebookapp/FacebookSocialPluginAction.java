package com.kts.facebookapp;

import com.kts.facebookapp.dtos.ShareGameLinkDTO;
import com.kts.unity.config.gamification.GamificationPlugin;
import com.kts.unity.config.web.backend.PlayerManager;
import com.kts.unity.config.web.backend.utils.email.EmailType;
import com.kts.unity.config.web.backend.utils.email.EmailUtil;
import com.kts.unity.config.web.backend.utils.FacebookSocialPluginSettings;
import com.kts.unity.config.web.backend.utils.PasswordGenerator;
import com.kts.unity.config.web.backend.utils.ScoringRules;
import com.kts.unity.config.web.backend.utils.UrlUtils;
import com.kts.unity.config.web.entities.configs.Configuration;
import com.kts.unity.config.web.entities.FbUserWithGameData;
import com.kts.unity.config.web.entities.player.Player;
import com.kts.unity.config.web.entities.player.PlayerGameProfile;
import com.kts.unity.config.web.entities.player.PlayerGameStatistics;
import com.kts.unity.config.web.entities.player.PlayerOnlineData;
import com.kts.unity.config.web.entities.player.PlayerWithGameProfAndStatisticsAndChallengeData;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

public class FacebookSocialPluginAction extends ActionSupport implements SessionAware, ParameterAware, ServletRequestAware {

    private String pluginSettingsRedirectUrl;
    private String pluginSettingsAuthorizreUrl;
    private String statusMsg;
    private String accessToken = null;
    private Map session;
    private Map parameters;
    private HttpServletRequest request;
    private PlayerManager playerMngr;
    private Map<Integer, PlayerOnlineData> onlinePlayers;
    public static final String SECURE_LINK_URL_KEY = "secureLinkKey";
    public static final String PLAYER_EMAIL_TO_GET_SECURE_LINK_TO = "playerEmailToGetSecureLink";
    

    @Override
    public String execute() throws Exception {
        playerMngr = (PlayerManager) AppContext.getContext().getBean(AppSpringBeans.PLAYER_MANAGER_BEAN);
        FacebookSocialPluginSettings pluginSettings = (FacebookSocialPluginSettings) AppContext.getContext().getBean(AppSpringBeans.FB_SOCIAL_PLUGIN_SETTINGS_BEAN);
        pluginSettingsRedirectUrl = pluginSettings.getRedirectURL();
        pluginSettingsAuthorizreUrl = pluginSettings.getAuthorizeURL();
        onlinePlayers = playerMngr.getOnlinePlayers();
        //Map request = (Map) ActionContext.getContext().get("request");
        
        
        //Adding utility attributes of application settings
        request.setAttribute("externalResourcesUrl", pluginSettings.getSiteResourcesUrl());
        request.setAttribute("mobileExternalResourcesUrl", pluginSettings.getMobileSiteResourcesUrl());
        request.setAttribute("friendsSiteUrl", pluginSettings.getFriendsSiteURL());
        request.setAttribute("facebookAppServlet", pluginSettings.getFacebookAppServletName());
        request.setAttribute("displayNavigationBar", pluginSettings.getDisplaySiteNavigationBar());
        request.setAttribute("fb_app_id", pluginSettings.getAppid());        
        request.setAttribute("announcementText", Configuration.getInstance().getAnnouncementText());
        request.setAttribute("announcementDate", Configuration.getInstance().getAnnouncementDate());

        ShareGameLinkDTO shareLinkData = new ShareGameLinkDTO(pluginSettings.getSiteResourcesUrl(),
                pluginSettings.getSiteResourcesUrl() + "/images/stt_facebook.jpg",
                pluginSettings.getSiteResourcesUrl() + "/images/stt_facebook.jpg",
                "Hey, join me at cool online multiplayer game!",
                "Participate in battles with real opponents, achieve highest ratings!!!");
        
        request.setAttribute("shareLinkData", shareLinkData);
        
        
        
        System.out.println("Facebook plugin execute method started...");


        //read security code(remove last 4 string symbols) and get player Id by this code from Auth table. 
        System.out.println("Player Id in session : " + session.get(ConfigParams.FBAPP_PLAYER_ID_LBL));
        if (session.get(ConfigParams.FBAPP_PLAYER_ID_LBL) != null) {
            //this.playerId = (Integer) session.getAttribute(ConfigParams.FBAPP_PLAYER_CODE_LBL);
        } else {
            String secCodeInReq = getRequestParameterValue(ConfigParams.FBAPP_PLAYER_ID_LBL);
            System.out.println("Player Id(secCode) request parameter : " + secCodeInReq);

            if ((secCodeInReq != null) && (secCodeInReq.length() > ConfigParams.EXTRA_SYMBOLS_COUNT)) {
                String secCodeInitial = secCodeInReq.substring(0, (secCodeInReq.length() - ConfigParams.EXTRA_SYMBOLS_COUNT));
                System.out.println("Truncated secCode : " + secCodeInitial);
                Integer playerIdInReq = playerMngr.getPlayerIDBySecCode(secCodeInitial);
                session.put(ConfigParams.FBAPP_PLAYER_ID_LBL, playerIdInReq);
            }



        }


        //set attribute in session if browser is mobile
        Boolean isMobile = (Boolean) session.get(ConfigParams.FBAPP_IS_MOBBROWSER_TYPE_LBL);
        System.out.println("mob device before logic : " + isMobile);
        if (isMobile == null) {
            String mobileParamVal = getRequestParameterValue(ConfigParams.FBAPP_IS_MOBBROWSER_TYPE_LBL);
            if ((mobileParamVal != null) && ("true".equalsIgnoreCase(mobileParamVal))) {
                isMobile = true;
            } else {
                if (mobileParamVal == null) {
                    return resolveStrutsForward(null);
                }
                isMobile = false;
            }
            session.put(ConfigParams.FBAPP_IS_MOBBROWSER_TYPE_LBL, isMobile);
            System.out.println("Requested from mobile device : " + isMobile);
        }


        //facebook authorization
        this.accessToken = (String) session.get("rctSocialAppToken");
        System.out.println("Access token : " + this.accessToken);
        try {

            if (this.accessToken == null) {

                if (getRequestParameterValue("code") != null) {
                    String code = getRequestParameterValue("code");
                    System.out.println("Access code : " + code);

                    String accessTokenURLStr = pluginSettings.getAccessTokenURL(code);

                    System.out.println("Token url  : " + accessTokenURLStr);

                    UrlUtils urlUtil = new UrlUtils();

                    String tokenUrlContent = urlUtil.readUrlContent(accessTokenURLStr);
                    System.out.println("Token url content : " + tokenUrlContent);
                    this.accessToken = tokenUrlContent.split("&")[0].replaceFirst("access_token=", "");
                    System.out.println("Access token read from url : " + this.accessToken);

                    session.put("rctSocialAppToken", this.accessToken);


                } else {
                    System.out.println("in request");
                    //httpresponse.sendRedirect(pluginSettings.getAuthorizeURL());
                    statusMsg = getRequestParameterValue("fbstatus");
                    System.out.println("after sending request");
                    System.out.println("message : " + statusMsg);
                    return "authorize_redirect";
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        //end of FB authorization



        FacebookClient facebookClient = new DefaultFacebookClient(this.accessToken);

        User user = facebookClient.fetchObject("me", User.class);//me
        request.setAttribute("user", user);


        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
        ArrayList<User> friendsInFb = new ArrayList<User>();
        for (List<User> friendsList : myFriends) {
            for (User friend : friendsList) {
                //Add only ones that are not in game - create separate list for players in game to display them in separate list !!!!
                friendsInFb.add(friend);


                //User friendObj = facebookClient.fetchObject(friend.getId(), User.class);
                //httpresponse.getWriter().println("Friend id : " + friendObj.getId() + " -> name : " + friendObj.getName() + " -> user name : " + friendObj.getUsername());

                //!!!Depending on the amount of data it will improve performance to iterate through friend, but this object has less data fields initialized
                //httpresponse.getWriter().println("Friend id : " + friend.getId() + " -> name : " + friend.getName() + " -> user name : " + friend.getUsername());

            }
        }
        //check - if FB returned empty list - set list in app as null
        if(friendsInFb.size() == 0){
            friendsInFb = null;
        }


        String command = getRequestParameterValue(ConfigParams.FBAPP_COMMAND_LBL);

        
        
        if ("viewgamestat".equals(command)) {
            System.out.println("View friend game data action called!");
            String friendToBeDisplayed = getRequestParameterValue("friend");
            if (friendToBeDisplayed != null) {
                int playerInGameId = playerMngr.getPlayerIdByFbID(friendToBeDisplayed);
                if (playerInGameId > 0) {
                    Player playerInGame = playerMngr.getPlayerById(playerInGameId);
                    if (playerInGame == null) {//requested player ID that does not exits - set data object to null and display error message
                        return resolveStrutsForward("view_fb_friend_in_game_details");
                    }
                    PlayerWithGameProfAndStatisticsAndChallengeData friendGameData = new PlayerWithGameProfAndStatisticsAndChallengeData();
                    friendGameData.setFacebookId(friendToBeDisplayed);
                    friendGameData.setPlayer(playerInGame);
                    PlayerGameProfile gameProfile = playerMngr.getPlayerGameProfile(playerInGame.getName());
                    friendGameData.setGameProfile(gameProfile);
                    PlayerGameStatistics gameStat = playerMngr.getPlayerGameStatistics(playerInGame.getName());
                    friendGameData.setGamesStatistics(gameStat);
                    request.setAttribute("friendGameData", friendGameData);
                }
            }

            return resolveStrutsForward("view_fb_friend_in_game_details");
        }

        
        
        if ("getsecurelinkatemail".equals(command)) {

            request.setAttribute(ConfigParams.FBAPP_HOME_URL_LBL, pluginSettings.getRedirectURL());

            System.out.println("Get secure link command called !!!!!!!!!!!!");

            String playerName = getRequestParameterValue("getlink_for_playername");
            String playerEmailToGetSecureLinkTo = getRequestParameterValue("email_to_get_secure_link_to");

            if ((playerName != null) && (playerEmailToGetSecureLinkTo != null) && (!"".equals(playerName)) && (!"".equals(playerEmailToGetSecureLinkTo))) {
                System.out.println("player name : " + playerName.toUpperCase() + " ; email : " + playerEmailToGetSecureLinkTo);
                Player playerForEnteredName = playerMngr.getPlayerByName(playerName.toUpperCase());
                if (playerForEnteredName != null) {//player with provided name exists - send email with security link to provided email
                    
                    PasswordGenerator passGenerator = new PasswordGenerator();
                    
                    StringBuilder authSecurityToken = new StringBuilder();
                    authSecurityToken.append(passGenerator.getNewAutoGeneratedPassword(ConfigParams.LENGTH_FOR_EXTRA_PART_AUTO_GENERATED_PASSWORD_TOKEN_LENGTH));
                    authSecurityToken.append(playerForEnteredName.getPassword());
                    authSecurityToken.append(passGenerator.getNewAutoGeneratedPassword(ConfigParams.LENGTH_FOR_EXTRA_PART_AUTO_GENERATED_PASSWORD_TOKEN_LENGTH));
                    
                    StringBuilder authURL = new StringBuilder(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
                    authURL.append("/authwithtoken.action?usernm=");
                    authURL.append(playerForEnteredName.getName());
                    authURL.append("&authtoken=");
                    authURL.append(authSecurityToken.toString());
                    
                    EmailUtil emailUtil = (EmailUtil) AppContext.getContext().getBean(AppSpringBeans.EMAIL_UTIL_BEAN);
                    Map<String, String> eMailParams = new HashMap<String, String>();
                    eMailParams.put(SECURE_LINK_URL_KEY, authURL.toString());
                    eMailParams.put(PLAYER_EMAIL_TO_GET_SECURE_LINK_TO, playerEmailToGetSecureLinkTo);
                    boolean sendStatus = emailUtil.generateAndSendEmail(EmailType.PASSWORD_FB_PLUGIN_AUTH_TO_CLIENT, "msg text", eMailParams);
                    if(sendStatus){
                        request.setAttribute(ConfigParams.FBAPP_MSG_LBL, "!!! Your authorization link was sent. Please check you mail inbox.");
                    }else{
                        request.setAttribute(ConfigParams.FBAPP_MSG_LBL, "Sorry, email sending failed. Please try again or use another email address");
                    }
                    
/*                    
                    String facebookId = playerMngr.getFbId(playerForEnteredName.getId());
                    if (facebookId == null) {
                        playerMngr.createFbIdentityInGame(playerForEnteredName.getId(), user.getId());
                        //httpresponse.sendRedirect(pluginSettings.getRedirectURL());
                        return "redirect";
                    } else {
                        //httpresponse.sendRedirect(pluginSettings.getRedirectURL());
                        return "redirect";
                    }
*/                    

                } else {
                    request.setAttribute(ConfigParams.FBAPP_MSG_LBL, "No player with such name !!!");
                }

            } else {
                String initialParam = getRequestParameterValue("initial");//this param indicates that this is call from main page and login parameters are not passed yet. Do not display warning message
                Boolean isRequestedFromHomePage = false;
                try {//handle invalid values if someone will try to broke parameters
                    isRequestedFromHomePage = Boolean.parseBoolean(initialParam);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (isRequestedFromHomePage) {
                    request.setAttribute(ConfigParams.FBAPP_MSG_LBL, null);
                } else {
                    request.setAttribute(ConfigParams.FBAPP_MSG_LBL, "Fields cannot be empty !");
                }

            }

            if (isMobile) {
                return resolveStrutsForward("game_login_mobile");
            } else {
                return resolveStrutsForward("game_login");
            }
        }        
        
        


        //Get data vice versa as well i.e. when user logged in facebook but not logged in application !!!!

        if ((((Integer) session.get(ConfigParams.FBAPP_PLAYER_ID_LBL)) != null)
                && (((Integer) session.get(ConfigParams.FBAPP_PLAYER_ID_LBL)) != 0)) {

            Player player = playerMngr.getPlayerById((Integer) session.get(ConfigParams.FBAPP_PLAYER_ID_LBL));
            if (player == null) {
                return resolveStrutsForward("plugin_error");
            }

            System.out.println("pluginuserId  parameter : " + session.get(ConfigParams.FBAPP_PLAYER_ID_LBL));

            String facebbokId = playerMngr.getFbId((Integer) session.get(ConfigParams.FBAPP_PLAYER_ID_LBL));
            if (facebbokId == null) {
                //if user registered second time there will be facebook identity already
                //get facebook identity for facebook Id and remove old record if it exists
                int playerIdInGame = playerMngr.getPlayerIdByFbID(user.getId());
                if (playerIdInGame > 0) {
                    int operationStatus = playerMngr.removeFbIdentityInGame(playerIdInGame);
                    if (operationStatus <= 0) {
                        return resolveStrutsForward("plugin_error");
                    }
                }

                playerMngr.createFbIdentityInGame((Integer) session.get(ConfigParams.FBAPP_PLAYER_ID_LBL), user.getId());
                System.out.println("FB identity created");
            }

            PlayerWithGameProfAndStatisticsAndChallengeData playerGameData = new PlayerWithGameProfAndStatisticsAndChallengeData();
            playerGameData.setPlayer(player);
            playerGameData.setGameProfile(playerMngr.getPlayerGameProfile(player.getName()));
            playerGameData.setGamesStatistics(playerMngr.getPlayerGameStatistics(player.getName()));
            playerGameData.setLevelsProgress(playerMngr.getPlayerLevelProgress(player.getId()));

            if (playerGameData.getGameProfile().getRank() != 5) {
                int progressPercentage = 0;
                int currentScore = playerGameData.getGameProfile().getScores();
                ScoringRules rules = (ScoringRules) AppContext.getContext().getBean(AppSpringBeans.SCORING_RULES_BEAN);
                int nextRankMinScore = rules.getMinScoresForRank(playerGameData.getGameProfile().getRank() + 1);
                double perc = 100.0 - (nextRankMinScore - currentScore) * 100.0f / nextRankMinScore;
                progressPercentage = (int) perc;
                playerGameData.setNextRankProgress(progressPercentage);
            } else {
                playerGameData.setNextRankProgress(100);
            }


            this.addPlayerGameProfileToRequest(request, playerGameData);

            this.addFriendsInGameToRequest(request, friendsInFb, playerMngr);

            return resolveStrutsForward(null);
        } else {

            if (user != null) {//user logged in facebook but doesn't have account in game 
                int playerIdAtServer = playerMngr.getPlayerIdByFbID(user.getId());
                if (playerIdAtServer > 0) {
                    Player playerInGame = playerMngr.getPlayerById(playerIdAtServer);
                    if (playerInGame == null) {
                        return resolveStrutsForward("plugin_error");
                    }

                    PlayerWithGameProfAndStatisticsAndChallengeData playerData = new PlayerWithGameProfAndStatisticsAndChallengeData();
                    playerData.setPlayer(playerInGame);
                    playerData.setGameProfile(playerMngr.getPlayerGameProfile(playerInGame.getName()));
                    playerData.setGamesStatistics(playerMngr.getPlayerGameStatistics(playerInGame.getName()));
                    playerData.setLevelsProgress(playerMngr.getPlayerLevelProgress(playerInGame.getId()));


                    if (playerData.getGameProfile().getRank() != 5) {
                        int progressPercentage = 0;
                        int currentScore = playerData.getGameProfile().getScores();
                        ScoringRules rules = (ScoringRules) AppContext.getContext().getBean(AppSpringBeans.SCORING_RULES_BEAN);
                        int nextRankMinScore = rules.getMinScoresForRank(playerData.getGameProfile().getRank() + 1);
                        double perc = 100.0 - (nextRankMinScore - currentScore) * 100.0f / nextRankMinScore;
                        progressPercentage = (int) perc;
                        playerData.setNextRankProgress(progressPercentage);
                    } else {
                        playerData.setNextRankProgress(100);
                    }


                    this.addPlayerGameProfileToRequest(request, playerData);
                    this.addFriendsInGameToRequest(request, friendsInFb, playerMngr);

                } else {
                    request.setAttribute("not_in_game_yet", true);
                    this.addFriendsInGameToRequest(request, friendsInFb, playerMngr);
                }

                return resolveStrutsForward(null);
            } else {
                return resolveStrutsForward("plugin_error");
            }
        }
    }

    private void addFriendsInGameToRequest(HttpServletRequest request, ArrayList<User> friendsInFb, PlayerManager manager) {
        
        /*top players added in this method   */
        ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> challengeLeaders = null;
        challengeLeaders = manager.getPlayersWithGameProfAndChallengeData(GamificationPlugin.getWeekRankingList(), 7);
        request.setAttribute("challengeleadersdashboard", challengeLeaders);
        
        
        ArrayList<Integer> playersIDs = manager.getPlayersIDsForFacebookUsers(friendsInFb);
        System.out.println("Friends player IDs in game : " + playersIDs);
        ArrayList<FbUserWithGameData> friendsInGame = new ArrayList<FbUserWithGameData>();        
        if((playersIDs != null) && (playersIDs.size() > 0)){
            ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> playersGameDataAndStat = manager.getPlayersListWithGameDataAndStatistics(playersIDs);
            Map<Integer, String> facebookIdents = manager.getFacebookIdentsForUsers(playersIDs);

            for(int k = 0; k < playersGameDataAndStat.size(); k++){
                PlayerWithGameProfAndStatisticsAndChallengeData playerGameDataTmp = new PlayerWithGameProfAndStatisticsAndChallengeData();
                playerGameDataTmp.setPlayer(playersGameDataAndStat.get(k).getPlayer());
                playerGameDataTmp.setGameProfile(playersGameDataAndStat.get(k).getGameProfile());
                playerGameDataTmp.setGamesStatistics(playersGameDataAndStat.get(k).getGamesStatistics());
                
                User fbUser = null;
                for(int j = 0; j < friendsInFb.size(); j++){
                    if(friendsInFb.get(j).getId().equals(facebookIdents.get(playersGameDataAndStat.get(k).getPlayer().getId()))){
                        fbUser = friendsInFb.get(j);
                        break;
                    }                    
                }
                
                FbUserWithGameData fbUserWithGameData = new FbUserWithGameData(fbUser, playerGameDataTmp);
                
                if(onlinePlayers.containsKey(fbUserWithGameData.getGameData().getPlayer().getId())){
                    fbUserWithGameData.setOnline(true);
                }
                 
                 
                friendsInGame.add(fbUserWithGameData);                
            }
        }


        if (friendsInGame.isEmpty()) {
            friendsInGame = null;
        }
        request.setAttribute("friendsgame", friendsInGame);


        if (friendsInGame == null) {
            request.setAttribute("fbfriends", friendsInFb);
        } else {
            ArrayList<User> fbUsersWithoutInGame = new ArrayList<User>();
            for (User fbFriend : friendsInFb) {
                int playerIdinGame = manager.getPlayerIdByFbID(fbFriend.getId());
                if (playerIdinGame < 0) {
                    fbUsersWithoutInGame.add(fbFriend);
                }
            }
            friendsInFb = null;
            request.setAttribute("fbfriends", fbUsersWithoutInGame);
        }


    }

    /*
    private void dispatchRequest(HttpServletRequest request, HttpServletResponse httpresponse, String jspPageName) throws ServletException, IOException {
    if (jspPageName == null) {
    Boolean isMobile = (Boolean) request.getSession().getAttribute(ConfigParams.FBAPP_IS_MOBBROWSER_TYPE_LBL);
    if ((isMobile != null) && isMobile) {//dispatch to mobile template
    RequestDispatcher rd = request.getRequestDispatcher("facebook_app/mobile/mobile_rct_fb_app.jsp");
    rd.forward(request, httpresponse);
    } else {//dispatch to desktop template
    RequestDispatcher rd = request.getRequestDispatcher("facebook_app/rct_fb_app.jsp");
    rd.forward(request, httpresponse);
    }
    } else {
    RequestDispatcher rd = request.getRequestDispatcher(jspPageName);
    rd.forward(request, httpresponse);
    }
    }
     */
    private String resolveStrutsForward(String strutsForwardName) {
        if (strutsForwardName == null) {
            Boolean isMobile = (Boolean) getSession().get(ConfigParams.FBAPP_IS_MOBBROWSER_TYPE_LBL);
            if ((isMobile != null) && isMobile) {//dispatch to mobile template
                return "mobile_success";
            } else {//dispatch to desktop template
                return "success";
            }
        } else {
            return strutsForwardName;
        }
    }

    private void addPlayerGameProfileToRequest(HttpServletRequest request, PlayerWithGameProfAndStatisticsAndChallengeData profile) {
        request.setAttribute("playerGameData", profile);
        request.setAttribute("challengeRatingPosition",  GamificationPlugin.getCurrentPositionInWeekRating(profile.getPlayer().getId()));
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

    public String getPluginSettingsRedirectUrl() {
        return pluginSettingsRedirectUrl;
    }

    public String getPluginSettingsAuthorizreUrl() {
        return pluginSettingsAuthorizreUrl;
    }

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
    
    
}
