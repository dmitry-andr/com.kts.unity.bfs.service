package com.kts.unity.config.web.backend.utils;

import com.kts.unity.config.web.utils.ConfigParams;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class WebServiceSecurityTokenManager {
    
    private static WebServiceSecurityTokenManager instance = null;

    public static final String TOKEN_REQUEST_VALID_TOKEN_STATUS = "validToken";
    public static final String TOKEN_REQUEST_EXPIRED_TOKEN_ERROR_STATUS = "tokenExpired";
    public static final String TOKEN_REQUEST_NO_SUCH_TOKEN_ERROR_STATUS = "noRequestedToken";

    private static final int MIN_INDEX_FOR_HANDSHAKE_KEY = 0;
    private static final int MAX_INDEX_FOR_HANDSHAKE_KEY = 9;

    //handshake tokens in MD5
    private static final String[] HANDSHAKE_TOKENS_LIST = Settings.getSecurityHandshakeTokensList().split(";");
    
    private final Map<String, WebServiceSecurityToken> securityTokens = new HashMap<String, WebServiceSecurityToken>();

    
    private WebServiceSecurityTokenManager(){
        // Exists only to defeat instantiation.
    }

    public static synchronized WebServiceSecurityTokenManager getInstance(){
        if(instance != null){
            return instance;
        }else{
            instance = new WebServiceSecurityTokenManager();
            return instance;
        }
    }
    
    
    public synchronized WebServiceSecurityToken createSecurityToken() {
    	
    	//Clean expired tokens if any
        cleanExpiredTokens();
                
        WebServiceSecurityToken securityToken = new WebServiceSecurityToken();

        String securityTokenHex = CypherUtility.getMD5HexFromString((new UCID()).toString());
        //add nadshake key index - index in range from min to max
        int handShakeKeyIndex = (new Random()).nextInt((MAX_INDEX_FOR_HANDSHAKE_KEY - MIN_INDEX_FOR_HANDSHAKE_KEY) + 1) + MIN_INDEX_FOR_HANDSHAKE_KEY;
        securityToken.setTokenId(securityTokenHex + handShakeKeyIndex);

        long currentTimeMillis = (new Date()).getTime();
        securityToken.setTokenCreationTimeMillis(currentTimeMillis);
        
        securityTokens.put(securityToken.getTokenId(), securityToken);
        
        
       
        return securityToken;
    }

    public synchronized String getTokenValidityStatus(String securityTokenId) {
        WebServiceSecurityToken securityToken = securityTokens.get(securityTokenId);
        if (securityToken != null) {
            long currentTimeMillis = (new Date()).getTime();
            if ((currentTimeMillis - securityToken.getTokenCreationTimeMillis()) < Settings.getTtlWebServiceSecurityTokenMillis()) {
                securityTokens.remove(securityToken.getTokenId());
                return TOKEN_REQUEST_VALID_TOKEN_STATUS;
            } else {
                securityTokens.remove(securityToken.getTokenId());
                return TOKEN_REQUEST_EXPIRED_TOKEN_ERROR_STATUS;
            }
        } else {
            return TOKEN_REQUEST_NO_SUCH_TOKEN_ERROR_STATUS;
        }
    }

    private synchronized int cleanExpiredTokens() {
        
        if(securityTokens.size() < 5){
            return 3;//number of tokens is too small to launch validation loop
        }

        Iterator<Map.Entry<String, WebServiceSecurityToken>> entriesIterator = securityTokens.entrySet().iterator();

        long currentTimeMillis = (new Date()).getTime();
        while (entriesIterator.hasNext()) {
        	
            Map.Entry<String, WebServiceSecurityToken> entry = entriesIterator.next();
            System.out.println("Token : " + entry.getKey() + " : " + entry.getValue());
            if ((currentTimeMillis - entry.getValue().getTokenCreationTimeMillis()) < Settings.getTtlWebServiceSecurityTokenMillis()) {
            	entriesIterator.remove();
            }
        }

        return 0;
    }

    public synchronized String authorizeByTokenHandshake(String tokenData) {
        if (tokenData == null) {
            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }
        if ((!tokenData.contains("_")) || (tokenData.split("_").length < 2)) {
            return ConfigParams.BAD_INPUT_PARAMS_WS;
        }

        String serverTokenId = tokenData.split("_")[0];
        String appToken = tokenData.split("_")[1];

        //check if server token is valid
        if (!WebServiceSecurityTokenManager.TOKEN_REQUEST_VALID_TOKEN_STATUS.equals(WebServiceSecurityTokenManager.getInstance().getTokenValidityStatus(serverTokenId))) {
            return "81";//invalid server token passed
        }

        String handshakeTokenIndexStrVal = serverTokenId.substring(serverTokenId.length() - 1);
        //System.out.println("Hnadshake token index - " + handshakeTokenIndexStrVal);
        int handshakeTokenIndex = Integer.parseInt(handshakeTokenIndexStrVal);
        int indexToSelect = HANDSHAKE_TOKENS_LIST.length - 1 - handshakeTokenIndex;
        String appTokenExpectedByServer = CypherUtility.getMD5HexFromString(serverTokenId + HANDSHAKE_TOKENS_LIST[indexToSelect]);

        if (appTokenExpectedByServer.equals(appToken)) {
            return TOKEN_REQUEST_VALID_TOKEN_STATUS;
        } else {
            return "82";//invalid app token passed
        }
    }

}
