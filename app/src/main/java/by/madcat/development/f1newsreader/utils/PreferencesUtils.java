package by.madcat.development.f1newsreader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class PreferencesUtils {

    public static final String GP_DATA_COUNTRY = "gp_country";
    public static final String GP_DATA_DATE = "gp_date";
    public static final String GP_DATA_TIMESTAMP = "gp_timestamp";

    public final static String REMINDER_RINGTONE = "reminder_ringtone";
    public static final String REMINDER_INTERVAL = "reminder_interval";

    public final static String WEEKEND_TITLE = "weekend_title";
    public final static String WEEKEND_IMAGE = "weekend_image";
    public final static String WEEKEND_1ST_DAY_TITLE = "weekend_1st_day_title";
    public final static String WEEKEND_2ND_DAY_TITLE = "weekend_2nd_day_title";
    public final static String WEEKEND_3RD_DAY_TITLE = "weekend_3rd_day_title";
    public final static String WEEKEND_1ST_DAY_TEXT = "weekend_1st_day_text";
    public final static String WEEKEND_2ND_DAY_TEXT = "weekend_2nd_day_text";
    public final static String WEEKEND_3RD_DAY_TEXT = "weekend_3rd_day_text";

    public static final String SAVE_IMAGES_ON_SD = "move_pic_to_sd";

    public static final String IMAGE_PATH = "F1NewsImages";

    public static final String NOTIFICATIONS_COUNT_ID = "notifications_count";

    public static final String AUTOSCROLLING_FLAG = "autoscrolling_text_online";
    public static final String SCREENOF_FLAG = "screen_off_disable";

    public static final String MAIN_IMAGE = "main_image_choose";

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.edit();
    }

    public static String getNextGpCountry(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(GP_DATA_COUNTRY, "");
    }

    public static String getNextGpDate(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(GP_DATA_DATE, "");
    }

    public static int getNextGpTime(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(GP_DATA_TIMESTAMP, 0);
    }

    public static void saveTimersData(String country, String date, int timestamp, Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);

        editor.putString(GP_DATA_COUNTRY, country);
        editor.putString(GP_DATA_DATE, date);
        editor.putInt(GP_DATA_TIMESTAMP, timestamp);
        editor.commit();
    }

    public static void saveRingtoneData(String ringtonUri, Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putString(REMINDER_RINGTONE, ringtonUri);
        editor.commit();
    }

    public static String loadRingtoneData(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(REMINDER_RINGTONE, "");
    }

    public static String getRingtoneTitle(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(REMINDER_RINGTONE, "");
    }

    public static void saveWeekendData(String weekendTitle, Map<String, String> weekendData, Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putString(WEEKEND_TITLE, weekendTitle);

        int counter = 1;

        for(Map.Entry entry : weekendData.entrySet()){
            switch (counter){
                case 1:
                    editor.putString(WEEKEND_1ST_DAY_TITLE, entry.getKey().toString());
                    editor.putString(WEEKEND_1ST_DAY_TEXT, entry.getValue().toString());
                    break;
                case 2:
                    editor.putString(WEEKEND_2ND_DAY_TITLE, entry.getKey().toString());
                    editor.putString(WEEKEND_2ND_DAY_TEXT, entry.getValue().toString());
                    break;
                case 3:
                    editor.putString(WEEKEND_3RD_DAY_TITLE, entry.getKey().toString());
                    editor.putString(WEEKEND_3RD_DAY_TEXT, entry.getValue().toString());
                    break;
            }
            counter++;
        }

        editor.commit();
    }

    public static Map<String, String> getWeekendData(Context context){
        Map<String, String> weekendData = new LinkedHashMap<>();

        weekendData.put(
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_1ST_DAY_TITLE, ""),
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_1ST_DAY_TEXT, "")
        );

        weekendData.put(
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_2ND_DAY_TITLE, ""),
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_2ND_DAY_TEXT, "")
        );

        weekendData.put(
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_3RD_DAY_TITLE, ""),
                PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_3RD_DAY_TEXT, "")
        );

        return weekendData;
    }

    public static String getWeekendTitle(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_TITLE, "");
    }

    public static String getReminderInterval(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(REMINDER_INTERVAL, "0");
    }

    public static boolean reminderIntervalIsOn(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("reminder_on", false);
    }

    public static boolean reminderVibrationIsOn(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("reminder_vibration", false);
    }

    public static boolean imagesOnSdCard(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SAVE_IMAGES_ON_SD, false);
    }

    public static int getNotificationsCount(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(NOTIFICATIONS_COUNT_ID, 0);
    }

    public static void setIssetNotificationsCount(Context context, int count){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putInt(NOTIFICATIONS_COUNT_ID, count);
        editor.commit();
    }

    public static void clearIssetNotificationsCount(Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putInt(NOTIFICATIONS_COUNT_ID, 0);
        editor.commit();
    }

    public static int getWeekendTitleFontSize(Context context){
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("gp_title_font_size", "12"));
    }

    public static int getWeekendTimerFontSize(Context context){
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("gp_timer_font_size", "12"));
    }

    public static void enableAutoScrolling(Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putBoolean(AUTOSCROLLING_FLAG, true);
        editor.commit();
    }

    public static void disableAutoScrolling(Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putBoolean(AUTOSCROLLING_FLAG, false);
        editor.commit();
    }

    public static boolean getAutoscrollingFlag(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(AUTOSCROLLING_FLAG, false);
    }

    public static String getMainImageNum(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(MAIN_IMAGE, "0");
    }

    public static void setScreenOffDisable(Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putBoolean(SCREENOF_FLAG, true);
        editor.commit();
    }

    public static void unsetScreenOffDisable(Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putBoolean(SCREENOF_FLAG, false);
        editor.commit();
    }

    public static boolean getScreenOffDisable(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SCREENOF_FLAG, false);
    }
}
