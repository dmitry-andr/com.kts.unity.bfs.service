package com.kts.unity.config.gamification;

import com.kts.unity.config.gamification.common.Challenge;
import com.kts.unity.config.gamification.common.ChallengeDAO;
import com.kts.unity.config.gamification.common.ScoresRankingEntry;
import com.kts.unity.config.gamification.challenges.daytopplayers.DayRankingEntryDAO;
import com.kts.unity.config.gamification.challenges.sniperofweek.SniperRankingEntryDAO;
import com.kts.unity.config.gamification.challenges.timeperiodtournament.TournamentTimePeriodEntryDAO;
import com.kts.unity.config.gamification.challenges.weektopplayers.WeekRankingEntryDAO;
import com.kts.unity.config.gamification.common.ChallengeAward;
import com.kts.unity.config.gamification.common.ChallengeAwardDAO;
import com.kts.unity.config.gamification.common.SniperRankingEntry;
import com.kts.unity.config.web.backend.PlayerManager;
import com.kts.unity.config.web.entities.configs.Achievement;
import com.kts.unity.config.web.entities.configs.AchievementsConfiguration;
import com.kts.unity.config.web.entities.player.PlayerGameProfile;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import com.kts.unity.config.web.utils.CommonUtils;
import com.kts.unity.config.web.utils.Dictionary;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamificationPlugin {

    public static final String ACTIVE_CHALLENGE_IN_GAME_KEY = "active_challenge_in_game";
    public static final String TOURNAMENT_END_DATE_TIME_ENABLED_KEY = "tournament_end_date_enable_status";
    public static final String TOURNAMENT_END_DATE_KEY = "tournament_end_date";
    public static final String TOURNAMENT_END_TIME_HOURS_KEY = "tournament_end_time_hours";
    public static final String TOURNAMENT_END_TIME_MINUTES_KEY = "tournament_end_time_minutes";
    public static final String TOURNAMENT_END_TIME_MILLIS_KEY = "tournament_end_time_millis";
    public static final String WEEK_END_DATE_TIME_ENABLED_KEY = "week_end_date_enable_status";
    public static final String WEEK_END_DATE_KEY = "week_end_date";
    public static final String WEEK_END_TIME_KEY = "week_end_time";
    public static final String WEEK_END_TIME_MILLIS_KEY = "week_end_time_millis";
    public static final String WEEK_NUM_OF_WEEKS_TO_REPEAT_KEY = "week_num_to_repeat_end_time";
    public static final String DAY_END_DATE_TIME_ENABLED_KEY = "day_end_date_enable_status";
    public static final String DAY_NEXT_AWARDING_DATE_KEY = "day_end_date";
    public static final String DAY_FINISH_DATE_KEY = "day_finish_date";
    public static final String DAY_END_TIME_KEY = "day_end_time";
    public static final String DAY_END_TIME_MILLIS_KEY = "day_end_time_millis";
    public static final String DAY_NUM_OF_DAYS_TO_DELAY_KEY = "day_num_to_repeat_end_time";
    private static final int RETURN_CODE_IF_CHALLENGE_INACTIVE = 8;
    public static final int POINS_TO_ADD_FOR_LOST_PLAYER = 3;
    public static final int NUM_OF_TOP_RECORDS_FOR_AWARDING = 10;
    private static final String AWARD_SHOULD_BE_DISPLAYED_FLAG = "disp";
    private static DayRankingEntryDAO dayRankingEntryDAO;
    private static WeekRankingEntryDAO weekRankingEntryDAO;
    private static SniperRankingEntryDAO sniperRankingEntryDAO;
    private static TournamentTimePeriodEntryDAO tournamentRankingEntryDAO;
    // Challenges list functionality code
    private static ChallengeDAO challengeDAO;
    private static ChallengeAwardDAO challengeAwardDAO;
    private static Map<String, Challenge> challenges = null;

    public static int removeChallengesDataForPlayer(int playerId){
        int status = 0;
        
        status = dayRankingEntryDAO.clearDataForPlayer(playerId);
        status = status + (status - 1);
        
        status = weekRankingEntryDAO.clearDataForPlayer(playerId);
        status = status + (status - 1);
        
        status = sniperRankingEntryDAO.clearDataForPlayer(playerId);
        status = status + (status - 1);
                
        status = tournamentRankingEntryDAO.clearDataForPlayer(playerId);
        status = status + (status - 1);
                
        return status;
    }
    
    public static Map<String, Challenge> getChallenges() {
        if (challenges != null) {
            return challenges;
        } else {
            updateChallengesList();
            return challenges;
        }
    }

    public static void updateChallengesList() {
        List<Challenge> challengesList = challengeDAO.getChallengeList();
        if (challengesList != null) {
            challenges = new HashMap<String, Challenge>(challengesList.size());
            for (Challenge challenge : challengesList) {
                challenges.put(challenge.getId(), challenge);
            }
        }
    }

    public static int updateChallengeActiveStatus(String challengeId, int activityStatusValue) {
        int status = 0;
        status = challengeDAO.updateActivityStatus(challengeId, activityStatusValue);
        updateChallengesList();

        return status;
    }

    public static int clearDataForChallenge(String challengeName) {
        int status = 0;

        Date currentDate = new Date();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String currentDateStr = df.format(currentDate);

        if (ChallengeTypes.DAY.getValue().equals(challengeName)) {
            List<ScoresRankingEntry> dayRankingList = getDayRankingList();
            dayRankingEntryDAO.clearAllDataForDayRanking();//clear data as rating list already pulled and further operation might be rather "heavy"

            updateAwardData(dayRankingList, currentDateStr, "71");

        }//ChallengeTypes.DAY


        if (ChallengeTypes.WEEK.getValue().equals(challengeName)) {

            List<ScoresRankingEntry> weekRankingList = getWeekRankingList();
            weekRankingEntryDAO.clearAllDataForWeekRanking();//clear data as rating list already pulled and further operation might be rather "heavy"

            updateAwardData(weekRankingList, currentDateStr, "70");

        }//ChallengeTypes.WEEK


        if (ChallengeTypes.SNIPER_OF_WEEK.getValue().equals(challengeName)) {

            List<ScoresRankingEntry> sniperRankingList = getSniperRankingList();
            sniperRankingEntryDAO.clearAllDataForSniperRanking();//clear data as rating list already pulled and further operation might be rather "heavy"

            updateAwardData(sniperRankingList, currentDateStr, "72");

        }//ChallengeTypes.SNIPER_OF_WEEK


        if (ChallengeTypes.TIME_PERIOD_TOURNAMENT.getValue().equals(challengeName)) {

            List<ScoresRankingEntry> tournamentRankingList = getTournamentRankingList();
            tournamentRankingEntryDAO.clearAllDataForTournament();//clear data as rating list already pulled and further operation might be rather "heavy"

            //Tournament end date is reached or it was stopped manually - remove end date/time data

            if ("true".equalsIgnoreCase(Dictionary.getValue(TOURNAMENT_END_DATE_TIME_ENABLED_KEY))) {
                updateAwardData(tournamentRankingList, currentDateStr, "73");
            }

            Dictionary.removeValue(GamificationPlugin.TOURNAMENT_END_DATE_TIME_ENABLED_KEY);
            Dictionary.removeValue(GamificationPlugin.TOURNAMENT_END_DATE_KEY);
            Dictionary.removeValue(GamificationPlugin.TOURNAMENT_END_TIME_HOURS_KEY);
            Dictionary.removeValue(GamificationPlugin.TOURNAMENT_END_TIME_MINUTES_KEY);
            Dictionary.removeValue(GamificationPlugin.TOURNAMENT_END_TIME_MILLIS_KEY);
        }//ChallengeTypes.TIME_PERIOD_TOURNAMENT

        return status;
    }

    private static void updateAwardData(List<ScoresRankingEntry> challengeRankingList, String currentDateStr, String awardPrefix) {
        if (challengeRankingList.size() >= 3) {
            PlayerManager playerMgr = (PlayerManager) AppContext.getContext().getBean(AppSpringBeans.PLAYER_MANAGER_BEAN);

            for (int k = 0; k < 3; k++) {
                String newAwardId = awardPrefix + (k + 1);
                String newAwardIdWithDatestamp = newAwardId + "_" + currentDateStr + "_" + challengeRankingList.get(k).getScores() + "_" + AWARD_SHOULD_BE_DISPLAYED_FLAG;

                PlayerGameProfile playerGameProfile = playerMgr.getPlayerGameProfileById(challengeRankingList.get(k).getPlayerId());
                Achievement achmnt = AchievementsConfiguration.getInstance().getAchievementForId(newAwardId);
                playerGameProfile.setCoinsBalance(playerGameProfile.getCoinsBalance() + achmnt.getAwardingCoins());
                playerMgr.updatePlayerGameProfile(playerGameProfile);

                ChallengeAward playerAwardsData = challengeAwardDAO.getChallengeAwardsForPlayer(challengeRankingList.get(k).getPlayerId());
                if (playerAwardsData != null) {
                    playerAwardsData.setAwardsList(playerAwardsData.getAwardsList() + ";" + newAwardIdWithDatestamp);
                    challengeAwardDAO.updateChallengeAwardsRecord(playerAwardsData);
                } else {
                    playerAwardsData = new ChallengeAward();
                    playerAwardsData.setPlayerId(challengeRankingList.get(k).getPlayerId());
                    playerAwardsData.setAwardsList(newAwardIdWithDatestamp);
                    challengeAwardDAO.createAwardRecordForPlayer(playerAwardsData);
                }
            }

            //provide awards for top-10 participation
            for (int j = 3; ((j < 10) && (j < challengeRankingList.size())); j++) {
                ChallengeAward award = challengeAwardDAO.getChallengeAwardsForPlayer(challengeRankingList.get(j).getPlayerId());
                String newAwardId = awardPrefix + "4" + "_" + currentDateStr + "_" + challengeRankingList.get(j).getScores() + "_" + AWARD_SHOULD_BE_DISPLAYED_FLAG;
                if (award != null) {
                    award.setAwardsList(award.getAwardsList() + ";" + newAwardId);
                    challengeAwardDAO.updateChallengeAwardsRecord(award);
                } else {
                    award = new ChallengeAward();
                    award.setPlayerId(challengeRankingList.get(j).getPlayerId());
                    award.setAwardsList(newAwardId);
                    challengeAwardDAO.createAwardRecordForPlayer(award);
                }
            }
        }

    }

    public static ChallengeAward getChallengeAwardsForPlayer(int playerId) {
        return challengeAwardDAO.getChallengeAwardsForPlayer(playerId);
    }

    public static ChallengeAward getNewAwardsForPlayer(int playerId) {

        ChallengeAward currentAwardsData = challengeAwardDAO.getChallengeAwardsForPlayer(playerId);
        if (currentAwardsData == null) {
            return null;
        }

        String awardsList = currentAwardsData.getAwardsList();
        StringBuilder awardsIdsToBeDisplayed = new StringBuilder();
        StringBuilder awardsIdsWithRemovedDispFlag = new StringBuilder();
        if (awardsList.contains(AWARD_SHOULD_BE_DISPLAYED_FLAG)) {
            String[] awardIdsEntries = awardsList.split(";");
            for (int k = 0; k < awardIdsEntries.length; k++) {
                if (awardIdsEntries[k].contains(AWARD_SHOULD_BE_DISPLAYED_FLAG)) {
                    String[] awardIdItems = awardIdsEntries[k].split("_");
                    StringBuilder awardIdNoDispFlag = new StringBuilder();
                    awardIdNoDispFlag.append(awardIdItems[0]);
                    awardIdNoDispFlag.append("_");
                    awardIdNoDispFlag.append(awardIdItems[1]);
                    awardIdNoDispFlag.append("_");
                    awardIdNoDispFlag.append(awardIdItems[2]);

                    if (awardsIdsToBeDisplayed.length() > 0) {
                        awardsIdsToBeDisplayed.append(";");
                    }
                    if (awardsIdsWithRemovedDispFlag.length() > 0) {
                        awardsIdsWithRemovedDispFlag.append(";");
                    }
                    awardsIdsToBeDisplayed.append(awardIdNoDispFlag.toString());
                    awardsIdsWithRemovedDispFlag.append(awardIdNoDispFlag.toString());
                } else {
                    if (awardsIdsWithRemovedDispFlag.length() > 0) {
                        awardsIdsWithRemovedDispFlag.append(";");
                    }
                    awardsIdsWithRemovedDispFlag.append(awardIdsEntries[k]);
                }
            }//for

            currentAwardsData.setAwardsList(awardsIdsWithRemovedDispFlag.toString());
            challengeAwardDAO.updateChallengeAwardsRecord(currentAwardsData);

            ChallengeAward newAwardsDataOnly = new ChallengeAward();
            newAwardsDataOnly.setPlayerId(playerId);
            newAwardsDataOnly.setAwardsList(awardsIdsToBeDisplayed.toString());
            return newAwardsDataOnly;
        } else {
            return null;
        }
    }

    public static ChallengeDAO getChallengeDAO() {
        return challengeDAO;
    }

    public void setChallengeDAO(ChallengeDAO challgDAO) {
        challengeDAO = challgDAO;
    }

    public static ChallengeAwardDAO getChallengeAwardDAO() {
        return challengeAwardDAO;
    }

    public void setChallengeAwardDAO(ChallengeAwardDAO challgAwardDAO) {
        challengeAwardDAO = challgAwardDAO;
    }
    // END OF Challenges list functionality code

    // DAY Ranking functionality code
    public static int updateDayScoresForPlayer(int playerId, int addedScores) {
        int status = 0;
        if (GamificationPlugin.getChallenges().get(ChallengeTypes.DAY.getValue()).getIsActive() != 1) {
            return RETURN_CODE_IF_CHALLENGE_INACTIVE;
        }

        ScoresRankingEntry dayRanking = dayRankingEntryDAO.getDayRankingEntryForId(playerId);
        if (dayRanking != null) {
            dayRanking.setLastActivityTime((new Date()).getTime());
            int scoresToChange = dayRanking.getScores() + addedScores;
            if (scoresToChange < 0) {
                scoresToChange = 0;
            }
            dayRanking.setScores(scoresToChange);
            //dayRanking.setPositionAtLastGame(dayRankingEntryDAO.getPlayerPositionInDayRanking(playerId));
            dayRankingEntryDAO.updateDayRankingEntry(dayRanking);
        } else {
            dayRanking = new ScoresRankingEntry();
            dayRanking.setPlayerId(playerId);
            dayRanking.setLastActivityTime((new Date()).getTime());
            int scoresToChange = addedScores;
            if (scoresToChange < 0) {
                scoresToChange = 0;
            }
            dayRanking.setScores(scoresToChange);
            dayRanking.setPositionAtLastGame(dayRankingEntryDAO.getNumberOfRecords() + 1);
            dayRankingEntryDAO.createDayRankingEntry(dayRanking);
        }
        
        checkIfDayRatingIsOver();
        
        return status;
    }

    public static int checkIfDayRatingIsOver() {
        //check if daily tournament time is over
        long currentTimeMillis = (new Date()).getTime();
        if ("true".equalsIgnoreCase(Dictionary.getValue(DAY_END_DATE_TIME_ENABLED_KEY))) {
            if (currentTimeMillis > Long.parseLong(Dictionary.getValue(DAY_END_TIME_MILLIS_KEY))) {
                clearDataForChallenge(ChallengeTypes.DAY.getValue());

                long finishDateMillis = CommonUtils.getTimeInMillis(Dictionary.getValue(DAY_FINISH_DATE_KEY), Dictionary.getValue(DAY_END_TIME_KEY));
                if ((finishDateMillis - currentTimeMillis) > 0) {
                    //Update next award date
                    int daysToRepeat = Integer.parseInt(Dictionary.getValue(DAY_NUM_OF_DAYS_TO_DELAY_KEY));
                    long nextAwardTimeMillis = currentTimeMillis + daysToRepeat * 24 * 60 * 60 * 1000;
                    Dictionary.putValue(DAY_END_TIME_MILLIS_KEY, String.valueOf(nextAwardTimeMillis));
                    Dictionary.putValue(DAY_NEXT_AWARDING_DATE_KEY, CommonUtils.getDateFromMillis(nextAwardTimeMillis));
                } else {//finish date reached - deactivate Daily challenge
                    //remove timer data
                    cleanDailyTrnmtTimeData();
                    updateChallengeActiveStatus(ChallengeTypes.DAY.getValue(), 0);
                    if(ChallengeTypes.DAY.getValue().equals(Dictionary.getValue(GamificationPlugin.ACTIVE_CHALLENGE_IN_GAME_KEY))){
                        if(challenges.get(ChallengeTypes.WEEK.getValue()).getIsActive() == 1){
                            Dictionary.putValue(GamificationPlugin.ACTIVE_CHALLENGE_IN_GAME_KEY, ChallengeTypes.WEEK.getValue());
                        }else{
                            Dictionary.removeValue(GamificationPlugin.ACTIVE_CHALLENGE_IN_GAME_KEY);
                        }
                    }
                    
                }
            }
        }//if ("true" DAY_END_DATE_TIME_ENABLED_KEY)


        return 1;
    }

    public static void cleanDailyTrnmtTimeData() {
        Dictionary.removeValue(GamificationPlugin.DAY_END_DATE_TIME_ENABLED_KEY);
        Dictionary.removeValue(GamificationPlugin.DAY_NEXT_AWARDING_DATE_KEY);
        Dictionary.removeValue(GamificationPlugin.DAY_FINISH_DATE_KEY);
        Dictionary.removeValue(GamificationPlugin.DAY_END_TIME_KEY);
        Dictionary.removeValue(GamificationPlugin.DAY_NUM_OF_DAYS_TO_DELAY_KEY);
        Dictionary.removeValue(GamificationPlugin.DAY_END_TIME_MILLIS_KEY);
    }

    public static int getCurrentPositionInDayRating(int playerId) {
        return dayRankingEntryDAO.getPlayerPositionInDayRanking(playerId);
    }

    public static ScoresRankingEntry getPlayerDayRanking(int playerId) {
        return dayRankingEntryDAO.getDayRankingEntryForId(playerId);
    }

    public static int updatePlayerDayRanking(ScoresRankingEntry dayRankingEntry) {
        return dayRankingEntryDAO.updateDayRankingEntry(dayRankingEntry);
    }

    public static List<ScoresRankingEntry> getDayRankingList() {
        return dayRankingEntryDAO.getEntryListForLastDayPeriod();
    }

    public DayRankingEntryDAO getDayRankingEntryDAO() {
        return dayRankingEntryDAO;
    }

    public void setDayRankingEntryDAO(DayRankingEntryDAO dayRankingDAO) {
        dayRankingEntryDAO = dayRankingDAO;
    }
    // END of DAY Ranking functionality code

    // WEEK Ranking functionality code
    public static int updateWeekScoresForPlayer(int playerId, int addedScores) {
        int status = 0;
        if (GamificationPlugin.getChallenges().get(ChallengeTypes.WEEK.getValue()).getIsActive() != 1) {
            return RETURN_CODE_IF_CHALLENGE_INACTIVE;
        }

        ScoresRankingEntry weekRanking = weekRankingEntryDAO.getWeekRankingEntryForId(playerId);
        if (weekRanking != null) {
            weekRanking.setLastActivityTime((new Date()).getTime());
            int scoresToChange = weekRanking.getScores() + addedScores;
            if (scoresToChange < 0) {
                scoresToChange = 0;
            }
            weekRanking.setScores(scoresToChange);
            //weekRanking.setPositionAtLastGame(weekRankingEntryDAO.getPlayerPositionInWeekRanking(playerId));
            weekRankingEntryDAO.updateWeekRankingEntry(weekRanking);
        } else {
            weekRanking = new ScoresRankingEntry();
            weekRanking.setPlayerId(playerId);
            weekRanking.setLastActivityTime((new Date()).getTime());
            int scoresToChange = addedScores;
            if (scoresToChange < 0) {
                scoresToChange = 0;
            }
            weekRanking.setScores(scoresToChange);
            weekRanking.setPositionAtLastGame(weekRankingEntryDAO.getNumberOfRecords() + 1);
            weekRankingEntryDAO.createWeekRankingEntry(weekRanking);
        }

        checkIfWeekRatingIsOver();

        return status;
    }

    public static int checkIfWeekRatingIsOver() {
        //check if weekly tournament time is over
        long currentTimeMillis = (new Date()).getTime();
        if ("true".equalsIgnoreCase(Dictionary.getValue(WEEK_END_DATE_TIME_ENABLED_KEY))) {
            if (currentTimeMillis > Long.parseLong(Dictionary.getValue(WEEK_END_TIME_MILLIS_KEY))) {
                clearDataForChallenge(ChallengeTypes.WEEK.getValue());
                int weeksRemainsCounter = Integer.parseInt(Dictionary.getValue(WEEK_NUM_OF_WEEKS_TO_REPEAT_KEY));
                weeksRemainsCounter--;
                if (weeksRemainsCounter > 0) {
                    Dictionary.putValue(WEEK_NUM_OF_WEEKS_TO_REPEAT_KEY, String.valueOf(weeksRemainsCounter));
                    //Update next award date
                    long nextAwardTimeMillis = currentTimeMillis + 7 * 24 * 60 * 60 * 1000;
                    Dictionary.putValue(WEEK_END_TIME_MILLIS_KEY, String.valueOf(nextAwardTimeMillis));
                    Dictionary.putValue(WEEK_END_DATE_KEY, CommonUtils.getDateFromMillis(nextAwardTimeMillis));
                } else {
                    //remove timer data
                    Dictionary.removeValue(GamificationPlugin.WEEK_END_DATE_TIME_ENABLED_KEY);
                    Dictionary.removeValue(GamificationPlugin.WEEK_END_DATE_KEY);
                    Dictionary.removeValue(GamificationPlugin.WEEK_END_TIME_KEY);
                    Dictionary.removeValue(GamificationPlugin.WEEK_NUM_OF_WEEKS_TO_REPEAT_KEY);
                    Dictionary.removeValue(GamificationPlugin.WEEK_END_TIME_MILLIS_KEY);

                }
            }
        }//if ("true" WEEK_END_DATE_TIME_ENABLED_KEY)


        return 1;
    }

    public static int getCurrentPositionInWeekRating(int playerId) {
        return weekRankingEntryDAO.getPlayerPositionInWeekRanking(playerId);
    }

    public static ScoresRankingEntry getPlayerWeekRanking(int playerId) {
        return weekRankingEntryDAO.getWeekRankingEntryForId(playerId);
    }

    public static int updatePlayerWeekRanking(ScoresRankingEntry weekRankingEntry) {
        return weekRankingEntryDAO.updateWeekRankingEntry(weekRankingEntry);
    }

    public static List<ScoresRankingEntry> getWeekRankingList() {
        return weekRankingEntryDAO.getEntryListForLastWeekPeriod();
    }

    public WeekRankingEntryDAO getWeekRankingEntryDAO() {
        return weekRankingEntryDAO;
    }

    public void setWeekRankingEntryDAO(WeekRankingEntryDAO weekRankingDAO) {
        weekRankingEntryDAO = weekRankingDAO;
    }
    //END of Week Ranking functionality code

    // SNIPER of the week Ranking functionality code
    public static int updateSniperScoresForPlayer(int playerId, int accuracy) {
        int status = 0;
        if (GamificationPlugin.getChallenges().get(ChallengeTypes.SNIPER_OF_WEEK.getValue()).getIsActive() != 1) {
            return RETURN_CODE_IF_CHALLENGE_INACTIVE;
        }

        SniperRankingEntry sniperRanking = sniperRankingEntryDAO.getSniperRankingEntryForId(playerId);
        if (sniperRanking != null) {
            sniperRanking.setLastActivityTime((new Date()).getTime());

            String shootingHistoryInDB = sniperRanking.getShootingHistory();
            String[] historyEntries = shootingHistoryInDB.split(";");
            final int NUM_OF_HISTORY_ENTRIES_TO_SAVE = 3;
            if (historyEntries.length != NUM_OF_HISTORY_ENTRIES_TO_SAVE) {
                sniperRanking.setShootingHistory(accuracy + ";" + shootingHistoryInDB);
            } else {
                for (int k = 0; k < (historyEntries.length - 1); k++) {//shift all entries to remove last one and add new at the beginning
                    historyEntries[k + 1] = historyEntries[k];
                }
                historyEntries[0] = String.valueOf(accuracy);
                StringBuilder shootingHistory = new StringBuilder();
                for (int i = 0; i < historyEntries.length; i++) {
                    shootingHistory.append(historyEntries[i]);
                    if (i != (historyEntries.length - 1)) {
                        shootingHistory.append(";");
                    }
                }
                sniperRanking.setShootingHistory(shootingHistory.toString());
            }

            sniperRanking.setScores(calculateAccuracyScores(sniperRanking.getShootingHistory()));

            //sniperRanking.setPositionAtLastGame(sniperRankingEntryDAO.getPlayerPositionInSniperRanking(playerId));

            sniperRankingEntryDAO.updateSniperRankingEntry(sniperRanking);
        } else {
            sniperRanking = new SniperRankingEntry();
            sniperRanking.setPlayerId(playerId);
            sniperRanking.setLastActivityTime((new Date()).getTime());
            sniperRanking.setShootingHistory(String.valueOf(accuracy));
            sniperRanking.setScores(accuracy);
            sniperRanking.setPositionAtLastGame(sniperRankingEntryDAO.getNumberOfRecords() + 1);
            sniperRankingEntryDAO.createSniperRankingEntry(sniperRanking);
        }
        return status;
    }

    private static int calculateAccuracyScores(String shootingData) {
        String[] shootingEntries = shootingData.split(";");
        int accuracySum = 0;
        for (int k = 0; k < shootingEntries.length; k++) {
            accuracySum += Integer.parseInt(shootingEntries[k]);
        }
        return (int) Math.round(accuracySum / shootingEntries.length);
    }

    public static int getCurrentPositionInSniperRating(int playerId) {
        return sniperRankingEntryDAO.getPlayerPositionInSniperRanking(playerId);
    }

    public static SniperRankingEntry getPlayerSniperRanking(int playerId) {
        return sniperRankingEntryDAO.getSniperRankingEntryForId(playerId);
    }

    public static int updatePlayerSniperRanking(SniperRankingEntry weekRankingEntry) {
        return sniperRankingEntryDAO.updateSniperRankingEntry(weekRankingEntry);
    }

    public static List<ScoresRankingEntry> getSniperRankingList() {
        return sniperRankingEntryDAO.getEntryListForLastWeekPeriod();
    }

    public SniperRankingEntryDAO getSniperRankingEntryDAO() {
        return sniperRankingEntryDAO;
    }

    public void setSniperRankingEntryDAO(SniperRankingEntryDAO sniperRankingDAO) {
        sniperRankingEntryDAO = sniperRankingDAO;
    }
    // END OF SNIPER of the week Ranking functionality code

    // TOURNAMENT Ranking functionality code
    public static int updateTournamentScoresForPlayer(int playerId, int addedScores) {
        int status = 0;
        if (GamificationPlugin.getChallenges().get(ChallengeTypes.TIME_PERIOD_TOURNAMENT.getValue()).getIsActive() != 1) {
            return RETURN_CODE_IF_CHALLENGE_INACTIVE;
        }

        ScoresRankingEntry tournamentRanking = tournamentRankingEntryDAO.getTournamentRankingEntryForId(playerId);
        if (tournamentRanking != null) {
            tournamentRanking.setLastActivityTime((new Date()).getTime());
            int scoresToChange = tournamentRanking.getScores() + addedScores;
            if (scoresToChange < 0) {
                scoresToChange = 0;
            }
            tournamentRanking.setScores(scoresToChange);
            tournamentRankingEntryDAO.updateTournamentRankingEntry(tournamentRanking);
        } else {
            tournamentRanking = new ScoresRankingEntry();
            tournamentRanking.setPlayerId(playerId);
            tournamentRanking.setLastActivityTime((new Date()).getTime());
            int scoresToChange = addedScores;
            if (scoresToChange < 0) {
                scoresToChange = 0;
            }
            tournamentRanking.setScores(scoresToChange);
            tournamentRanking.setPositionAtLastGame(tournamentRankingEntryDAO.getNumberOfRecords() + 1);
            tournamentRankingEntryDAO.createTournamentRankingEntry(tournamentRanking);
        }

        //check if tournament time is over
        long currentTimeMillis = (new Date()).getTime();
        if ("true".equalsIgnoreCase(Dictionary.getValue(TOURNAMENT_END_DATE_TIME_ENABLED_KEY))) {
            if (currentTimeMillis > Long.parseLong(Dictionary.getValue(TOURNAMENT_END_TIME_MILLIS_KEY))) {
                clearDataForChallenge(ChallengeTypes.TIME_PERIOD_TOURNAMENT.getValue());
            }
        }

        return status;
    }

    public static int getCurrentPositionAtTournamentRating(int playerId) {
        return tournamentRankingEntryDAO.getPlayerPositionInTournament(playerId);
    }

    public static ScoresRankingEntry getPlayerTournamentRanking(int playerId) {
        return tournamentRankingEntryDAO.getTournamentRankingEntryForId(playerId);
    }

    public static int updatePlayerTournamentRanking(ScoresRankingEntry tournamentRankingEntry) {
        return tournamentRankingEntryDAO.updateTournamentRankingEntry(tournamentRankingEntry);
    }

    public static List<ScoresRankingEntry> getTournamentRankingList() {
        return tournamentRankingEntryDAO.getEntryListForTournament();
    }

    public TournamentTimePeriodEntryDAO getTournamentRankingEntryDAO() {
        return tournamentRankingEntryDAO;
    }

    public void setTournamentRankingEntryDAO(TournamentTimePeriodEntryDAO tourntRankingEntryDAO) {
        tournamentRankingEntryDAO = tourntRankingEntryDAO;
    }
    //END of TOURNAMENT Ranking functionality code
}
