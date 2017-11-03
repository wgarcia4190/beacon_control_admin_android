package com.sigma.beaconcontrol.helpers.general;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Wilson on 5/22/17.
 */

public class DateUtils {

    public static boolean isToday(Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(new Date());
        cal2.setTime(date);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isYesterday(Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(new Date());
        cal2.setTime(date);

        cal1.add(Calendar.DAY_OF_YEAR, -1);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static String getTime(Date date) {
        return new SimpleDateFormat("h:mm a").format(date);
    }

    public static boolean isAfter(Date date) {
        return new Date().after(date);
    }

    public static String getDateFormat(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(date);
    }

    public static String getHours(Date date){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(new Date());
        cal2.setTime(date);

        long seconds = cal1.get(Calendar.SECOND) - cal2.get(Calendar.SECOND);
        long minutes = cal1.get(Calendar.MINUTE) - cal2.get(Calendar.MINUTE);
        long hours = cal1.get(Calendar.HOUR_OF_DAY) - cal2.get(Calendar.HOUR_OF_DAY);

        return hours > 0 ? "HACE "+hours+" HORAS" : minutes > 0 ? "HACE "+minutes+" MINUTOS" :
                "HACE "+seconds+" SEGUNDOS";

    }
}
