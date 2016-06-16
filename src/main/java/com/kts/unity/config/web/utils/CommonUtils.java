package com.kts.unity.config.web.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommonUtils {

    public static final String DATE_FORMAT_ON_UI = "yyyy-MM-dd";
    public static final String TIME_FORMAT_ON_UI = "HH:mm";

    public static long getTimeInMillis(String date, String timeHours, String timeMinutes) {

        SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT_ON_UI + " " + TIME_FORMAT_ON_UI);//this describes date string to be built and transformed to milliseconds
        long milliseconds = -1;
        Date d = new Date();
        try {
            d = f.parse(date + " " + timeHours + ":" + timeMinutes);
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        milliseconds = d.getTime();
        return milliseconds;
    }

    public static long getTimeInMillis(String date, String timeHours) {
        return getTimeInMillis(date, timeHours, "00");
    }
    
/*    
	@Deprecated
    public static long getTimeInMillis(String date, String timeHours) {

        SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT_ON_UI + " " + TIME_FORMAT_ON_UI);//this describes date string to be built and transformed to milliseconds
        long milliseconds = -1;
        Date d = new Date();
        try {
            d = f.parse(date + " " + timeHours + ":00");
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        milliseconds = d.getTime();
        return milliseconds;
    }
*/

    public static String getDateFromMillis(long milliseconds) {
        Date date = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat(DATE_FORMAT_ON_UI);
        return df.format(date);
    }

    public static long getDaysRemainForMillisTime(long initialTime, long endTime) {
        long dateDiffMillis = endTime - initialTime;
        return (long) Math.floor(dateDiffMillis / 86400000);
    }
    
    public static long getHoursRemainIn24Hours(long initialTime, long endTime){
        long dateDiffMillis = endTime - initialTime;
        return (long) Math.floor(dateDiffMillis / (60 * 60 * 1000) % 24);
    }
    
    public static long getMinutesRemainingInHour(long initialTime, long endTime){
    	long dateDiffMillis = endTime - initialTime;
    	return (long) ((dateDiffMillis / (1000*60)) % 60);
    }
}
