package com.kts.unity.config.gamification.actions;

import com.kts.unity.config.gamification.GamificationPlugin;
import com.kts.unity.config.gamification.common.Challenge;
import com.kts.unity.config.web.actions.ActionConstants;
import com.kts.unity.config.web.utils.CommonUtils;
import com.kts.unity.config.web.utils.Dictionary;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ViewGamificationHomePage {

    private String currentServerTimeAndDate = null;
    private List<Challenge> challenges;
    private String activeChallengeInGame = null;
    private Boolean activeStatusTimer = null;
    private Boolean activeWeekStatusTimer = null;
    private Boolean activeDayStatusTimer = null;
    private String weekTimerEndDate = null;
    private String weekTimerEndTime = null;
    private String weekEndTimeStampMillis = null;
    private String weeksDurationCounter = null;
    private long weekChallengeDaysRemain;
    private long weekChallengeHoursRemain;
    
    
    private String dayNextAwardingDate = null;
    private String dayTimerEndTime = null;
    private String dayFinishDate = null;
    private String dayDaysDelayPeriod = null;
    private String dayEndDateTimeStampMillis = null;
    private long dayChallengeDaysRemain;
    private long dayChallengeHoursRemain;
    
    
    
    private String tournamentTimerEndDate = null;    
    private String tournamentTimerEndTime = null;
    private String tournamentEndTmistampMillis = null;
    private long tournamentDaysRemain;
    private long tournamentHoursRemain;
    private Integer[] timePickerHours = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
    private List<Integer> timePickerHoursEntries = Arrays.asList(timePickerHours);
    private Integer[] weeksCounterPicker = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    private List<Integer> weeksCounterPickerEntries = Arrays.asList(weeksCounterPicker);
    private Integer[] daysToDelayDailyRankingPicker = {1,2,3,4,5};
    private List<Integer> daysToDelayDailyRankingPickerEntries = Arrays.asList(daysToDelayDailyRankingPicker);

    public String execute() {

        Date d1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm");
        currentServerTimeAndDate = df.format(d1);

        challenges = new ArrayList<Challenge>();

        Challenge emptyChallengeForUIControl = new Challenge();
        emptyChallengeForUIControl.setId("none");
        emptyChallengeForUIControl.setTitle("None");
        challenges.add(emptyChallengeForUIControl);

        Map<String, Challenge> challengesMap = GamificationPlugin.getChallenges();
        Iterator it = challengesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            challenges.add((Challenge) pairs.getValue());
        }

        activeChallengeInGame = Dictionary.getValue(GamificationPlugin.ACTIVE_CHALLENGE_IN_GAME_KEY);


        long currentTimeMillis = (new Date()).getTime();
        if (Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_DATE_TIME_ENABLED_KEY) != null) {
            activeStatusTimer = true;
            tournamentTimerEndDate = Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_DATE_KEY);
            tournamentTimerEndTime = Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_TIME_KEY);
            tournamentEndTmistampMillis = Dictionary.getValue(GamificationPlugin.TOURNAMENT_END_TIME_MILLIS_KEY);
            
            tournamentDaysRemain = CommonUtils.getDaysRemainForMillisTime(currentTimeMillis, Long.parseLong(tournamentEndTmistampMillis));
            tournamentHoursRemain = CommonUtils.getHoursRemainIn24Hours(currentTimeMillis, Long.parseLong(tournamentEndTmistampMillis));
        }
        
        if (Dictionary.getValue(GamificationPlugin.WEEK_END_DATE_TIME_ENABLED_KEY) != null) {
            activeWeekStatusTimer = true;
            weekTimerEndDate = Dictionary.getValue(GamificationPlugin.WEEK_END_DATE_KEY);
            weekTimerEndTime = Dictionary.getValue(GamificationPlugin.WEEK_END_TIME_KEY);
            weeksDurationCounter = Dictionary.getValue(GamificationPlugin.WEEK_NUM_OF_WEEKS_TO_REPEAT_KEY);
            weekEndTimeStampMillis = Dictionary.getValue(GamificationPlugin.WEEK_END_TIME_MILLIS_KEY);
            
            weekChallengeDaysRemain = CommonUtils.getDaysRemainForMillisTime(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.WEEK_END_TIME_MILLIS_KEY)));
            weekChallengeHoursRemain = CommonUtils.getHoursRemainIn24Hours(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.WEEK_END_TIME_MILLIS_KEY)));
        }
        
        if (Dictionary.getValue(GamificationPlugin.DAY_END_DATE_TIME_ENABLED_KEY) != null) {
            activeDayStatusTimer = true;
            dayNextAwardingDate = Dictionary.getValue(GamificationPlugin.DAY_NEXT_AWARDING_DATE_KEY);
            dayTimerEndTime = Dictionary.getValue(GamificationPlugin.DAY_END_TIME_KEY);
            dayFinishDate = Dictionary.getValue(GamificationPlugin.DAY_FINISH_DATE_KEY);
            dayDaysDelayPeriod = Dictionary.getValue(GamificationPlugin.DAY_NUM_OF_DAYS_TO_DELAY_KEY);
            dayEndDateTimeStampMillis = Dictionary.getValue(GamificationPlugin.DAY_END_TIME_MILLIS_KEY);
            
            dayChallengeDaysRemain = CommonUtils.getDaysRemainForMillisTime(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.DAY_END_TIME_MILLIS_KEY)));
            dayChallengeHoursRemain = CommonUtils.getHoursRemainIn24Hours(currentTimeMillis, Long.parseLong(Dictionary.getValue(GamificationPlugin.DAY_END_TIME_MILLIS_KEY)));
            
            
        }

        return ActionConstants.SUCCESS;
    }

    public String getActiveChallengeInGame() {
        return activeChallengeInGame;
    }

    public void setActiveChallengeInGame(String activeChallengeInGame) {
        this.activeChallengeInGame = activeChallengeInGame;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    public Boolean getActiveStatusTimer() {
        return activeStatusTimer;
    }

    public void setActiveStatusTimer(Boolean activeStatusTimer) {
        this.activeStatusTimer = activeStatusTimer;
    }

    public Boolean getActiveWeekStatusTimer() {
        return activeWeekStatusTimer;
    }

    public void setActiveWeekStatusTimer(Boolean activeWeekStatusTimer) {
        this.activeWeekStatusTimer = activeWeekStatusTimer;
    }

    public Boolean getActiveDayStatusTimer() {
        return activeDayStatusTimer;
    }

    public void setActiveDayStatusTimer(Boolean activeDayStatusTimer) {
        this.activeDayStatusTimer = activeDayStatusTimer;
    }
    

    
    public String getTournamentTimerEndDate() {
        return tournamentTimerEndDate;
    }

    public void setTournamentTimerEndDate(String tournamentTimerEndDate) {
        this.tournamentTimerEndDate = tournamentTimerEndDate;
    }

    public String getWeekTimerEndDate() {
        return weekTimerEndDate;
    }

    public void setWeekTimerEndDate(String weekTimerEndDate) {
        this.weekTimerEndDate = weekTimerEndDate;
    }

    public String getWeekTimerEndTime() {
        return weekTimerEndTime;
    }

    public void setWeekTimerEndTime(String weekTimerEndTime) {
        this.weekTimerEndTime = weekTimerEndTime;
    }

    public String getWeekEndTimeStampMillis() {
        return weekEndTimeStampMillis;
    }

    public void setWeekEndTimeStampMillis(String weekEndTimeStampMillis) {
        this.weekEndTimeStampMillis = weekEndTimeStampMillis;
    }
    
    

    public String getCurrentServerTimeAndDate() {
        return currentServerTimeAndDate;
    }

    public void setCurrentServerTimeAndDate(String currentServerTimeAndDate) {
        this.currentServerTimeAndDate = currentServerTimeAndDate;
    }

    public List<Integer> getTimePickerHoursEntries() {
        return timePickerHoursEntries;
    }

    public void setTimePickerHoursEntries(List<Integer> timePickerHoursEntries) {
        this.timePickerHoursEntries = timePickerHoursEntries;
    }

    public List<Integer> getWeeksCounterPickerEntries() {
        return weeksCounterPickerEntries;
    }

    public void setWeeksCounterPickerEntries(List<Integer> weeksCounterPickerEntries) {
        this.weeksCounterPickerEntries = weeksCounterPickerEntries;
    }

    public List<Integer> getDaysToDelayDailyRankingPickerEntries() {
        return daysToDelayDailyRankingPickerEntries;
    }

    public void setDaysToDelayDailyRankingPickerEntries(List<Integer> daysToDelayDailyRankingPickerEntries) {
        this.daysToDelayDailyRankingPickerEntries = daysToDelayDailyRankingPickerEntries;
    }

    public String getDayNextAwardingDate() {
        return dayNextAwardingDate;
    }

    public void setDayNextAwardingDate(String dayNextAwardingDate) {
        this.dayNextAwardingDate = dayNextAwardingDate;
    }

    public String getDayTimerEndTime() {
        return dayTimerEndTime;
    }

    public void setDayTimerEndTime(String dayTimerEndtime) {
        this.dayTimerEndTime = dayTimerEndtime;
    }

    public String getDayFinishDate() {
        return dayFinishDate;
    }

    public void setDayFinishDate(String dayFinishDate) {
        this.dayFinishDate = dayFinishDate;
    }

    public String getDayDaysDelayPeriod() {
        return dayDaysDelayPeriod;
    }

    public void setDayDaysDelayPeriod(String dayDaysDelayPeriod) {
        this.dayDaysDelayPeriod = dayDaysDelayPeriod;
    }

    public String getDayEndDateTimeStampMillis() {
        return dayEndDateTimeStampMillis;
    }

    public void setDayEndDateTimeStampMillis(String dateEndDateTimeStampMillis) {
        this.dayEndDateTimeStampMillis = dateEndDateTimeStampMillis;
    }
    
    
    
    
    

    public String getWeeksDurationCounter() {
        return weeksDurationCounter;
    }

    public void setWeeksDurationCounter(String weeksDurationCounter) {
        this.weeksDurationCounter = weeksDurationCounter;
    }

    public long getWeekChallengeDaysRemain() {
        return weekChallengeDaysRemain;
    }

    public void setWeekChallengeDaysRemain(long weekChallengeDaysRemain) {
        this.weekChallengeDaysRemain = weekChallengeDaysRemain;
    }

    public long getWeekChallengeHoursRemain() {
        return weekChallengeHoursRemain;
    }

    public void setWeekChallengeHoursRemain(long weekChallengeHoursRemain) {
        this.weekChallengeHoursRemain = weekChallengeHoursRemain;
    }

    public long getDayChallengeDaysRemain() {
        return dayChallengeDaysRemain;
    }

    public void setDayChallengeDaysRemain(long dayChallengeDaysRemain) {
        this.dayChallengeDaysRemain = dayChallengeDaysRemain;
    }

    public long getDayChallengeHoursRemain() {
        return dayChallengeHoursRemain;
    }

    public void setDayChallengeHoursRemain(long dayChallengeHoursRemain) {
        this.dayChallengeHoursRemain = dayChallengeHoursRemain;
    }
    
    

    
    public String getTournamentTimerEndTime() {
        return tournamentTimerEndTime;
    }

    public void setTournamentTimerEndTime(String tournamentTimerEndTime) {
        this.tournamentTimerEndTime = tournamentTimerEndTime;
    }

    public String getTournamentEndTmistampMillis() {
        return tournamentEndTmistampMillis;
    }

    public void setTournamentEndTmistampMillis(String tournamentEndTmistampMillis) {
        this.tournamentEndTmistampMillis = tournamentEndTmistampMillis;
    }

    public long getTournamentDaysRemain() {
        return tournamentDaysRemain;
    }

    public void setTournamentDaysRemain(long tournamentDaysRemain) {
        this.tournamentDaysRemain = tournamentDaysRemain;
    }

    public long getTournamentHoursRemain() {
        return tournamentHoursRemain;
    }

    public void setTournamentHoursRemain(long tournamentHoursRemain) {
        this.tournamentHoursRemain = tournamentHoursRemain;
    }
}
