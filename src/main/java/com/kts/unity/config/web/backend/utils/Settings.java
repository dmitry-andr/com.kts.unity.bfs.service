package com.kts.unity.config.web.backend.utils;

import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.ConfigParams;
import java.io.IOException;
import java.util.Properties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class Settings {
    
    private static boolean developmentModeEnabled = false;
    private static int maxAllowedAverageWSResponseTimeMillis;
    private static int maxAllowedMaximumWSResponseTimeMillis;
    private static int initialNumOfPlayersOnAdminPage;
    private static int maxNumOfPlayersOnAdminPage;
    private static long timeToSeePlayerOnline;
    private static boolean notifyAdminOnPlayersGoOnline = false;
    private static String reservedPlayerNames;
    private static String botsNames;
    private static int numOfAdjacentPlayersInChlgRkgList = 0;
    private static int numOfPlayersInChlgTopList = 0;
    private static int logHealthMonitorAcceptancePeriodHours;
    private static int logHealthMonitorAcceptanceNumOfLogsInPeriod;
    private static String logHealthMonitorEmailsSendTo;
    private static long ttlWebServiceSecurityTokenMillis;
    private static String googleGcmAPISecurityKey;
    private static String securityHandshakeTokensList;
    private static String createPlayerSecKey;
    private static String getAppAllConfigsSecKey;
    private static String getConfigurationSecKey;
    private static String rocketsConfigSecKey;
    private static String weaponConfigurationSecKey;
    private static String perksConfigurationSecKey;
    private static String botsConfigurationSecKey;
    private static String authenticateByEmailSecKey;
    private static String rankingRulesSecKey;
    private static String logRemoteExceptionSecKey;
    private static String updatePurchasedCoinsOperationSecKey;
    private static String updatePurchasedInGameItemSecKey;
    private static String playerRankingDataSecKey;
    private static String playerAwardsDataSecKey;
    private static String securityTokenOperationSecKey;
    
    private static String emailToAddress;
    private static String mailFromAdminAddress;
    private static String mailFromAdminAddressPassword;
    private static String smtpHostNameAdminMsg;
    private static String smtpPortAdminMsg;
    private static String mailSubject;
    private static String mailSubjectLogHealthMonitor;
    private static String mailFromServiceUserAddressDevTest;
    private static String mailFromServiceUserAddressPasswordDevTest;
    private static String smtpServiceUserHostNameDevTest;
    private static String smtpServiceUserPortDevTest;
    private static String mailFromServiceUserAddressProd = "battlefield.space.multiplayer@gmail.com";
    private static String mailFromServiceUserAddressPasswordProd = "qwert76VB";
    private static String smtpServiceUserHostNameProd = "smtp.gmail.com";
    private static String smtpServiceUserPortProd = "587";
    
    

    public int init(){
        int status = 0;
        
        Resource resource = AppContext.getContext().getResource(ConfigParams.SETTINGS_FILE_PATH_NAME);
        try{
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            String debugModeStrVal = props.getProperty("development_mode");
            developmentModeEnabled = Boolean.parseBoolean(debugModeStrVal);
            maxAllowedAverageWSResponseTimeMillis = Integer.parseInt(props.getProperty("max_allowed_ws_average_response_time_millis"));
            maxAllowedMaximumWSResponseTimeMillis = Integer.parseInt(props.getProperty("max_allowed_ws_max_response_time_millis"));
            initialNumOfPlayersOnAdminPage = Integer.parseInt(props.getProperty("initial_number_of_players_admin_page"));
            maxNumOfPlayersOnAdminPage = Integer.parseInt(props.getProperty("max_number_of_players_admin_page"));
            timeToSeePlayerOnline = Long.parseLong(props.getProperty("time_to_see_players_online_millis"));
            notifyAdminOnPlayersGoOnline = Boolean.parseBoolean(props.getProperty("gcm_notify_admins_about_online_players"));            
            reservedPlayerNames = props.getProperty("reserved_player_names");
            botsNames = props.getProperty("bots_names");
            numOfAdjacentPlayersInChlgRkgList = Integer.parseInt(props.getProperty("number_of_adjacent_users_in_ranking_list"));
            numOfPlayersInChlgTopList = Integer.parseInt(props.getProperty("number_of_users_in_top_list_challenge"));
            logHealthMonitorAcceptancePeriodHours = Integer.parseInt(props.getProperty("log_health_monitor_acceptance_period_hours"));
            logHealthMonitorAcceptanceNumOfLogsInPeriod = Integer.parseInt(props.getProperty("log_health_monitor_acceptance_num_logs_in_period"));
            logHealthMonitorEmailsSendTo = props.getProperty("log_health_monitor_acceptance_emails_send_to");
            ttlWebServiceSecurityTokenMillis = Long.parseLong(props.getProperty("ttl_web_service_security_token_millis"));
            
            //Initialize security keys value
            googleGcmAPISecurityKey = props.getProperty("google_gcm_api_security_key");
            securityHandshakeTokensList = props.getProperty("security_handshake_tokens_list");
            createPlayerSecKey = props.getProperty("create_player_security_key");
            getAppAllConfigsSecKey = props.getProperty("get_app_all_configs_security_key");
            getConfigurationSecKey = props.getProperty("get_configuration_security_key");
            rocketsConfigSecKey = props.getProperty("get_rockets_configuration_security_key");
            weaponConfigurationSecKey = props.getProperty("get_weapon_configuration_security_key");
            perksConfigurationSecKey = props.getProperty("get_perks_configuration_security_key");
            botsConfigurationSecKey = props.getProperty("get_bots_configuration_security_key");
            authenticateByEmailSecKey = props.getProperty("authenticate_by_email_security_key");
            rankingRulesSecKey = props.getProperty("get_ranking_rules_security_key");
            logRemoteExceptionSecKey = props.getProperty("log_remote_exception_security_key");
            updatePurchasedCoinsOperationSecKey = props.getProperty("update_purchased_coins_operation_security_key");
            updatePurchasedInGameItemSecKey = props.getProperty("update_purchased_in_game_item_operation_security_key");
            playerRankingDataSecKey = props.getProperty("get_player_ranking_data_security_key");
            playerAwardsDataSecKey = props.getProperty("get_player_awards_data_security_key");
            securityTokenOperationSecKey = props.getProperty("get_security_token_operation_security_key");
            
            //Email config params
            emailToAddress = props.getProperty("emailToAddress");
            mailFromAdminAddress = props.getProperty("mailFromAdminAddress");
            mailFromAdminAddressPassword = props.getProperty("mailFromAdminAddressPassword");
            smtpHostNameAdminMsg = props.getProperty("smtpHostNameAdminMsg");
            smtpPortAdminMsg = props.getProperty("smtpPortAdminMsg");
            mailSubject = props.getProperty("mailSubject");
            mailSubjectLogHealthMonitor = props.getProperty("mailSubjectLogHealthMonitor");
            mailFromServiceUserAddressDevTest = props.getProperty("mailFromServiceUserAddressDevTest");
            mailFromServiceUserAddressPasswordDevTest = props.getProperty("mailFromServiceUserAddressPasswordDevTest");
            smtpServiceUserHostNameDevTest = props.getProperty("smtpServiceUserHostNameDevTest");
            smtpServiceUserPortDevTest = props.getProperty("smtpServiceUserPortDevTest");
            mailFromServiceUserAddressProd = props.getProperty("mailFromServiceUserAddressProd");
            mailFromServiceUserAddressPasswordProd = props.getProperty("mailFromServiceUserAddressPasswordProd");
            smtpServiceUserHostNameProd = props.getProperty("smtpServiceUserHostNameProd");
            smtpServiceUserPortProd = props.getProperty("smtpServiceUserPortProd");
            
            
            
            
            status = 1;
        }catch(IOException ex){
            ex.printStackTrace();
        }
                
        return status;
    }

    public static boolean isDevelopmentModeEnabled() {
        return developmentModeEnabled;
    }

    public static int getMaxAllowedAverageWSResponseTimeMillis() {
        return maxAllowedAverageWSResponseTimeMillis;
    }

    public static int getMaxAllowedMaximumWSResponseTimeMillis() {
        return maxAllowedMaximumWSResponseTimeMillis;        
    }

    public static int getInitialNumOfPlayersOnAdminPage() {
        return initialNumOfPlayersOnAdminPage;
    }

    public static int getMaxNumOfPlayersOnAdminPage() {
        return maxNumOfPlayersOnAdminPage;
    }

    public static long getTimeToSeePlayerOnline() {
        return timeToSeePlayerOnline;
    }

    public static boolean isNotifyAdminOnPlayersGoOnline() {
        return notifyAdminOnPlayersGoOnline;
    }


    public static String getReservedPlayerNames() {
        return reservedPlayerNames;
    }

    public static String getBotsNames() {
        return botsNames;
    }

    public static int getNumOfAdjacentPlayersInChlgRkgList() {
        return numOfAdjacentPlayersInChlgRkgList;
    }

    public static int getNumOfPlayersInChlgTopList() {
        return numOfPlayersInChlgTopList;
    }

    public static int getLogHealthMonitorAcceptancePeriodHours() {
        return logHealthMonitorAcceptancePeriodHours;
    }

    public static int getLogHealthMonitorAcceptanceNumOfLogsInPeriod() {
        return logHealthMonitorAcceptanceNumOfLogsInPeriod;
    }

    public static String getLogHealthMonitorEmailsSendTo() {
        return logHealthMonitorEmailsSendTo;
    }

    public static long getTtlWebServiceSecurityTokenMillis() {
        return ttlWebServiceSecurityTokenMillis;
    }

	public static String getGoogleGcmAPISecurityKey() {
		return googleGcmAPISecurityKey;
	}

	public static String getCreatePlayerSecKey() {
		return createPlayerSecKey;
	}

	public static String getSecurityHandshakeTokensList() {
		return securityHandshakeTokensList;
	}

	public static String getGetAppAllConfigsSecKey() {
		return getAppAllConfigsSecKey;
	}

	public static String getGetConfigurationSecKey() {
		return getConfigurationSecKey;
	}

	public static String getRocketsConfigSecKey() {
		return rocketsConfigSecKey;
	}

	public static String getWeaponConfigurationSecKey() {
		return weaponConfigurationSecKey;
	}

	public static String getPerksConfigurationSecKey() {
		return perksConfigurationSecKey;
	}

	public static String getBotsConfigurationSecKey() {
		return botsConfigurationSecKey;
	}

	public static String getAuthenticateByEmailSecKey() {
		return authenticateByEmailSecKey;
	}

	public static String getRankingRulesSecKey() {
		return rankingRulesSecKey;
	}

	public static String getLogRemoteExceptionSecKey() {
		return logRemoteExceptionSecKey;
	}

	public static String getUpdatePurchasedCoinsOperationSecKey() {
		return updatePurchasedCoinsOperationSecKey;
	}

	public static String getUpdatePurchasedInGameItemSecKey() {
		return updatePurchasedInGameItemSecKey;
	}

	public static String getPlayerRankingDataSecKey() {
		return playerRankingDataSecKey;
	}

	public static String getPlayerAwardsDataSecKey() {
		return playerAwardsDataSecKey;
	}

	public static String getSecurityTokenOperationSecKey() {
		return securityTokenOperationSecKey;
	}

	public static String getEmailToAddress() {
		return emailToAddress;
	}

	public static String getMailFromAdminAddress() {
		return mailFromAdminAddress;
	}

	public static String getMailFromAdminAddressPassword() {
		return mailFromAdminAddressPassword;
	}

	public static String getSmtpHostNameAdminMsg() {
		return smtpHostNameAdminMsg;
	}

	public static String getSmtpPortAdminMsg() {
		return smtpPortAdminMsg;
	}

	public static String getMailSubject() {
		return mailSubject;
	}

	public static String getMailSubjectLogHealthMonitor() {
		return mailSubjectLogHealthMonitor;
	}

	public static String getMailFromServiceUserAddressDevTest() {
		return mailFromServiceUserAddressDevTest;
	}

	public static String getMailFromServiceUserAddressPasswordDevTest() {
		return mailFromServiceUserAddressPasswordDevTest;
	}

	public static String getSmtpServiceUserHostNameDevTest() {
		return smtpServiceUserHostNameDevTest;
	}

	public static String getSmtpServiceUserPortDevTest() {
		return smtpServiceUserPortDevTest;
	}

	public static String getMailFromServiceUserAddressProd() {
		return mailFromServiceUserAddressProd;
	}

	public static String getMailFromServiceUserAddressPasswordProd() {
		return mailFromServiceUserAddressPasswordProd;
	}

	public static String getSmtpServiceUserHostNameProd() {
		return smtpServiceUserHostNameProd;
	}

	public static String getSmtpServiceUserPortProd() {
		return smtpServiceUserPortProd;
	}
	
	
	
	
}
