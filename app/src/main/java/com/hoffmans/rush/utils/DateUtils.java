package com.hoffmans.rush.utils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by devesh on 17/11/15.
 */
public class DateUtils {


    public static String DEFAULT_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static String DATE_FORMAT="dd-MM-yyyy";
    private static DateUtils mDateUtils;
    private String daysList[] = {"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};

    /**
     * to get instance of DateUtils class.
     *
     * @return
     */
    public static DateUtils getInstance() {
        if (mDateUtils == null) {
            mDateUtils = new DateUtils();
        }
        return mDateUtils;
    }

    /**
     * convert given date's month number to text
     *
     * @param date String date in format of dd-MM-yyyy
     * @return Date with month as name
     */
    public String dateWithMonthName(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date dateObj = sdf.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObj);
            String month = new DateFormatSymbols().getMonths()[getMonthFromDate(dateObj)];
            return calendar.get(Calendar.DAY_OF_MONTH) + " " + month + " " + calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    private int getMonthFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public int getDayFromDATE(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);

    }

    /**
     * get the month as string from date
     *
     * @param date
     * @return
     */
    public String getMonthStringFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new SimpleDateFormat("MMMM").format(cal.getTime());

    }

    /**
     * @param month int digit of month
      * @return  Month name as String
     */
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    /**
     * get year from date
     * @param date
     * @return the year
     */
    public int getYearFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.YEAR);


    }

    /**
     * create Date() from given String date into given format
     *
     * @param dateString Date which is converted to Date object
     * @param format     Format of date
     * @return Date Object
     */
    public Date getDateFromString(String dateString, String format) {
        Date convertedDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
        return convertedDate;
    }


    /**
     * get the day name from given date object
     * @param date
     * @return
     */
    public String getDayNameFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return daysList[cal.get(Calendar.DAY_OF_WEEK)-1];
    }

    public Date getTimeFromString(String time, String format) {
//        Date dt = sourceFormat.parse(bookingStartTime);
//        "hh:mm aa"
        DateFormat parseFormat = new SimpleDateFormat(format, Locale.ENGLISH);
//        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = parseFormat.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
//        String time1 = sdf.format(dt);
    }

    /**
     * @param time time in format of 01:00 PM
     * @return time in format of 13:00
     */
   public String get12HoursTo24Hour(String time) {
       DateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

            try {
                Date date = parseFormat.parse(time);
                Calendar calender = Calendar.getInstance();
                calender.setTime(date);
                String format = "%1$02d"; // two digits
                return calender.get(Calendar.HOUR_OF_DAY) + ":" + String.format(format, calender.get(Calendar.MINUTE));
            } catch (ParseException e) {
                e.printStackTrace();
                return time;
            }
        }


    public Date get12HoursTo24HourDate(String time){
        DateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        try {
            Date date = parseFormat.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String parseGMTDate(String datetime) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DEFAULT_FORMAT);
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        simpleDateFormat.setTimeZone(utcZone);
        Date myDate = simpleDateFormat.parse(datetime);
        //To display:

        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        String formattedDate = simpleDateFormat.format(myDate);
        return formattedDate;
    }

    public String getTimeString(String datetime , String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try
        {
            return simpleDateFormat.format(getDateFromString(datetime, DateUtils.DEFAULT_FORMAT));
        }catch (Exception e){
            return  null;
        }
    }

    public String getOnlyDateString(String datetime , String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            return simpleDateFormat.format(getDateFromString(datetime, DateUtils.DEFAULT_FORMAT));
        }catch (Exception e){
            return  null;
        }
    }




    }

