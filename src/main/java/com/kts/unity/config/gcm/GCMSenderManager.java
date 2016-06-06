package com.kts.unity.config.gcm;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GCMSenderManager {

    private static final String GOOGLE_GCM_API_SECURITY_KEY = Settings.getGoogleGcmAPISecurityKey();
    public static final int MULTICAST_SIZE = 999;//1000 is maximum value - restricted by Google
    GCMManager gcmMgr;
    private Map<String, GCMUser> registeredUsers;
    private Sender sender;
    private static final Executor threadPool = Executors.newFixedThreadPool(5);
    private String messageToSend;
    private static GCMSenderManager gcmSenderMgr;
    private static Boolean notifyPlayersAboutOpponentsOnline = true;
    private static long lastOnlineOpponentsBroadcastTimestampMillis = 0;//this value used in initialization, of First broadcast it is changed to actual timestamp
    private static int broadcastNotificationPeriodHours = 2;

    private GCMSenderManager() {
    }

    public static GCMSenderManager getInstance() {
        if (gcmSenderMgr != null) {
            return gcmSenderMgr;
        } else {
            gcmSenderMgr = new GCMSenderManager();
            return gcmSenderMgr;
        }
    }
    
    public String sendMessageToUsers(String message, String users){
        System.out.println("Sending GCM message to users - " + users);
        messageToSend = message;

        gcmMgr = (GCMManager) AppContext.getContext().getBean(AppSpringBeans.GCM_MANAGER_BEAN);

        sender = newSender();
        
        List<String> usersNames = new ArrayList<String>();
        String[] usersArr = users.split(";");
        usersNames.addAll(Arrays.asList(usersArr));

        String status;
        List<GCMUser> gcmUsers = gcmMgr.getGcmUsersForNames(usersNames);
        if ((gcmUsers == null) || (gcmUsers.isEmpty())) {
            status = "Message ignored as there is no device registered!";
        } else {
            // send a multicast message using JSON
            // must split in chunks of 1000 devices (GCM limit)
            int total = gcmUsers.size();
            List<String> partialDevices = new ArrayList<String>(total);
            int counter = 0;
            int tasks = 0;
            registeredUsers = new HashMap<String, GCMUser>();
            for (GCMUser currentGcmUser : gcmUsers) {
                registeredUsers.put(currentGcmUser.getGcmRegistrationId(), currentGcmUser);
                counter++;
                partialDevices.add(currentGcmUser.getGcmRegistrationId());
                int partialSize = partialDevices.size();
                if (partialSize == MULTICAST_SIZE || counter == total) {
                    asyncSend(partialDevices);
                    partialDevices.clear();
                    tasks++;
                }
            }
            status = "Asynchronously sending " + tasks + " multicast messages to "
                    + total + " devices";
        }

        return status;
    }//sendMessageToUsers

    public String sendBroadcastMessage(String messageText) {
        System.out.println("Broadcasting GCM message :" + messageText);
        
        messageToSend = messageText;

        gcmMgr = (GCMManager) AppContext.getContext().getBean(AppSpringBeans.GCM_MANAGER_BEAN);

        sender = newSender();

        String status;
        List<GCMUser> gcmUsers = gcmMgr.getGcmUsersList(MULTICAST_SIZE);
        if ((gcmUsers == null) || (gcmUsers.isEmpty())) {
            status = "Message ignored as there is no device registered!";
        } else {
            // send a multicast message using JSON
            // must split in chunks of 1000 devices (GCM limit)
            int total = gcmUsers.size();
            List<String> partialDevices = new ArrayList<String>(total);
            int counter = 0;
            int tasks = 0;
            registeredUsers = new HashMap<String, GCMUser>();
            for (GCMUser currentGcmUser : gcmUsers) {
                registeredUsers.put(currentGcmUser.getGcmRegistrationId(), currentGcmUser);
                counter++;
                partialDevices.add(currentGcmUser.getGcmRegistrationId());
                int partialSize = partialDevices.size();
                if (partialSize == MULTICAST_SIZE || counter == total) {
                    asyncSend(partialDevices);
                    partialDevices.clear();
                    tasks++;
                }
            }
            status = "Asynchronously sending " + tasks + " multicast messages to "
                    + total + " devices";
        }

        return status;
    }//sendBroadcastMeaasge

    private void asyncSend(List<String> partialDevices) {
        // make a copy
        final List<String> devices = new ArrayList<String>(partialDevices);
        threadPool.execute(new Runnable() {

            public void run() {
                Message message = new Message.Builder().addData("data", messageToSend).build();
                System.out.println("Message is being sent : " + message);
                MulticastResult multicastResult;
                try {
                    multicastResult = sender.send(message, devices, 5);
                } catch (IOException e) {
                    System.out.println("Error posting messages" + e);
                    return;
                }
                List<Result> results = multicastResult.getResults();
                // analyze the results
                for (int i = 0; i < devices.size(); i++) {
                    String regId = devices.get(i);
                    Result result = results.get(i);
                    String messageId = result.getMessageId();
                    if (messageId != null) {
                        System.out.println("Succesfully sent message to device: " + regId + "; messageId = " + messageId);
                        String canonicalRegId = result.getCanonicalRegistrationId();
                        if (canonicalRegId != null) {
                            // same device has more than on registration id: update it
                            System.out.println("canonicalRegId " + canonicalRegId);
                            GCMUser user = registeredUsers.get(regId);
                            user.setGcmRegistrationId(canonicalRegId);
                            gcmMgr.updateGcmRecord(user);
                            //Datastore.updateRegistration(regId, canonicalRegId);
                        }
                    } else {
                        String error = result.getErrorCodeName();
                        if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                            // application has been removed from device - unregister it
                            System.out.println("Unregistered device: " + regId);
                            GCMUser userToDelete = registeredUsers.get(regId);
                            gcmMgr.deleteGcmRecord(userToDelete.getRecordId());
                            //Datastore.unregister(regId);
                        } else {
                            System.out.println("Error sending message to " + regId + ": " + error);
                        }
                    }
                }
            }
        });
    }//asyncSend
    
    protected Sender newSender() {
        return new Sender(GOOGLE_GCM_API_SECURITY_KEY);
    }
    
    
    public static Boolean getNotifyPlayersAboutOpponentsOnline() {
        return notifyPlayersAboutOpponentsOnline;
    }

    public static void setNotifyPlayersAboutOpponentsOnline(Boolean notifyPlayersAboutOpponentsOnlineVal) {
        notifyPlayersAboutOpponentsOnline = notifyPlayersAboutOpponentsOnlineVal;
    }

    public static long getLastOnlineOpponentsBroadcastTimestampMillis() {
        return lastOnlineOpponentsBroadcastTimestampMillis;
    }

    public static void setLastOnlineOpponentsBroadcastTimestampMillis(long lastBroadcastTimestampMillis) {
        GCMSenderManager.lastOnlineOpponentsBroadcastTimestampMillis = lastBroadcastTimestampMillis;
    }

    public static int getBroadcastNotificationPeriodHours() {
        return broadcastNotificationPeriodHours;
    }

    public static void setBroadcastNotificationPeriodHours(int broadcastNotificationPeriodHours) {
        GCMSenderManager.broadcastNotificationPeriodHours = broadcastNotificationPeriodHours;
    }
        
    
}
