package com.kts.unity.config.rest.ws;

import com.kts.facebookapp.dtos.DataForExperienceDTO;
import com.kts.unity.config.gamification.GamificationPlugin;
import com.kts.unity.config.gamification.ChallengeTypes;
import com.kts.unity.config.gamification.common.Challenge;
import com.kts.unity.config.gamification.common.ChallengeAward;
import com.kts.unity.config.gamification.common.ScoresRankingEntry;
import com.kts.unity.config.gamification.common.SniperRankingEntry;
import com.kts.unity.config.gcm.GCMSenderManager;
import com.kts.unity.config.web.backend.PlayerManager;
import com.kts.unity.config.web.backend.utils.CountriesList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.kts.unity.config.web.entities.configs.Configuration;
import com.kts.unity.config.web.entities.player.Player;
import com.kts.unity.config.web.entities.configs.RocketsConfiguration;
import com.kts.unity.config.web.entities.configs.WeaponConfiguration;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.ConfigParams;
import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.backend.utils.FacebookSocialPluginSettings;
import com.kts.unity.config.web.backend.utils.player.PlayerGameProfileTable;
import com.kts.unity.config.web.backend.utils.player.PlayerGameProfileTags;
import com.kts.unity.config.web.backend.utils.player.PlayerGameStatisticsTable;
import com.kts.unity.config.web.backend.utils.player.PlayerTable;
import com.kts.unity.config.web.backend.utils.ScoringRules;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.backend.utils.UrlUtils;
import com.kts.unity.config.web.backend.utils.WebServiceSecurityTokenManager;
import com.kts.unity.config.web.backend.utils.purchase.PurchaseTypes;
import com.kts.unity.config.web.entities.GameNavigationStatistics;
import com.kts.unity.config.web.entities.configs.AchievementsConfiguration;
import com.kts.unity.config.web.entities.configs.AvatarsConfiguration;
import com.kts.unity.config.web.entities.GameUsageStatistics;
import com.kts.unity.config.web.entities.LevelsProgress;
import com.kts.unity.config.web.entities.LoggingRecord;
import com.kts.unity.config.web.entities.PurchaseTransactionData;
import com.kts.unity.config.web.entities.configs.BotsConfiguration;
import com.kts.unity.config.web.entities.configs.PerkEntry;
import com.kts.unity.config.web.entities.configs.PerksConfiguration;
import com.kts.unity.config.web.entities.configs.RocketConfigEntry;
import com.kts.unity.config.web.entities.configs.WeaponEntry;
import com.kts.unity.config.web.entities.player.PlayerGameProfile;
import com.kts.unity.config.web.entities.player.PlayerGameStatistics;
import com.kts.unity.config.web.entities.player.PlayerOnlineData;
import com.kts.unity.config.web.entities.player.PlayerWithGameProfAndStatisticsAndChallengeData;
import com.kts.unity.config.web.utils.CommonUtils;
import com.kts.unity.config.web.utils.Dictionary;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.POST;


import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

//example of calling path http://localhost:8084/rct_serv/rest/rct_data_serv/getconfigparams?confName=param1
//Sets the path to base URL + /rct_data_serv
@Path("/rct_data_serv")
public class RctRestDataServ {

    @Context
    HttpServletRequest request;

    @Path("getoperationstamp")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getOperationStamp(@FormParam("param1") String param1,
            @FormParam("param2") String param2) {
                
        if (Settings.getSecurityTokenOperationSecKey().equals(param1)) {
            String securityTokenId = WebServiceSecurityTokenManager.getInstance().createSecurityToken().getTokenId();            
            return securityTokenId;
        } else {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }
    }

    @Path("appconfiguration")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getAppConfiguration(@FormParam("configName") String configName) {
    	
        if (Settings.getGetAppAllConfigsSecKey().equals(configName)) {
            return this.getAllConfigsAsXML();
        } else {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }
    }

    private String getAllConfigsAsXML() {
        /*Configurations used in the app on mobile app launch
        getConfigParams
        getRocketsConfiguration
        getCountries
        getWeaponConfiguration
        getPerksConfiguration
        getRankingRules
         */
        StringBuilder configOutput = new StringBuilder();
        //getConfigParams
        configOutput.append(Configuration.getInstance().getXmlOutput());
        //getRocketsConfiguration
        configOutput.append(RocketsConfiguration.getInstance().getXmlOutput());
        //getCountries
        CountriesList countriesList = (CountriesList) AppContext.getContext().getBean(AppSpringBeans.COUNTRIES_BEAN);
        configOutput.append(countriesList.getListInXML());
        //getWeaponConfiguration
        configOutput.append(WeaponConfiguration.getInstance().getXmlOutput());
        //getPerksConfiguration
        configOutput.append("\n");
        configOutput.append(PerksConfiguration.getInstance().getXmlOutput());
        //getRankingRules
        ScoringRules scoreRules = (ScoringRules) AppContext.getContext().getBean(AppSpringBeans.SCORING_RULES_BEAN);
        configOutput.append(scoreRules.rankingRulesXml());

        configOutput.append(AchievementsConfiguration.getInstance().getXmlOutput());

        configOutput.append("\n");
        configOutput.append(AvatarsConfiguration.getInstance().getXmlOutput());


        return configOutput.toString();
    }

    @Path("getconfigparams")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getConfigParams(@QueryParam("configName") String configName) {
        if (Settings.getGetConfigurationSecKey().equals(configName)) {
            return Configuration.getInstance().getXmlOutput();
        } else {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }
    }

    @Path("getcountries")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCountries() {
        CountriesList countriesList = (CountriesList) AppContext.getContext().getBean(AppSpringBeans.COUNTRIES_BEAN);

        return countriesList.getListInXML();
    }

    @Path("getrankingrules")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRankingRules(@QueryParam("operationSecCode") String operationSecCode) {

        if (!Settings.getRankingRulesSecKey().equals(operationSecCode)) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        ScoringRules scoreRules = (ScoringRules) AppContext.getContext().getBean(AppSpringBeans.SCORING_RULES_BEAN);

        return scoreRules.rankingRulesXml();
    }

    @Path("getrocketsconfiguration")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRocketsConfiguration(@QueryParam("configName") String configName) {
        if (Settings.getRocketsConfigSecKey().equals(configName)) {
            return RocketsConfiguration.getInstance().getXmlOutput();
        } else {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }
    }

    @Path("getweaponconfiguration")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getWeaponConfiguration(@QueryParam("configName") String configName) {
        if (Settings.getWeaponConfigurationSecKey().equals(configName)) {
            return WeaponConfiguration.getInstance().getXmlOutput();
        } else {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }
    }

    @Path("getperksconfiguration")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPerksConfiguration(@QueryParam("configName") String configName) {
        if (Settings.getPerksConfigurationSecKey().equals(configName)) {
            return PerksConfiguration.getInstance().getXmlOutput();
        } else {
            return null;
        }
    }

    @Path("getbotsconfiguration")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getBotsConfiguration(@FormParam("configName") String configName) {
        if (Settings.getBotsConfigurationSecKey().equals(configName)) {
            return BotsConfiguration.getInstance().getXmlOutput();
        } else {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }
    }

    @Path("logremoteexception")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String logRemoteException(@QueryParam("message") String message, @QueryParam("secCode") String secCode) {

        if ((message == null) || "".equals(message) || (secCode == null) || "".equals(secCode)) {
            return "8";//return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        if (!Settings.getLogRemoteExceptionSecKey().equals(secCode)) {
            return "9";//return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        int status = 0;

        LoggingRecord logRecord = new LoggingRecord();

        String playerName = null;
        if (message.contains("<plname>") && message.contains("</plname>")) {
            playerName = message.split("<plname>")[1].split("</plname>")[0];
        }
        if ((playerName != null) && !"".equals(playerName)) {
            logRecord.setPlayerName(playerName);
            message = message.replace(playerName, "");
        } else {
            logRecord.setPlayerName("tmpName");
        }

        String remoteAppVersion = null;
        if ((message.contains("<version>")) && message.contains("</version>")) {
            remoteAppVersion = message.split("<version>")[1].split("</version>")[0];
        }
        if ((remoteAppVersion != null) && !"".equals(remoteAppVersion)) {
            logRecord.setRemoteAppVersion(remoteAppVersion);
            message = message.replace(remoteAppVersion, "");
        } else {
            logRecord.setRemoteAppVersion("obsolete");
        }

        Date currentDate = new Date();
        logRecord.setTransactionDate(currentDate);
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        logRecord.setTransactionTime(df.format(currentDate));
        logRecord.setLogRegordText(message);
        status = getConfigManager().createRemoteLogRecord(logRecord);

        getConfigManager().updateRemoteLogsHealthMonitor();

        return String.valueOf(status);
    }

    
    @Path("authenticateplayerbypassword")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String authenticatePlayerByPassword(@FormParam("playerName") String playerName,
            @FormParam("playerPassword") String playerPassword,
            @FormParam("params") String params) {

        if ((playerName == null) || "".equals(playerName) || (playerPassword == null) || "".equals(playerPassword)) {
            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }
       
        String tokenHandShakeStatus = WebServiceSecurityTokenManager.getInstance().authorizeByTokenHandshake(params);
        if(!WebServiceSecurityTokenManager.TOKEN_REQUEST_VALID_TOKEN_STATUS.equals(tokenHandShakeStatus)){
            return tokenHandShakeStatus;//invalid token
        }

        String result = "";

        result = this.getPlayerManager().authenticatePlayerByPassword(playerName, playerPassword, 1);
        
        return result;
    }

    @Path("authenticateplayerbyemailsocial")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String authenticatePlayerByEmailSocial(@FormParam("playerName") String playerName,
            @FormParam("playerEmail") String playerEmail,
            @FormParam("socialId") String socialId,
            @FormParam("operationSecCode") String operationSecCode,
            @FormParam("permanentFlag") int permanentFlag,
            @FormParam("params") String params) {

        if ((playerName == null) || "".equals(playerName) || (operationSecCode == null)
                || "".equals(operationSecCode) || (permanentFlag < 0) || (permanentFlag > 1)) {

            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        if (!Settings.getAuthenticateByEmailSecKey().equals(operationSecCode)) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }
        
        String tokenHandShakeStatus = WebServiceSecurityTokenManager.getInstance().authorizeByTokenHandshake(params);
        if(!Settings.isDevelopmentModeEnabled() && !WebServiceSecurityTokenManager.TOKEN_REQUEST_VALID_TOKEN_STATUS.equals(tokenHandShakeStatus)){
            return tokenHandShakeStatus;//invalid token
        }

        Player playerInSystem = this.getPlayerManager().getPlayerByName(playerName);
        if (playerInSystem == null) {
            return ConfigParams.USER_DOES_NOT_EXIST_WS;
        }

        String fbIdInSystem = this.getPlayerManager().getFbId(playerInSystem.getId());

        String result = "";
        if ((playerEmail != null) && (!"".equals(playerEmail)) && (socialId != null) && (!"".equals(socialId))) {
            if ((playerInSystem.getEmail() != null) && (fbIdInSystem != null)) {
                result = this.getPlayerManager().authenticatePlayerByEmail(playerName, playerEmail, permanentFlag);
            } else {//create email if it is missing
                if (playerInSystem.getEmail() == null) {
                    result = this.getPlayerManager().authenticatePlayerByFbId(playerName, socialId, permanentFlag);
                    if (!result.equals(ConfigParams.USER_PASSW_AUTH_FAILED_WS)) {
                        playerInSystem.setEmail(playerEmail);
                        this.getPlayerManager().updatePlayerProfile(playerInSystem);
                    }
                } else {//user has email but has no FB identity or changed his/her nickname - create/change FB identity
                    result = this.getPlayerManager().authenticatePlayerByEmail(playerName, playerEmail, permanentFlag);
                    if (!result.equals(ConfigParams.USER_PASSW_AUTH_FAILED_WS)) {
                        int obsoletePlayerIdForFBId = this.getPlayerManager().getPlayerIdByFbID(socialId);
                        if (obsoletePlayerIdForFBId == -1) {
                            this.getPlayerManager().createFbIdentityInGame(playerInSystem.getId(), socialId);
                        } else {//player already has FB ID for other nickname - need to update FB identity
                            this.getPlayerManager().removeFbIdentityInGame(obsoletePlayerIdForFBId);
                            this.getPlayerManager().createFbIdentityInGame(playerInSystem.getId(), socialId);
                        }
                    }
                }
            }
        } else {//email or FbId were not passed as param
            if ((socialId == null) || "".equals(socialId)) {
                result = this.getPlayerManager().authenticatePlayerByEmail(playerName, playerEmail, permanentFlag);
            } else {
                result = this.getPlayerManager().authenticatePlayerByFbId(playerName, socialId, permanentFlag);
            }
        }


        if (!result.equals(ConfigParams.USER_PASSW_AUTH_FAILED_WS)) {
            this.getPlayerManager().updatePlayerOnlineStatus(playerName);
            if (Settings.isNotifyAdminOnPlayersGoOnline()) {
                String status = GCMSenderManager.getInstance().sendMessageToUsers("Player \"" + playerName + "\" become online!!! Join battle !!!", "DIMA-ANDR5;NEXUS4");
                System.out.println("Notified admins about online players available. Status - " + status);
            }

            //Notify registered users when someone goes online
            long currentTimeMillis = (new Date()).getTime();
            long expiringTimeframeMillis = GCMSenderManager.getBroadcastNotificationPeriodHours() * 3600000; //60min * 60sec * 1000
            if (((currentTimeMillis - GCMSenderManager.getLastOnlineOpponentsBroadcastTimestampMillis()) > expiringTimeframeMillis) 
                    && GCMSenderManager.getNotifyPlayersAboutOpponentsOnline()//global parameter to notify players about others online
                    && !"DARK_WARRIOR".equals(playerName) //service user - don't send notifications if such user authenticates
                    ) {
                
                Map<Integer, PlayerOnlineData> playersOnline = this.getPlayerManager().getOnlinePlayers();
                ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> playersWithEnabledNotification = this.getPlayerManager().getPlayersListWithOnlinePlayersNotificationEnabled();
                StringBuilder listOfPlayersForGCMNotification = null;
                if((playersWithEnabledNotification != null) && (playersWithEnabledNotification.size() > 0)){
                    listOfPlayersForGCMNotification = new StringBuilder();
                    for (PlayerWithGameProfAndStatisticsAndChallengeData playersWithEnabledNotificationEntry : playersWithEnabledNotification) {
                        int playerId = playersWithEnabledNotificationEntry.getPlayer().getId();
                        if((playersOnline.get(playerId)) == null){//this player is offline - notify him/her
                            if(listOfPlayersForGCMNotification.length() !=0){
                                listOfPlayersForGCMNotification.append(";");
                                listOfPlayersForGCMNotification.append(playersWithEnabledNotificationEntry.getPlayer().getName());
                            }else{//first entry to be added without ";"
                                listOfPlayersForGCMNotification.append(playersWithEnabledNotificationEntry.getPlayer().getName());
                            }
                        }
                    }//for
                    
                }
                
                System.out.println("Broadcast list  - " + listOfPlayersForGCMNotification);
                GCMSenderManager.setLastOnlineOpponentsBroadcastTimestampMillis(currentTimeMillis);
                String broadcastMessage = "There are players online!!! Join battle!!!";
                String status = "DEVELOPMENT MODE";
                status = GCMSenderManager.getInstance().sendMessageToUsers(broadcastMessage, listOfPlayersForGCMNotification.toString());//Don't send messages in development mode 
                System.out.println("Notified players about online opponents available. Status - " + status);
            }
        }



        return result;
    }

    @Deprecated
    @Path("authenticateplayerbyseccode")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String authenticatePlayerBySecCode(@FormParam("playerName") String playerName, @FormParam("securityCode") String securityCode) {
        int result = this.getPlayerManager().authenticatePlayerBySecCode(playerName, securityCode);

        if (result == 1) {
            this.getPlayerManager().updatePlayerOnlineStatus(playerName);
            if (Settings.isNotifyAdminOnPlayersGoOnline()) {
                GCMSenderManager.getInstance().sendMessageToUsers("Player \"" + playerName + "\" become online!!! Join battle !!!", "DIMA-ANDR5;NEXUS4");
            }
        }


        return String.valueOf(result);
    }

    @Path("createplayer")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String createPlayer(@FormParam("playerName") String playerName,
            @FormParam("playerEmail") String playerEmail,
            @FormParam("playerPassw") String playerPassw,
            @FormParam("fbid") String fbid,
            @FormParam("crPlayerSecCode") String crPlayerSecCode,
            @FormParam("params") String params) {

        if ((playerName == null) || ("".equals(playerName))) {
            return "3";
        }

        if (!Settings.getCreatePlayerSecKey().equals(crPlayerSecCode)) {
            return "6";
        }

        String tokenHandShakeStatus = WebServiceSecurityTokenManager.getInstance().authorizeByTokenHandshake(params);
        if(!WebServiceSecurityTokenManager.TOKEN_REQUEST_VALID_TOKEN_STATUS.equals(tokenHandShakeStatus)){
            return tokenHandShakeStatus;//invalid token
        }

        Player playerInSystem = this.getPlayerManager().getPlayerByName(playerName);
        if ((playerInSystem != null) || (Pattern.compile(Pattern.quote(playerName), Pattern.CASE_INSENSITIVE).matcher(Settings.getReservedPlayerNames()).find())) {
            return "2";
        }

        //do not allow to register user if FB id alredy used
        if ((fbid != null) && (!"".equals(fbid))) {
            int playerIdInSystemForFbId = this.getPlayerManager().getPlayerIdByFbID(fbid);
            if (playerIdInSystemForFbId != -1) {
                return "7";
            }
        }

        int status = 0;

        Player playerToCreate = new Player();
        playerToCreate.setName(playerName);
        playerToCreate.setEmail(playerEmail);
        playerToCreate.setPassword(playerPassw);

        status = this.getPlayerManager().createPlayer(playerToCreate, fbid);
        if (status == 1) {
            Player createdPlayer = this.getPlayerManager().getPlayerByName(playerName);
            PlayerGameProfile playerProfile = new PlayerGameProfile();
            playerProfile.setProfileId(createdPlayer.getId());
            playerProfile.setCountryCode("US");
            playerProfile.setShipType(301);
            playerProfile.setWeaponId(201);
            playerProfile.setRank(1);
            playerProfile.setScores(100);

            status = this.getPlayerManager().createPlayerGameProfile(playerProfile);
        } else {
            return String.valueOf(status);
        }

        return String.valueOf(status);
    }

    @Path("saveplayergameprofile")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String savePlayerGameProfile(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode,
            @FormParam("countryCode") String countryCode,
            @FormParam("shipTypeId") int shipTypeId,
            @FormParam("weaponId") int weaponId,
            @FormParam("avatar") int avatar,
            @FormParam("notifyAboutOnlinePlayers") int notifyAboutOnlinePlayers) {

        if ((playerName == null) || (secCode == null) || (countryCode == null) || (shipTypeId < 0)
                || (weaponId < 0) || "".equals(playerName) || "".equals(secCode)
                || (avatar < 500)) {
            return "5";
        }

        int secCodeStatus = this.getPlayerManager().authenticatePlayerBySecCode(playerName, secCode);
        if (secCodeStatus != 1) {
            return "2";//Not authorized code
        }
              

        int status = 0;


        PlayerGameProfile gameProfileInSystem = this.getPlayerManager().getPlayerGameProfile(playerName);
        if (gameProfileInSystem == null) {//no profile yet - should be created
            Player player = this.getPlayerManager().getPlayerByName(playerName);
            if (player == null) {
                return "4";
            }

            PlayerGameProfile playerProfile = new PlayerGameProfile();
            playerProfile.setProfileId(player.getId());
            playerProfile.setCountryCode(countryCode);
            playerProfile.setShipType(shipTypeId);
            playerProfile.setWeaponId(weaponId);
            playerProfile.setRank(1);
            playerProfile.setScores(100);
            playerProfile.setAvatar(String.valueOf(avatar));

            status = this.getPlayerManager().createPlayerGameProfile(playerProfile);
            if (status == 1) {
                return "3";//Player profile was created on first attempt to save data
            } else {
                return "0";
            }
        } else {
            gameProfileInSystem.setCountryCode(countryCode);
            gameProfileInSystem.setShipType(shipTypeId);
            gameProfileInSystem.setWeaponId(weaponId);
            //gameProfileInSystem.setRank(rank);
            //gameProfileInSystem.setScores(scores);
            gameProfileInSystem.setAvatar(String.valueOf(avatar));
            gameProfileInSystem.setNotifyAboutOnlinePlayers(notifyAboutOnlinePlayers);

            status = this.getPlayerManager().updatePlayerGameProfile(gameProfileInSystem);

            return String.valueOf(status);
        }
    }

    @Path("saveplayerpurchaseditemsids")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String savePlayerPurchasedItemsIds(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode,
            @FormParam("secStamp") String secStamp,
            @FormParam("itemIdsList") String itemIdsList) {
        
        return "Deprecated";
        
/*
        if ((playerName == null) || "".equals(playerName) || (secCode == null) || "".equals(secCode)
                || (secStamp == null) || "".equals(secStamp)) {

            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        int secCodeStatus = this.getPlayerManager().authenticatePlayerBySecCode(playerName, secCode);
        if ((secCodeStatus != 1) || !(ConfigParams.UPDATE_PUCHASED_ITEM_OPERATION_SEC_CODE.equals(secStamp))) {
            return "2";//Not authorized code
        }

        int status = 0;

        Player player = this.getPlayerManager().getPlayerByName(playerName);
        if (player == null) {
            return "4";
        }

        PlayerGameProfile gameProfileInSystem = this.getPlayerManager().getPlayerGameProfile(playerName);
        if (gameProfileInSystem != null) {
            String purchasedElements = gameProfileInSystem.getPuchasedItemsList();
            if ((purchasedElements == null) || "".equals(purchasedElements)) {
                purchasedElements = itemIdsList;
                gameProfileInSystem.setPuchasedItemsList(purchasedElements);
            } else {//player already has some purchased elements
                String[] itemIdsListElements;
                if (itemIdsList.contains(";")) {
                    itemIdsListElements = itemIdsList.split(";");
                } else {
                    itemIdsListElements = new String[]{itemIdsList};
                }

                for (int k = 0; k < itemIdsListElements.length; k++) {
                    if (!purchasedElements.contains(itemIdsListElements[k])) {
                        purchasedElements = purchasedElements + ";" + itemIdsListElements[k];
                        gameProfileInSystem.setPuchasedItemsList(purchasedElements);
                    } else {//player already has such item no need to update
                    }
                }
            }
        } else {// return error code
            return "0";
        }

        //gameProfileInSystem.setNumberOfShopVisits(gameProfileInSystem.getNumberOfShopVisits() + 1);

        status = this.getPlayerManager().updatePlayerGameProfile(gameProfileInSystem);

        return String.valueOf(status);
        */
    }

    @Path("savepurchasedcoins")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String savePurchasedCoins(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode,
            @FormParam("secStamp") String secStamp,
            @FormParam("coinsAmount") int coinsAmount,
            @FormParam("params") String params) {

        if ((playerName == null) || "".equals(playerName) || (secCode == null) || "".equals(secCode)
                || (secStamp == null) || "".equals(secStamp) || (coinsAmount < 1)) {

            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        String tokenHandShakeStatus = WebServiceSecurityTokenManager.getInstance().authorizeByTokenHandshake(params);
        if(!WebServiceSecurityTokenManager.TOKEN_REQUEST_VALID_TOKEN_STATUS.equals(tokenHandShakeStatus)){
            return tokenHandShakeStatus;//invalid token
        }
        
        int secCodeStatus = this.getPlayerManager().authenticatePlayerBySecCode(playerName, secCode);
        if ((secCodeStatus != 1) || !(Settings.getUpdatePurchasedCoinsOperationSecKey().equals(secStamp))) {
            return "2";//Not authorized code
        }
                

        int status = 0;

        Player player = this.getPlayerManager().getPlayerByName(playerName);
        if (player == null) {
            return "4";
        }

        PlayerGameProfile gameProfileInSystem = this.getPlayerManager().getPlayerGameProfile(playerName);
        if (gameProfileInSystem != null) {
            int coinsBalance = gameProfileInSystem.getCoinsBalance();
            if (coinsAmount >= 1) {
                coinsBalance = coinsBalance + coinsAmount;
                gameProfileInSystem.setCoinsBalance(coinsBalance);

                //updated player coins balance - record this interaction in DB
                PurchaseTransactionData transactionData = new PurchaseTransactionData();
                Date currentDate = new Date();
                transactionData.setTransactionDate(currentDate);
                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                transactionData.setTransactionTime(df.format(currentDate));
                transactionData.setPlayerId(player.getId());
                transactionData.setOperationType(PurchaseTypes.COINS_PURCHASE.getValue());
                transactionData.setPurchasedItem(444);
                transactionData.setCoinsBalanceChange(coinsAmount);

                this.getPlayerManager().savePlayerPurchaseRecord(transactionData);
            }
        } else {// return error code
            return "0";
        }


        //gameProfileInSystem.setNumberOfShopVisits(gameProfileInSystem.getNumberOfShopVisits() + 1);

        status = this.getPlayerManager().updatePlayerGameProfile(gameProfileInSystem);

        return String.valueOf(status);
    }

    @Path("savepurchaseditem")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String savePurchasedItem(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode,
            @FormParam("secStamp") String secStamp,
            @FormParam("itemId") int itemId,            
            @FormParam("params") String params) {
        
        if ((playerName == null) || "".equals(playerName) || (secCode == null) || "".equals(secCode)
                || (secStamp == null) || "".equals(secStamp) || (itemId < 0)) {

            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        String tokenHandShakeStatus = WebServiceSecurityTokenManager.getInstance().authorizeByTokenHandshake(params);
        if(!WebServiceSecurityTokenManager.TOKEN_REQUEST_VALID_TOKEN_STATUS.equals(tokenHandShakeStatus)){
            return tokenHandShakeStatus;//invalid token
        }
        
        int secCodeStatus = this.getPlayerManager().authenticatePlayerBySecCode(playerName, secCode);
        if ((secCodeStatus != 1) || !(Settings.getUpdatePurchasedInGameItemSecKey().equals(secStamp))) {
            return "2";//Not authorized code
        }

        int status = 0;

        Player player = this.getPlayerManager().getPlayerByName(playerName);
        if (player == null) {
            return "4";
        }



        PlayerGameProfile gameProfileInSystem = this.getPlayerManager().getPlayerGameProfile(playerName);
        if (gameProfileInSystem != null) {

            int itemPriceInSystem = this.getItemPriceForId(itemId);            
            if (itemPriceInSystem > gameProfileInSystem.getCoinsBalance()) {
                return "8";
            }

            String purchasedElements = gameProfileInSystem.getPuchasedItemsList();
            if ((purchasedElements == null) || "".equals(purchasedElements)) {
                purchasedElements = String.valueOf(itemId);
            } else {//player already has some purchased elements
                if (!purchasedElements.contains(String.valueOf(itemId))) {
                    purchasedElements = purchasedElements + ";" + String.valueOf(itemId);
                } else {//user laready has such item
                    return "7";
                }
            }
            gameProfileInSystem.setPuchasedItemsList(purchasedElements);
            gameProfileInSystem.setCoinsBalance(gameProfileInSystem.getCoinsBalance() - itemPriceInSystem);



            //updated player coins balance - record this interaction in DB
            PurchaseTransactionData transactionData = new PurchaseTransactionData();
            Date currentDate = new Date();
            transactionData.setTransactionDate(currentDate);
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            transactionData.setTransactionTime(df.format(currentDate));
            transactionData.setPlayerId(player.getId());
            transactionData.setOperationType(PurchaseTypes.IN_APP_ITEM_PURCHASE.getValue());
            transactionData.setPurchasedItem(itemId);
            transactionData.setCoinsBalanceChange((-1) * itemPriceInSystem);

            this.getPlayerManager().savePlayerPurchaseRecord(transactionData);


        } else {// return error code
            return "0";
        }


        //gameProfileInSystem.setNumberOfShopVisits(gameProfileInSystem.getNumberOfShopVisits() + 1);

        status = this.getPlayerManager().updatePlayerGameProfile(gameProfileInSystem);

        return String.valueOf(status);
    }

    //url example - http://rctuniapp.bfsmultiplayer.cloudbees.net/rest/rct_data_serv/updateplayersscoresandexperience?reporterPlayerName=NEXUS4&reporterPlayerAuthCode=f6b:1438d57b5ff:0&killerName=NEXUS4&killedName=DIMA-ANDR5&playersNumber=2&leftPlayersNumber=1&time=30&killerHits=6&killerShots=14&killerDamage=1&playerHits=1&playerShots=13&playerDamage=6
    @Path("updateplayersscoresandexperience")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String updatePlayersScoresAndExperience(
            @FormParam("reporterPlayerName") String reporterPlayerName,
            @FormParam("reporterPlayerAuthCode") String reporterPlayerAuthCode,
            @FormParam("killerName") String killerName,
            @FormParam("killedName") String killedName,
            @FormParam("playersNumber") int playersNumber,
            @FormParam("leftPlayersNumber") int leftPlayersNumber,
            @FormParam("time") int time,
            @FormParam("killerHits") int killerHits,
            @FormParam("killerShots") int killerShots,
            @FormParam("killerDamage") float killerDamage,
            @FormParam("playerHits") int playerHits,
            @FormParam("playerShots") int playerShots,
            @FormParam("playerDamage") float playerDamage,
            @FormParam("params") String params) {

        int status = 0;

        if ((reporterPlayerName == null) || (killerName == null) || (killedName == null)
                || "".equals(reporterPlayerName) || "".equals(killerName) || "".equals(killedName)) {
            return "5";//empty params
        }
        
        String tokenHandShakeStatus = WebServiceSecurityTokenManager.getInstance().authorizeByTokenHandshake(params);
        if(!WebServiceSecurityTokenManager.TOKEN_REQUEST_VALID_TOKEN_STATUS.equals(tokenHandShakeStatus)){
            return tokenHandShakeStatus;//invalid token
        }
        

        status = this.getPlayerManager().authenticatePlayerBySecCode(reporterPlayerName, reporterPlayerAuthCode);
        if (status != 1) {
            return String.valueOf(status);
        }

        DataForExperienceDTO experienceData = new DataForExperienceDTO();
        experienceData.setKillerDamage(killerDamage);
        experienceData.setKillerHits(killerHits);
        experienceData.setKillerShots(killerShots);
        experienceData.setPlayerDamage(playerDamage);
        experienceData.setPlayerHits(playerHits);
        experienceData.setPlayerShots(playerShots);
        experienceData.setTime(time);

        status = this.getPlayerManager().updatePlayersScoresAndExperience(killerName, killedName, playersNumber, leftPlayersNumber, experienceData);

        this.getPlayerManager().updatePlayerOnlineStatus(reporterPlayerName);

        return String.valueOf(status);
    }

    @Path("getmobappactiverankingdata")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getMobAppActiveRankingData(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode) {

        if (!Settings.getPlayerRankingDataSecKey().equals(secCode)) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        Challenge activeChallenge = GamificationPlugin.getChallenges().get(Dictionary.getValue(GamificationPlugin.ACTIVE_CHALLENGE_IN_GAME_KEY));
        if (activeChallenge == null) {
            return ConfigParams.NO_DATA_AVAILABLE_WS;
        }

        if (ChallengeTypes.DAY.getValue().equals(activeChallenge.getId())) {
            return this.getDayRankingForPlayer(playerName, secCode);
        }

        if (ChallengeTypes.WEEK.getValue().equals(activeChallenge.getId())) {

            return this.getWeekRankingForPlayer(playerName, secCode);
        }


        if (ChallengeTypes.SNIPER_OF_WEEK.getValue().equals(activeChallenge.getId())) {
            return this.getWeekSniperRankingForPlayer(playerName, secCode);
        }

        if (ChallengeTypes.TIME_PERIOD_TOURNAMENT.getValue().equals(activeChallenge.getId())) {
            return this.getTournamentRankingForPlayer(playerName, secCode);
        }


        return ConfigParams.NO_DATA_AVAILABLE_WS;

    }

    @Path("getweekrankingforplayer")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getWeekRankingForPlayer(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode) {

        if (!Settings.getPlayerRankingDataSecKey().equals(secCode)) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        Challenge activeChallenge = GamificationPlugin.getChallenges().get(ChallengeTypes.WEEK.getValue());
        if ((activeChallenge != null) && (activeChallenge.getIsActive() != 1)) {
            return ConfigParams.CHALLENGE_NOT_ACTIVE;
        }

        Map<String, String> additionalParamsForChallenge = new HashMap<String, String>();
        long currentTimeMillis = (new Date()).getTime();
        if ("true".equalsIgnoreCase(Dictionary.getValue(GamificationPlugin.WEEK_END_DATE_TIME_ENABLED_KEY))) {
            long tournamentDaysRemain = CommonUtils.getDaysRemainForMillisTime(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.WEEK_END_TIME_MILLIS_KEY)));
            additionalParamsForChallenge.put("tournamentDaysRemain", String.valueOf(tournamentDaysRemain));
            long tournamentHoursRemain = CommonUtils.getHoursRemainIn24Hours(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.WEEK_END_TIME_MILLIS_KEY)));
            additionalParamsForChallenge.put("tournamentHoursRemain", String.valueOf(tournamentHoursRemain));
            additionalParamsForChallenge.put("tournamentEndDate", Dictionary.getValue(GamificationPlugin.WEEK_END_DATE_KEY));
        } else {
            additionalParamsForChallenge.put("end_date_not_set", "end_date_not_set");
        }


        List<ScoresRankingEntry> fullWeekRankingList = GamificationPlugin.getWeekRankingList();
        Player playerRequestor = this.getPlayerManager().getPlayerByName(playerName);
        int startingIndexToDisplay = 0;
        int endIndexToDisplay = 0;
        ScoresRankingEntry playerRankingData = null;
        if (playerRequestor != null) {
            playerRankingData = GamificationPlugin.getPlayerWeekRanking(playerRequestor.getId());
        }

        if ((playerRequestor != null) && (playerRankingData != null)) {

            int positionChange = 0;

            if (playerRankingData != null) {//player has ranking data - need to find his index in list and output only his and adjacent data
                //find players' index in list
                int playerIndex = -1;
                for (int k = 0; k < fullWeekRankingList.size(); k++) {
                    if (fullWeekRankingList.get(k).getPlayerId() == playerRankingData.getPlayerId()) {
                        playerIndex = k;
                        break;
                    }
                }

                if ((playerIndex - Settings.getNumOfAdjacentPlayersInChlgRkgList()) > Settings.getNumOfPlayersInChlgTopList()) {
                    startingIndexToDisplay = playerIndex - Settings.getNumOfAdjacentPlayersInChlgRkgList();
                }

                if (((playerIndex + 1) + Settings.getNumOfAdjacentPlayersInChlgRkgList()) < fullWeekRankingList.size()) {
                    endIndexToDisplay = playerIndex + Settings.getNumOfAdjacentPlayersInChlgRkgList();
                } else {//list is shorter than player index + adjacent const
                    endIndexToDisplay = fullWeekRankingList.size() - 1;
                }

                int currentPosition = GamificationPlugin.getCurrentPositionInWeekRating(playerRankingData.getPlayerId());
                positionChange = playerRankingData.getPositionAtLastGame() - currentPosition;
                if (positionChange != 0) {
                    playerRankingData.setPositionAtLastGame(currentPosition);
                    GamificationPlugin.updatePlayerWeekRanking(playerRankingData);
                }
            }
            GamificationPlugin.checkIfWeekRatingIsOver();
            return this.getScoresRankingForPlayerXML(activeChallenge, playerRankingData, positionChange, fullWeekRankingList, startingIndexToDisplay, endIndexToDisplay, additionalParamsForChallenge);
        } else {//this method to be used by anonimous requestor - for example website
            if (fullWeekRankingList.size() > Settings.getNumOfPlayersInChlgTopList()) {
                endIndexToDisplay = Settings.getNumOfPlayersInChlgTopList() - 1;
            } else {
                endIndexToDisplay = fullWeekRankingList.size() - 1;
            }
            GamificationPlugin.checkIfWeekRatingIsOver();
            return this.getScoresRankingForPlayerXML(activeChallenge, null, 0, fullWeekRankingList, 0, endIndexToDisplay, additionalParamsForChallenge);
        }
    }

    @Path("getdayrankingforplayer")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getDayRankingForPlayer(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode) {

        if (!Settings.getPlayerRankingDataSecKey().equals(secCode)) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        Challenge activeChallenge = GamificationPlugin.getChallenges().get(ChallengeTypes.DAY.getValue());
        if ((activeChallenge != null) && (activeChallenge.getIsActive() != 1)) {
            return ConfigParams.CHALLENGE_NOT_ACTIVE;
        }
        
        
        Map<String, String> additionalParamsForChallenge = new HashMap<String, String>();
        long currentTimeMillis = (new Date()).getTime();
        if ("true".equalsIgnoreCase(Dictionary.getValue(GamificationPlugin.DAY_END_DATE_TIME_ENABLED_KEY))) {
            long tournamentDaysRemain = CommonUtils.getDaysRemainForMillisTime(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.DAY_END_TIME_MILLIS_KEY)));
            additionalParamsForChallenge.put("tournamentDaysRemain", String.valueOf(tournamentDaysRemain));
            long tournamentHoursRemain = CommonUtils.getHoursRemainIn24Hours(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.DAY_END_TIME_MILLIS_KEY)));
            additionalParamsForChallenge.put("tournamentHoursRemain", String.valueOf(tournamentHoursRemain));
            additionalParamsForChallenge.put("tournamentEndDate", Dictionary.getValue(GamificationPlugin.DAY_NEXT_AWARDING_DATE_KEY));
        } else {
            additionalParamsForChallenge.put("end_date_not_set", "end_date_not_set");
        }
        


        List<ScoresRankingEntry> fullDayRankingList = GamificationPlugin.getDayRankingList();
        Player playerRequestor = this.getPlayerManager().getPlayerByName(playerName);
        int startingIndexToDisplay = 0;
        int endIndexToDisplay = 0;
        ScoresRankingEntry playerRankingData = null;
        if (playerRequestor != null) {
            playerRankingData = GamificationPlugin.getPlayerDayRanking(playerRequestor.getId());
        }

        if ((playerRequestor != null) && (playerRankingData != null)) {
            int positionChange = 0;

            if (playerRankingData != null) {//player has ranking data - need to find his index in list and output only his and adjacent data
                //find players' index in list
                int playerIndex = -1;
                for (int k = 0; k < fullDayRankingList.size(); k++) {
                    if (fullDayRankingList.get(k).getPlayerId() == playerRankingData.getPlayerId()) {
                        playerIndex = k;
                        break;
                    }
                }

                if ((playerIndex - Settings.getNumOfAdjacentPlayersInChlgRkgList()) > Settings.getNumOfPlayersInChlgTopList()) {
                    startingIndexToDisplay = playerIndex - Settings.getNumOfAdjacentPlayersInChlgRkgList();
                }

                if (((playerIndex + 1) + Settings.getNumOfAdjacentPlayersInChlgRkgList()) < fullDayRankingList.size()) {
                    endIndexToDisplay = playerIndex + Settings.getNumOfAdjacentPlayersInChlgRkgList();
                } else {//list is shorter than player index + adjacent const
                    endIndexToDisplay = fullDayRankingList.size() - 1;
                }


                int currentPosition = GamificationPlugin.getCurrentPositionInDayRating(playerRankingData.getPlayerId());
                positionChange = playerRankingData.getPositionAtLastGame() - currentPosition;
                if (positionChange != 0) {
                    playerRankingData.setPositionAtLastGame(currentPosition);
                    GamificationPlugin.updatePlayerDayRanking(playerRankingData);
                }
            }
            GamificationPlugin.checkIfDayRatingIsOver();
            return this.getScoresRankingForPlayerXML(activeChallenge, playerRankingData, positionChange, fullDayRankingList, startingIndexToDisplay, endIndexToDisplay, additionalParamsForChallenge);
        } else {//this method to be used by anonimous requestor - for example website
            if (fullDayRankingList.size() > Settings.getNumOfPlayersInChlgTopList()) {
                endIndexToDisplay = Settings.getNumOfPlayersInChlgTopList() - 1;
            } else {
                endIndexToDisplay = fullDayRankingList.size() - 1;
            }
            GamificationPlugin.checkIfDayRatingIsOver();
            return this.getScoresRankingForPlayerXML(activeChallenge, null, 0, fullDayRankingList, 0, endIndexToDisplay, additionalParamsForChallenge);
        }
    }

    @Path("getweeksniperrankingforplayer")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getWeekSniperRankingForPlayer(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode) {


        if (!Settings.getPlayerRankingDataSecKey().equals(secCode)) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        Challenge activeChallenge = GamificationPlugin.getChallenges().get(ChallengeTypes.SNIPER_OF_WEEK.getValue());
        if ((activeChallenge != null) && (activeChallenge.getIsActive() != 1)) {
            return ConfigParams.CHALLENGE_NOT_ACTIVE;
        }


        List<ScoresRankingEntry> fullSniperRankingList = GamificationPlugin.getSniperRankingList();
        Player playerRequestor = this.getPlayerManager().getPlayerByName(playerName);
        int startingIndexToDisplay = 0;
        int endIndexToDisplay = 0;


        SniperRankingEntry playerRankingData = null;
        if (playerRequestor != null) {
            playerRankingData = GamificationPlugin.getPlayerSniperRanking(playerRequestor.getId());
        }
        if ((playerRequestor != null) && (playerRankingData != null)) {

            int positionChange = 0;

            if (playerRankingData != null) {//player has ranking data - need to find his index in list and output only his and adjacent data
                //find players' index in list
                int playerIndex = -1;
                for (int k = 0; k < fullSniperRankingList.size(); k++) {
                    if (fullSniperRankingList.get(k).getPlayerId() == playerRankingData.getPlayerId()) {
                        playerIndex = k;
                        break;
                    }
                }

                if ((playerIndex - Settings.getNumOfAdjacentPlayersInChlgRkgList()) > Settings.getNumOfPlayersInChlgTopList()) {
                    startingIndexToDisplay = playerIndex - Settings.getNumOfAdjacentPlayersInChlgRkgList();
                }

                if (((playerIndex + 1) + Settings.getNumOfAdjacentPlayersInChlgRkgList()) < fullSniperRankingList.size()) {
                    endIndexToDisplay = playerIndex + Settings.getNumOfAdjacentPlayersInChlgRkgList();
                } else {//list is shorter than player index + adjacent const
                    endIndexToDisplay = fullSniperRankingList.size() - 1;
                }

                int currentPosition = GamificationPlugin.getCurrentPositionInSniperRating(playerRankingData.getPlayerId());
                positionChange = playerRankingData.getPositionAtLastGame() - currentPosition;
                if (positionChange != 0) {
                    playerRankingData.setPositionAtLastGame(currentPosition);
                    GamificationPlugin.updatePlayerSniperRanking(playerRankingData);
                }
            }
            return this.getScoresRankingForPlayerXML(activeChallenge, playerRankingData, positionChange, fullSniperRankingList, startingIndexToDisplay, endIndexToDisplay, null);
        } else {//this method to be used by anonimous requestor - for example website
            if (fullSniperRankingList.size() > Settings.getNumOfPlayersInChlgTopList()) {
                endIndexToDisplay = Settings.getNumOfPlayersInChlgTopList() - 1;
            } else {
                endIndexToDisplay = fullSniperRankingList.size() - 1;
            }
            return this.getScoresRankingForPlayerXML(activeChallenge, null, 0, fullSniperRankingList, 0, endIndexToDisplay, null);
        }
    }

    @Path("gettournamentrankingforplayer")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getTournamentRankingForPlayer(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode) {

        if (!Settings.getPlayerRankingDataSecKey().equals(secCode)) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        Challenge activeChallenge = GamificationPlugin.getChallenges().get(ChallengeTypes.TIME_PERIOD_TOURNAMENT.getValue());
        if ((activeChallenge != null) && (activeChallenge.getIsActive() != 1)) {
            return ConfigParams.CHALLENGE_NOT_ACTIVE;
        }


        //check if tournament time is over. If it is over, clear data for this challenge, deactivate it and set WEEKLY rating as main
        boolean needToClearData = false;
        long currentTimeMillis = (new Date()).getTime();
        if ("true".equalsIgnoreCase(Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_DATE_TIME_ENABLED_KEY))) {
            if (currentTimeMillis > Long.parseLong(Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_TIME_MILLIS_KEY))) {
                needToClearData = true;
                //Deactivate challenge
                GamificationPlugin.updateChallengeActiveStatus(ChallengeTypes.TIME_PERIOD_TOURNAMENT.getValue(), 0);
                //set main challenge
                Dictionary.putValue(GamificationPlugin.ACTIVE_CHALLENGE_IN_GAME_KEY, GamificationPlugin.getChallenges().get(ChallengeTypes.WEEK.getValue()).getId());
                //Ensure that this challenge is activated
                GamificationPlugin.updateChallengeActiveStatus(ChallengeTypes.WEEK.getValue(), 1);
            }
        }


        List<ScoresRankingEntry> fullTournamentRankingList = GamificationPlugin.getTournamentRankingList();
        Player playerRequestor = this.getPlayerManager().getPlayerByName(playerName);
        int startingIndexToDisplay = 0;
        int endIndexToDisplay = 0;

        Map<String, String> additionalParamsForChallenge = new HashMap<String, String>();
        if ("true".equalsIgnoreCase(Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_DATE_TIME_ENABLED_KEY)) && (!needToClearData)) {
            long tournamentDaysRemain = CommonUtils.getDaysRemainForMillisTime(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_TIME_MILLIS_KEY)));
            additionalParamsForChallenge.put("tournamentDaysRemain", String.valueOf(tournamentDaysRemain));
            long tournamentHoursRemain = CommonUtils.getHoursRemainIn24Hours(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_TIME_MILLIS_KEY)));
            additionalParamsForChallenge.put("tournamentHoursRemain", String.valueOf(tournamentHoursRemain));
            long tournamentMinutesRemain = CommonUtils.getMinutesRemainingInHour(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_TIME_MILLIS_KEY)));
            additionalParamsForChallenge.put("tournamentMinutesRemain", String.valueOf(tournamentMinutesRemain));
            
            additionalParamsForChallenge.put("tournamentEndDate", Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_DATE_KEY));
        } else {
            additionalParamsForChallenge.put("end_date_not_set", "end_date_not_set");
        }


        ScoresRankingEntry playerRankingData = null;
        if (playerRequestor != null) {
            playerRankingData = GamificationPlugin.getPlayerTournamentRanking(playerRequestor.getId());
        }



        String xmlOutput = null;
        if ((playerRequestor != null) && (playerRankingData != null)) {

            int positionChange = 0;
            if (playerRankingData != null) {//player has ranking data - need to find his index in list and output only his and adjacent data
                //find players' index in list
                int playerIndex = -1;
                for (int k = 0; k < fullTournamentRankingList.size(); k++) {
                    if (fullTournamentRankingList.get(k).getPlayerId() == playerRankingData.getPlayerId()) {
                        playerIndex = k;
                        break;
                    }
                }

                if ((playerIndex - Settings.getNumOfAdjacentPlayersInChlgRkgList()) > Settings.getNumOfPlayersInChlgTopList()) {
                    startingIndexToDisplay = playerIndex - Settings.getNumOfAdjacentPlayersInChlgRkgList();
                }

                if (((playerIndex + 1) + Settings.getNumOfAdjacentPlayersInChlgRkgList()) < fullTournamentRankingList.size()) {
                    endIndexToDisplay = playerIndex + Settings.getNumOfAdjacentPlayersInChlgRkgList();
                } else {//list is shorter than player index + adjacent const
                    endIndexToDisplay = fullTournamentRankingList.size() - 1;
                }


                int currentPosition = GamificationPlugin.getCurrentPositionAtTournamentRating(playerRankingData.getPlayerId());
                positionChange = playerRankingData.getPositionAtLastGame() - currentPosition;
                if (positionChange != 0) {
                    playerRankingData.setPositionAtLastGame(currentPosition);
                    GamificationPlugin.updatePlayerTournamentRanking(playerRankingData);
                }
            }
            xmlOutput = this.getScoresRankingForPlayerXML(activeChallenge, playerRankingData, positionChange, fullTournamentRankingList, startingIndexToDisplay, endIndexToDisplay, additionalParamsForChallenge);
        } else {//this method to be used by anonimous requestor - for example website
            if (fullTournamentRankingList.size() > Settings.getNumOfPlayersInChlgTopList()) {
                endIndexToDisplay = Settings.getNumOfPlayersInChlgTopList() - 1;
            } else {
                endIndexToDisplay = fullTournamentRankingList.size() - 1;
            }

            xmlOutput = this.getScoresRankingForPlayerXML(activeChallenge, null, 0, fullTournamentRankingList, 0, endIndexToDisplay, additionalParamsForChallenge);
        }

        if (needToClearData) {
            GamificationPlugin.clearDataForChallenge(ChallengeTypes.TIME_PERIOD_TOURNAMENT.getValue());
        }

        return xmlOutput;
    }

    private String getScoresRankingForPlayerXML(Challenge challenge, ScoresRankingEntry playerRankingData, int positionChange, List<ScoresRankingEntry> scoresRankingList, int startingIndexToDisp, int endIndexToDisp, Map<String, String> params) {
        StringBuilder xmlOutput = new StringBuilder();
        xmlOutput.append("<rankingdata>");
        xmlOutput.append("\n");
        xmlOutput.append("<rankingtype>");
        xmlOutput.append(challenge.getId());
        xmlOutput.append("</rankingtype>");
        xmlOutput.append("\n");
        xmlOutput.append("<rankingtitle>");
        xmlOutput.append(challenge.getTitle());
        xmlOutput.append("</rankingtitle>");
        xmlOutput.append("\n");
        xmlOutput.append("<rankingintrotext>");
        xmlOutput.append(challenge.getIntroText());
        xmlOutput.append("</rankingintrotext>");
        xmlOutput.append("\n");


        xmlOutput.append("<params>");
        if ((params != null) && (!params.isEmpty())) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                xmlOutput.append("<" + entry.getKey() + ">");
                xmlOutput.append(entry.getValue());
                xmlOutput.append("</" + entry.getKey() + ">");
            }
        } else {
            xmlOutput.append("none");
        }
        xmlOutput.append("</params>");
        xmlOutput.append("\n");



        int endIndexForTopList = 0;
        int playerdataTagCounter = 1;
        xmlOutput.append("<playerslist>");
        if ((scoresRankingList != null) && (scoresRankingList.size() > 0)) {


            if ((scoresRankingList != null) && (scoresRankingList.size() > 0) && (startingIndexToDisp > 0)) {

                xmlOutput.append("\n");
                if (scoresRankingList.size() > Settings.getNumOfPlayersInChlgTopList()) {
                    endIndexForTopList = Settings.getNumOfPlayersInChlgTopList();
                } else {
                    endIndexForTopList = scoresRankingList.size();
                }


                for (int k = 0; k < endIndexForTopList; k++) {
                    ScoresRankingEntry rankingListEntry = scoresRankingList.get(k);
                    xmlOutput.append("<playerdata" + playerdataTagCounter + ">");
                    xmlOutput.append("<position>");
                    xmlOutput.append(k + 1);
                    xmlOutput.append("</position>");
                    xmlOutput.append("<plname>");
                    xmlOutput.append(rankingListEntry.getPlayerName());
                    xmlOutput.append("</plname>");
                    xmlOutput.append("<rank>");
                    xmlOutput.append(rankingListEntry.getRank());
                    xmlOutput.append("</rank>");
                    xmlOutput.append("<avatarid>");
                    xmlOutput.append(rankingListEntry.getAvatar());
                    xmlOutput.append("</avatarid>");

                    xmlOutput.append("<scores>");
                    xmlOutput.append(rankingListEntry.getScores());
                    xmlOutput.append("</scores>");

                    xmlOutput.append("</playerdata" + playerdataTagCounter + ">");

                    xmlOutput.append("\n");

                    playerdataTagCounter++;
                }

                xmlOutput.append("<startinglistposition>");
                xmlOutput.append(startingIndexToDisp + 1);
                xmlOutput.append("</startinglistposition>");
            }




            xmlOutput.append("\n");
            for (int k = startingIndexToDisp; k <= endIndexToDisp; k++) {
                ScoresRankingEntry rankingListEntry = scoresRankingList.get(k);
                xmlOutput.append("<playerdata" + playerdataTagCounter + ">");
                xmlOutput.append("<position>");
                xmlOutput.append(k + 1);
                xmlOutput.append("</position>");
                xmlOutput.append("<plname>");
                xmlOutput.append(rankingListEntry.getPlayerName());
                xmlOutput.append("</plname>");
                xmlOutput.append("<rank>");
                xmlOutput.append(rankingListEntry.getRank());
                xmlOutput.append("</rank>");
                xmlOutput.append("<avatarid>");
                xmlOutput.append(rankingListEntry.getAvatar());
                xmlOutput.append("</avatarid>");

                xmlOutput.append("<scores>");
                xmlOutput.append(rankingListEntry.getScores());
                xmlOutput.append("</scores>");

                if (playerRankingData != null) {
                    if (playerRankingData.getPlayerId() == rankingListEntry.getPlayerId()) {
                        xmlOutput.append("<poschange>");
                        xmlOutput.append(positionChange);
                        xmlOutput.append("</poschange>");
                    }
                }

                xmlOutput.append("</playerdata" + playerdataTagCounter + ">");

                xmlOutput.append("\n");

                playerdataTagCounter++;
            }

            xmlOutput.append("<numofplayers>");
            xmlOutput.append(endIndexForTopList + (endIndexToDisp - startingIndexToDisp + 1));
            xmlOutput.append("</numofplayers>");

            xmlOutput.append("\n");
        } else {
            xmlOutput.append(ConfigParams.NO_DATA_AVAILABLE_WS);
        }
        xmlOutput.append("<playerslist>");
        xmlOutput.append("\n");





        xmlOutput.append("</rankingdata>");

        return xmlOutput.toString();
    }

    @Path("getchallengesawardsforplayer")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getChallengesAwardsForPlayer(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode) {

        if (!Settings.getPlayerAwardsDataSecKey().equals(secCode)) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        Player playerRequestor = this.getPlayerManager().getPlayerByName(playerName);
        if (playerRequestor == null) {
            return "4";
        }

        ChallengeAward awards = GamificationPlugin.getChallengeAwardsForPlayer(playerRequestor.getId());
        if (awards == null) {
            return "no_awards_data_for_player";
        }

        StringBuilder xmlOut = new StringBuilder();
        xmlOut.append("<challengesawards>");
        xmlOut.append("\n");
        xmlOut.append("<plname>");
        xmlOut.append(playerRequestor.getName());
        xmlOut.append("</plname>");

        String[] awardEntries = awards.getAwardsList().split(";");

        xmlOut.append("<numofawards>");
        xmlOut.append(awardEntries.length);
        xmlOut.append("</numofawards>");

        xmlOut.append("\n");
        xmlOut.append("<awards>");
        xmlOut.append("\n");

        for (int k = 0; k < awardEntries.length; k++) {
            xmlOut.append("<awards" + k + ">");
            xmlOut.append("\n");
            String[] awardDataEntry = awardEntries[k].split("_");
            xmlOut.append("<id>");
            xmlOut.append(awardDataEntry[0]);
            xmlOut.append("</id>");
            xmlOut.append("\n");
            xmlOut.append("<date>");
            xmlOut.append(awardDataEntry[1]);
            xmlOut.append("</date>");
            xmlOut.append("\n");
            xmlOut.append("<scores>");
            xmlOut.append(awardDataEntry[2]);
            xmlOut.append("</scores>");
            xmlOut.append("\n");

            xmlOut.append("</awards" + k + ">");
            xmlOut.append("\n");
        }
        xmlOut.append("</awards>");
        xmlOut.append("\n");

        xmlOut.append("</challengesawards>");


        return xmlOut.toString();
    }

    @Path("updateplayerlevelprogress")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String updatePlayerLevelProgress(
            @FormParam("playerName") String playerName,
            @FormParam("playerAuthCode") String playerAuthCode,
            @FormParam("levelCompleted") int levelCompleted,
            @FormParam("progressCompletedPercents") int progressCompletedPercents,
            @FormParam("params") String params) {

        if ((playerName == null) || "".equals(playerName) || (playerAuthCode == null) || "".equals(playerAuthCode)
                || (levelCompleted <= 0) || (progressCompletedPercents < 0)) {

            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        int status = 0;

        String tokenHandShakeStatus = WebServiceSecurityTokenManager.getInstance().authorizeByTokenHandshake(params);
        if(!WebServiceSecurityTokenManager.TOKEN_REQUEST_VALID_TOKEN_STATUS.equals(tokenHandShakeStatus)){
            return tokenHandShakeStatus;//invalid token
        }
        
        status = this.getPlayerManager().authenticatePlayerBySecCode(playerName, playerAuthCode);
        if (status != 1) {
            return String.valueOf(status);
        }

        Player player = this.getPlayerManager().getPlayerByName(playerName);

        status = this.getPlayerManager().updatePlayerLevelProgress(player.getId(), levelCompleted, progressCompletedPercents);

        this.getPlayerManager().updatePlayerOnlineStatus(playerName);

        return String.valueOf(status);
    }

    @Path("getplayergameprofile")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlayerGameProfile(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode) {

        if ((playerName == null) || "".equals(playerName) || (secCode == null) || "".equals(secCode)) {
            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        int secCheckStatus = this.getPlayerManager().authenticatePlayerBySecCode(playerName, secCode);
        if (secCheckStatus != 1) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        PlayerGameProfile playerProfile = this.getPlayerManager().getPlayerGameProfile(playerName);

        String facebookId = null;
        if (playerProfile != null) {
            facebookId = this.getPlayerManager().getFbId(playerProfile.getProfileId());
        } else {
            return ConfigParams.NO_DATA_AVAILABLE_WS;
        }

        Player player = this.getPlayerManager().getPlayerByName(playerName);
        LevelsProgress levelsProgress = this.getPlayerManager().getPlayerLevelProgress(player.getId());//If player did not play singleplayer game this returns null




        return this.outputPlayerGameProfileXML(playerName, playerProfile, facebookId, levelsProgress, GamificationPlugin.getNewAwardsForPlayer(player.getId()));
    }

    @Path("getotherplayergameprofile")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getOtherPlayerGameProfile(
            @FormParam("requesterPlayerName") String requesterPlayerName,
            @FormParam("requesterSecCode") String requesterSecCode,
            @FormParam("playerName") String playerName) {

        if ((requesterPlayerName == null) || (requesterSecCode == null) || (playerName == null)
                || "".equals(requesterPlayerName) || "".equals(requesterSecCode) || "".equals(playerName)) {
            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }


        int secCheckStatus = this.getPlayerManager().authenticatePlayerBySecCode(requesterPlayerName, requesterSecCode);
        if (secCheckStatus != 1) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        PlayerGameProfile playerProfile = this.getPlayerManager().getPlayerGameProfile(playerName);
        String facebookId = null;
        if (playerProfile != null) {
            facebookId = this.getPlayerManager().getFbId(playerProfile.getProfileId());
        } else {
            return ConfigParams.NO_DATA_AVAILABLE_WS;
        }

        Player player = this.getPlayerManager().getPlayerByName(playerName);
        LevelsProgress levelsProgress = this.getPlayerManager().getPlayerLevelProgress(player.getId());

        return this.outputPlayerGameProfileXML(playerName, playerProfile, facebookId, levelsProgress, GamificationPlugin.getNewAwardsForPlayer(player.getId()));
    }

    @Path("getplayerlevelprogress")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlayerLevelProgress(
            @FormParam("playerName") String playerName,
            @FormParam("playerSecCode") String playerSecCode) {

        int status = 0;

        if ((playerName == null) || "".equals(playerName) || (playerSecCode == null) || "".equals(playerSecCode)) {
            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        status = this.getPlayerManager().authenticatePlayerBySecCode(playerName, playerSecCode);
        if (status != 1) {
            return ConfigParams.AUTH_FAILED;
        }

        Player player = this.getPlayerManager().getPlayerByName(playerName);

        return this.getPlayerManager().getPlayerLevelProgressinXML(player.getId());
    }

    @Path("getplayerfbfriends")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlayerFBFriends(@FormParam("playerName") String playerName,
            @FormParam("secCode") String secCode) {

        if ((playerName == null) || "".equals(playerName) || (secCode == null) || "".equals(secCode)) {
            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }


        int secCheckStatus = this.getPlayerManager().authenticatePlayerBySecCode(playerName, secCode);
        if (secCheckStatus != 1) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        

        //Check if facebook identity exists for this player otherwise launch workflow at mobile device of creating one
        Player requestorPlayer = this.getPlayerManager().getPlayerByName(playerName);
        String fbIdentityInGame = this.getPlayerManager().getFbId(requestorPlayer.getId());
        if (fbIdentityInGame == null) {
            return "no_fb_identity_in_game";
        }


        StringBuilder xmlOutput = new StringBuilder();

        UrlUtils utils = new UrlUtils();
        FacebookSocialPluginSettings pluginSettings = (FacebookSocialPluginSettings) AppContext.getContext().getBean(AppSpringBeans.FB_SOCIAL_PLUGIN_SETTINGS_BEAN);

        xmlOutput.append("<fbfriends>");

        String accessToken = utils.readUrlContent(pluginSettings.getAccessTokenUrlFbApp());
        //System.out.println("Access token : " + accessToken);

        String friendsForUserFBResponse = utils.readUrlContent(pluginSettings.getGraphRootURL() + fbIdentityInGame + "/friends?" + accessToken);
        //System.out.println("Friends for user response from FB : " + friendsForUserFBResponse);

        //Parse FB response
        int startSubStringIndex = friendsForUserFBResponse.indexOf("[");
        int endSubStringIndex = friendsForUserFBResponse.indexOf("]");
        String data = friendsForUserFBResponse.substring((startSubStringIndex + 1), endSubStringIndex);
        //System.out.println("data to process : " + data);

        String[] elemsWithID = data.split("\"\\},\\{\"");
        int tagAddedCounter = 0;
        for (int k = 0; k < elemsWithID.length; k++) {
            String facebookId;
            if (k != (elemsWithID.length - 1)) {
                facebookId = elemsWithID[k].split("id\":\"")[1];
            } else {//remove closing bracket with quotes - "}  - for last element with empty string
                facebookId = elemsWithID[k].split("id\":\"")[1].replace("\"}", "");
            }
            System.out.println((k + 1) + " - FB ID - " + facebookId);
            int playerIdByFbId = this.getPlayerManager().getPlayerIdByFbID(facebookId);
            if (playerIdByFbId > 0) {
                xmlOutput.append("<player" + (tagAddedCounter + 1) + ">");

                xmlOutput.append("<fbid>");
                xmlOutput.append(facebookId);
                xmlOutput.append("</fbid>");

                xmlOutput.append("<nameingame>");
                Player playerInGame = this.getPlayerManager().getPlayerById(playerIdByFbId);
                xmlOutput.append(playerInGame.getName());
                xmlOutput.append("</nameingame>");

                xmlOutput.append("</player" + (tagAddedCounter + 1) + ">");
                tagAddedCounter++;
            }
        }
        //END parse FB response

        if (tagAddedCounter == 0) {
            xmlOutput.append("no_fb_friends_installed_fbapp");
        }

        xmlOutput.append("</fbfriends>");

        return xmlOutput.toString();
    }

    @Path("gettopplayerslist")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getTopPlayersList(
            @FormParam("reqPlayerName") String reqPlayerName,
            @FormParam("reqAuthCode") String reqAuthCode,
            @FormParam("pageNumber") int pageNumber,
            @FormParam("numItemsOnPage") int numItemsOnPage) {

        if ((reqPlayerName == null) || "".equals(reqPlayerName) || (reqAuthCode == null) || "".equals(reqAuthCode)
                || (pageNumber <= 0) || (numItemsOnPage <= 0)) {
            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        int authStatus = this.getPlayerManager().authenticatePlayerBySecCode(reqPlayerName, reqAuthCode);
        if (authStatus != 1) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> topPlayersList = null;
        topPlayersList = this.getPlayerManager().getTopPlayersData(pageNumber, numItemsOnPage);
        if (topPlayersList == null) {
            return ConfigParams.NO_DATA_AVAILABLE_WS;
        }

        return this.getTopPlayersXMLOutput("topplayers", topPlayersList);
    }

    @Path("gettopplayerslistsingleplayermode")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getTopPlayersListSinglePlayerMode(
            @FormParam("reqPlayerName") String reqPlayerName,
            @FormParam("reqAuthCode") String reqAuthCode,
            @FormParam("pageNumber") int pageNumber,
            @FormParam("numItemsOnPage") int numItemsOnPage) {

        if ((reqPlayerName == null) || "".equals(reqPlayerName) || (reqAuthCode == null) || "".equals(reqAuthCode)
                || (pageNumber <= 0) || (numItemsOnPage <= 0)) {

            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }


        int authStatus = this.getPlayerManager().authenticatePlayerBySecCode(reqPlayerName, reqAuthCode);
        if (authStatus != 1) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }

        ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> topPlayersList = null;
        topPlayersList = this.getPlayerManager().getTopPlayersDataInSinglePlayerMode(pageNumber, numItemsOnPage);
        if (topPlayersList == null) {
            return ConfigParams.NO_DATA_AVAILABLE_WS;
        }

        return this.getTopPlayersXMLOutput("topplayerssingleplayermode", topPlayersList);
    }

    private String getTopPlayersXMLOutput(String rootTag, ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> topPlayersList) {

        Map<Integer, PlayerOnlineData> onlinePlayers = this.getPlayerManager().getOnlinePlayers();

        StringBuilder xmlOutput = new StringBuilder();
        xmlOutput.append("<" + rootTag + ">");
        xmlOutput.append("<pages>");
        xmlOutput.append(PlayerWithGameProfAndStatisticsAndChallengeData.getNumOfPages());
        xmlOutput.append("</pages>");
        xmlOutput.append("\n");

        for (int k = 0; k < topPlayersList.size(); k++) {
            xmlOutput.append("<playerentry" + (k + 1) + ">");

            xmlOutput.append("<" + PlayerTable.NAME + ">");
            xmlOutput.append(topPlayersList.get(k).getPlayer().getName());
            xmlOutput.append("</" + PlayerTable.NAME + ">");

            xmlOutput.append("<" + PlayerGameProfileTable.SCORES + ">");
            xmlOutput.append(topPlayersList.get(k).getGameProfile().getScores());
            xmlOutput.append("</" + PlayerGameProfileTable.SCORES + ">");

            xmlOutput.append("<" + PlayerGameProfileTable.RANK + ">");
            xmlOutput.append(topPlayersList.get(k).getGameProfile().getRank());
            xmlOutput.append("</" + PlayerGameProfileTable.RANK + ">");

            xmlOutput.append("<" + PlayerGameProfileTable.COUNTRY_CODE + ">");
            xmlOutput.append(topPlayersList.get(k).getGameProfile().getCountryCode());
            xmlOutput.append("</" + PlayerGameProfileTable.COUNTRY_CODE + ">");

            xmlOutput.append("<" + PlayerGameProfileTable.AVATAR + ">");
            xmlOutput.append(topPlayersList.get(k).getGameProfile().getAvatar());
            xmlOutput.append("</" + PlayerGameProfileTable.AVATAR + ">");

            xmlOutput.append("<singleplayerpoints>");
            if (topPlayersList.get(k).getLevelsProgress() != null) {
                xmlOutput.append(topPlayersList.get(k).getLevelsProgress().getLevelsProgressRankingPoints());
            } else {
                xmlOutput.append("no_data");
            }
            xmlOutput.append("</singleplayerpoints>");

            xmlOutput.append("<" + PlayerGameProfileTags.FACEBOOK_ID + ">");
            if ((topPlayersList.get(k).getFacebookId() != null) && (!"".equals(topPlayersList.get(k).getFacebookId()))) {
                xmlOutput.append(topPlayersList.get(k).getFacebookId());
            } else {
                xmlOutput.append(ConfigParams.NO_FACEBOOK_ID_TXT);
            }
            xmlOutput.append("</" + PlayerGameProfileTags.FACEBOOK_ID + ">");

            xmlOutput.append("<onlinestatus>");
            if (onlinePlayers.containsKey(topPlayersList.get(k).getPlayer().getId())) {
                xmlOutput.append(1);
            } else {
                xmlOutput.append(0);
            }
            xmlOutput.append("</onlinestatus>");


            xmlOutput.append("</playerentry" + (k + 1) + ">");
            xmlOutput.append("\n");
        }

        xmlOutput.append("</" + rootTag + ">");

        return xmlOutput.toString();
    }

    @Path("getplayergamestatistics")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlayerGameStatistics(
            @FormParam("requesterPlayerName") String requesterPlayerName,
            @FormParam("authCode") String authCode,
            @FormParam("playerNameStat") String playerNameStat) {

        if ((requesterPlayerName == null) || "".equals(requesterPlayerName) || (authCode == null) || "".equals(authCode)
                || (playerNameStat == null) || "".equals(playerNameStat)) {
            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        int authStatus = this.getPlayerManager().authenticatePlayerBySecCode(requesterPlayerName, authCode);
        if (authStatus != 1) {
            return ConfigParams.NOT_AUTH_RESPONSE_WS;
        }
        PlayerGameStatistics stat = this.getPlayerManager().getPlayerGameStatistics(playerNameStat);
        if (stat == null) {
            return ConfigParams.NO_DATA_AVAILABLE_WS;
        }

        StringBuilder xmlOutput = new StringBuilder();

        xmlOutput.append("<" + PlayerGameStatisticsTable.TBL_NAME + ">");

        xmlOutput.append("<playerName>");//hardcoded tag name as it is for debug purposes only
        xmlOutput.append(playerNameStat);
        xmlOutput.append("</playerName>");

        String tagName;
        for (int k = 0; k < stat.getKilledbyrank().length; k++) {
            tagName = PlayerGameStatisticsTable.KILLED_BY_RANK + (k + 1);
            xmlOutput.append("<" + tagName + ">");
            xmlOutput.append(stat.getKilledbyrank()[k]);
            xmlOutput.append("</" + tagName + ">");
        }
        for (int k = 0; k < stat.getKilledrank().length; k++) {
            tagName = PlayerGameStatisticsTable.KILLED_RANK + (k + 1);
            xmlOutput.append("<" + tagName + ">");
            xmlOutput.append(stat.getKilledrank()[k]);
            xmlOutput.append("</" + tagName + ">");
        }

        xmlOutput.append("</" + PlayerGameStatisticsTable.TBL_NAME + ">");

        return xmlOutput.toString();
    }

    @Path("savegameusagestatistics")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String saveGameUsageStatistics(
            @FormParam("playerName") String playerName,
            @FormParam("playerAuthCode") String playerAuthCode,
            @FormParam("appIicationId") String appIicationId,
            @FormParam("secondsOfUsage") int secondsOfUsage,
            @FormParam("averRespTime") int averRespTime,
            @FormParam("maxRespTime") int maxRespTime,
            @FormParam("minRespTime") int minRespTime,
            @FormParam("averPing") int averPing,
            @FormParam("maxPing") int maxPing,
            @FormParam("minPing") int minPing,
            @FormParam("averFps") int averFps,
            @FormParam("maxFps") int maxFps,
            @FormParam("minFps") int minFps,
            @FormParam("gameMode") int gameMode) {


        if ((playerName == null) || "".equals(playerName) || (playerAuthCode == null) || "".equals(playerAuthCode)
                || (secondsOfUsage < 0) || (averRespTime < 0) || (maxRespTime < 0) || (minRespTime < 0) || (averPing < 0)
                || (maxPing < 0) || (minPing < 0) || (averFps < 0) || (maxFps < 0) || (minFps < 0) || ((gameMode != 1) && (gameMode != 2))) {

            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }


        int secCodeStatus = this.getPlayerManager().authenticatePlayerBySecCode(playerName, playerAuthCode);
        if (secCodeStatus != 1) {
            return "2";//Not authorized code
        }

        int status = 0;

        GameUsageStatistics stat = new GameUsageStatistics();
        stat.setApplicationId(appIicationId);
        stat.setPlayerName(playerName);

        Date currentDate = new Date();

        stat.setDataCollectionDate(currentDate);

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        stat.setDataCollectionTime(df.format(currentDate));

        stat.setLastUsageTime(secondsOfUsage);
        stat.setAverageRespWait(averRespTime);
        stat.setMaxRespWait(maxRespTime);
        stat.setMinRespWait(minRespTime);
        stat.setAverPing(averPing);
        stat.setMaxPing(maxPing);
        stat.setMinPing(minPing);
        stat.setAverFps(averFps);
        stat.setMaxFps(maxFps);
        stat.setMinFps(minFps);


        status = getConfigManager().saveGameUsageStatisticsRecord(stat, gameMode);

        this.getPlayerManager().updatePlayerOnlineStatus(playerName);

        return String.valueOf(status);
    }

    @Path("savegamenavigationstatistics")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String saveGameNavigationStatistics(
            @FormParam("playerName") String playerName,
            @FormParam("playerAuthCode") String playerAuthCode,
            @FormParam("appIicationId") String appIicationId,
            @FormParam("numOfHangarVisits") int numOfHangarVisits,
            @FormParam("numOfCoinsButtonPress") int numOfCoinsButtonPress,
            @FormParam("fbShareAcheivement") int fbShareAcheivement,
            @FormParam("tournamentsButton") int tournamentsButton,
            @FormParam("levelSquare") int levelSquare,
            @FormParam("levelTriangle") int levelTriangle,
            @FormParam("levelHex") int levelHex,
            @FormParam("fbFriendsInviteBtn") int fbFriendsInviteBtn,
            @FormParam("musicOffBtnPresses") int musicOffBtnPresses,
            
            @FormParam("coins100") int coins100,
            @FormParam("coins250") int coins250,
            @FormParam("coins550") int coins550,
            @FormParam("training") int training,
            @FormParam("buy") int buy,
            @FormParam("avatar") int avatar,
            @FormParam("ship") int ship,
            @FormParam("weapon") int weapon,
            @FormParam("perks") int perks) {


        if ((playerName == null) || "".equals(playerName) || (playerAuthCode == null) || "".equals(playerAuthCode)
                || (numOfHangarVisits < 0) || (numOfCoinsButtonPress < 0) || (fbShareAcheivement < 0) || (tournamentsButton < 0)
                || (levelSquare < 0) || (levelTriangle < 0) || (levelHex < 0) || (fbFriendsInviteBtn < 0) || (musicOffBtnPresses < 0)
                || (coins100 < 0) || (coins250 < 0) || (coins550 < 0) || (training < 0) || (buy < 0) || (avatar < 0)
                || (ship < 0) || (weapon < 0) || (perks < 0)) {

            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }


        int secCodeStatus = this.getPlayerManager().authenticatePlayerBySecCode(playerName, playerAuthCode);
        if (secCodeStatus != 1) {
            return "2";//Not authorized code
        }

        int status = 0;

        GameNavigationStatistics stat = new GameNavigationStatistics();
        stat.setApplicationId(appIicationId);
        stat.setPlayerName(playerName);

        Date currentDate = new Date();

        stat.setDataCollectionDate(currentDate);

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        stat.setDataCollectionTime(df.format(currentDate));

        stat.setNumOfHangarVisits(numOfHangarVisits);
        stat.setNumOfCoinsButtonPress(numOfCoinsButtonPress);
        stat.setFbShareAcheivement(fbShareAcheivement);
        stat.setTournamentsButton(tournamentsButton);
        stat.setLevelSquare(levelSquare);
        stat.setLevelTriangle(levelTriangle);
        stat.setLevelHex(levelHex);
        stat.setFbFriends(fbFriendsInviteBtn);
        stat.setMusicOffBtn(musicOffBtnPresses);
        
        
        stat.setCoins100(coins100);
        stat.setCoins250(coins250);
        stat.setCoins550(coins550);
        stat.setTraining(training);
        stat.setBuy(buy);
        stat.setAvatar(avatar);
        stat.setShip(ship);
        stat.setWeapon(weapon);
        stat.setPerks(perks);


        status = this.getConfigManager().saveGameNavigationStatisticsRecord(stat);

        this.getPlayerManager().updatePlayerOnlineStatus(playerName);

        return String.valueOf(status);
    }

    private String outputPlayerGameProfileXML(String playerName, PlayerGameProfile playerProfile, String facebookId, LevelsProgress singlePlProgress, ChallengeAward challengeAwards) {
        StringBuilder xmlOutput = new StringBuilder();

        if (playerProfile == null) {
            return ConfigParams.NO_DATA_AVAILABLE_WS;
        }

        xmlOutput.append("<" + PlayerGameProfileTags.ROOT + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.PLAYER_NAME + ">");
        xmlOutput.append(playerName);
        xmlOutput.append("</" + PlayerGameProfileTags.PLAYER_NAME + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.COUNTRY_CODE + ">");
        xmlOutput.append(playerProfile.getCountryCode());
        xmlOutput.append("</" + PlayerGameProfileTags.COUNTRY_CODE + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.SHIP_TYPE + ">");
        xmlOutput.append(playerProfile.getShipType());
        xmlOutput.append("</" + PlayerGameProfileTags.SHIP_TYPE + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.WEAPON_ID + ">");
        xmlOutput.append(playerProfile.getWeaponId());
        xmlOutput.append("</" + PlayerGameProfileTags.WEAPON_ID + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.RANK + ">");
        xmlOutput.append(playerProfile.getRank());
        xmlOutput.append("</" + PlayerGameProfileTags.RANK + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.SCORES + ">");
        xmlOutput.append(playerProfile.getScores());
        xmlOutput.append("</" + PlayerGameProfileTags.SCORES + ">");
        
        xmlOutput.append("\n");
        
        xmlOutput.append("<" + PlayerGameProfileTags.NOTIFY_ON_PLAYERS_ONLINE + ">");
        xmlOutput.append(playerProfile.getNotifyAboutOnlinePlayers());
        xmlOutput.append("</" + PlayerGameProfileTags.NOTIFY_ON_PLAYERS_ONLINE + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<singleplayerscores>");
        if (singlePlProgress != null) {
            xmlOutput.append(singlePlProgress.getLevelsProgressRankingPoints());
        } else {
            xmlOutput.append(-1);
        }

        xmlOutput.append("</singleplayerscores>");

        xmlOutput.append("\n");

        xmlOutput.append("<singleplayerlevels>");
        if (singlePlProgress != null) {
            xmlOutput.append(singlePlProgress.getLevelsProgressData());
        } else {
            xmlOutput.append(ConfigParams.NO_DATA_AVAILABLE_WS);
        }
        xmlOutput.append("</singleplayerlevels>");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.FACEBOOK_ID + ">");
        if ((facebookId != null) && (!"".equals(facebookId))) {
            xmlOutput.append(facebookId);
        } else {
            xmlOutput.append(ConfigParams.NO_FACEBOOK_ID_TXT);
        }
        xmlOutput.append("</" + PlayerGameProfileTags.FACEBOOK_ID + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.PURCHASED_ITEMS_LIST + ">");
        if (((playerProfile.getPuchasedItemsList()) != null) && (!"".equals(playerProfile.getPuchasedItemsList()))) {
            xmlOutput.append(playerProfile.getPuchasedItemsList());
        } else {
            xmlOutput.append(ConfigParams.NO_PURCHASED_ITEMS);
        }
        xmlOutput.append("</" + PlayerGameProfileTags.PURCHASED_ITEMS_LIST + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.EXPERIENCE_POINTS + ">");
        xmlOutput.append(playerProfile.getExperiencePoints());
        xmlOutput.append("</" + PlayerGameProfileTags.EXPERIENCE_POINTS + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.ACHIEVEMENTS + ">");
        if ((playerProfile.getAchievementsList() != null) && (!"".equals(playerProfile.getAchievementsList()))) {
            xmlOutput.append(playerProfile.getAchievementsList());
        } else {
            xmlOutput.append(ConfigParams.NO_DATA_AVAILABLE_WS);
        }
        xmlOutput.append("</" + PlayerGameProfileTags.ACHIEVEMENTS + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.AVATAR + ">");
        xmlOutput.append(playerProfile.getAvatar());
        xmlOutput.append("</" + PlayerGameProfileTags.AVATAR + ">");

        xmlOutput.append("\n");

        xmlOutput.append("<" + PlayerGameProfileTags.COINS + ">");
        xmlOutput.append(playerProfile.getCoinsBalance());
        xmlOutput.append("</" + PlayerGameProfileTags.COINS + ">");

        xmlOutput.append("\n");


        xmlOutput.append("<newchallengeawards>");

        if (challengeAwards != null) {
            String[] awardIds = challengeAwards.getAwardsList().split(";");
            xmlOutput.append("\n");
            xmlOutput.append("<numofawards>");
            xmlOutput.append(awardIds.length);
            xmlOutput.append("</numofawards>");

            for (int k = 0; k < awardIds.length; k++) {
                String[] awardItems = awardIds[k].split("_");
                xmlOutput.append("<awards" + k + ">");
                xmlOutput.append("\n");
                xmlOutput.append("<id>");
                xmlOutput.append(awardItems[0]);
                xmlOutput.append("</id>");
                xmlOutput.append("\n");
                xmlOutput.append("<date>");
                xmlOutput.append(awardItems[1]);
                xmlOutput.append("</date>");
                xmlOutput.append("\n");
                xmlOutput.append("<scores>");
                xmlOutput.append(awardItems[2]);
                xmlOutput.append("</scores>");
                xmlOutput.append("\n");

                xmlOutput.append("</awards" + k + ">");
                xmlOutput.append("\n");

            }
        } else {
            xmlOutput.append("no_new_awards");
        }
        xmlOutput.append("</newchallengeawards>");

        xmlOutput.append("\n");


        xmlOutput.append("</" + PlayerGameProfileTags.ROOT + ">");

        return xmlOutput.toString();
    }

    private PlayerManager getPlayerManager() {
        return (PlayerManager) AppContext.getContext().getBean(AppSpringBeans.PLAYER_MANAGER_BEAN);
    }

    private ConfigManager getConfigManager() {
        return (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);
    }

    private int getItemPriceForId(int itemId) {
        if ((itemId > 100) && (itemId < 199)) {//Perk
            PerkEntry perkEntry = PerksConfiguration.getInstance().perkEntryForId(String.valueOf(itemId));
            return Integer.parseInt(perkEntry.getPriceCoins());
        }
        if ((itemId > 200) && (itemId < 299)) {//Weapon
            WeaponEntry weaponEntry = WeaponConfiguration.getInstance().weaponEntryForId(itemId);
            return Integer.parseInt(weaponEntry.getPriceCoins());
        }
        if ((itemId > 300) && (itemId < 399)) {//Rockets
            RocketConfigEntry rocketConfEntry = RocketsConfiguration.getInstance().rocketConfigForRocketId(String.valueOf(itemId));
            return Integer.parseInt(rocketConfEntry.getPriceCoins());
        }
        return 0;
    }
}
