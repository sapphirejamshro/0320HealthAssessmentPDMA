package com.sapphire.HealthAssessmentPDMA.helper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateFormatter {
    private static final String DATE_TIME_FORMAT  = "yyyy-MM-dd HH:mm:ss";
    private static final String ONLY_DATE_FORMAT = "dd/MM/yyyy";
    private static final String ONLY_TIME_FORMAT = "HH:mm:ss";


    public static String timestampStringToDateString(String date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        SimpleDateFormat sdf2 = new SimpleDateFormat(ONLY_DATE_FORMAT);
        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf2.format(date1);
    }

    public static Timestamp stringToTimeStampWithTime(String strTime) {

        Timestamp tempTimestamp = null;
        if (strTime != null && !strTime.equals(""))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

            Date parsedTimeStamp = null;
            try {
                // String date = dateFormat.format(Date.parse(strTime.trim()));
                parsedTimeStamp = dateFormat.parse(strTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (parsedTimeStamp != null) {
                tempTimestamp = new Timestamp(parsedTimeStamp.getTime());
            }

        }
        return tempTimestamp;
    }
    public static Timestamp currentDateToTimeStamp(){
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }
    public static String timestampStringToTimeString(String date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        SimpleDateFormat sdf2 = new SimpleDateFormat(ONLY_TIME_FORMAT);
        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf2.format(date1);
    }

    public static String currentTime(){
        Date date = new Date();
        DateFormat timeFormat = new SimpleDateFormat(ONLY_TIME_FORMAT);
        return timeFormat.format(date.getTime());

    }
}
