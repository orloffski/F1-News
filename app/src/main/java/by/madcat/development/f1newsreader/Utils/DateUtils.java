package by.madcat.development.f1newsreader.Utils;

import java.util.Date;

public final class DateUtils {
    public static final String NEXT_GP_TITLE = "До старта: ";
    public static final String DAYS_SUFF = "д";
    public static final String HOURS_SUFF = "ч";
    public static final String MINUTES_SUFF = "м";
    public static final String SECONDS_SUFF = "с";

    public static final int SECONDS_IN_DAY = 86400;
    public static final int SECONDS_IN_HOUR = 3600;
    public static final int SECONDS_IN_MINUTE = 60;

    public static String transformDateTime(String dateTime){
        StringBuilder builder = new StringBuilder();

        String[] date = dateTime.split(",", 0);

        String dateNumber = date[0].split(" ")[0];
        String monthNumber = date[0].split(" ")[1];
        String yearNumber = date[0].split(" ")[2];

        builder.append(yearNumber).append("-");

        if(getMonthNumber(monthNumber) + 1 < 10)
            builder.append("0");
        builder.append(getMonthNumber(monthNumber) + 1).append("-");

        if(dateNumber.length() < 2)
            builder.append("0");
        builder.append(dateNumber);

        builder.append("_").append(date[1]);

        return builder.toString();
    }

    public static String untransformDateTime(String dateTime){
        StringBuilder builder = new StringBuilder();

        String[] date = dateTime.split("_", 0);

        String yearNumber = date[0].split("-")[0];
        String monthNumber = date[0].split("-")[1];
        String dateNumber = date[0].split("-")[2];

        builder.append(dateNumber).append(".").append(monthNumber).append(".").append(yearNumber);

        return builder.toString();
    }

    private static int getMonthNumber(String monthName){
        String[] months = new String[]{"января","февраля","марта",
                "апреля","мая","июня","июля","августа","сентября",
                "октября","ноября","декабря"};

        for(int i =0; i < months.length; i++)
            if(months[i].equals(monthName))
                return i;

        return -1;
    }

    public static String getNextGpString(int timestamp){
        String nextGp = NEXT_GP_TITLE;

        long seconds = (timestamp - (new Date().getTime()/1000));

        nextGp += getDaysCount(seconds) + DAYS_SUFF + " ";

        seconds -= (getDaysCount(seconds) * SECONDS_IN_DAY);

        nextGp += getHoursCount(seconds) + HOURS_SUFF + " ";

        seconds -= (getHoursCount(seconds) * SECONDS_IN_HOUR);

        nextGp += getMinutesCount(seconds) + MINUTES_SUFF + " ";

        seconds -= (getMinutesCount(seconds) * SECONDS_IN_MINUTE);

        nextGp += seconds + SECONDS_SUFF;

        return nextGp;
    }

    private static long getDaysCount(long seconds){
        return seconds / SECONDS_IN_DAY;
    }

    private static long getHoursCount(long seconds){
        return seconds / SECONDS_IN_HOUR;
    }

    private static long getMinutesCount(long seconds){
        return seconds / SECONDS_IN_MINUTE;
    }
}
