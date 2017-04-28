package by.madcat.development.f1newsreader.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import by.madcat.development.f1newsreader.R;

import static by.madcat.development.f1newsreader.dataInet.LoadNewsTask.IMAGE_PATH;

public class SystemUtils {

    public static final String BG_LOAD_FLAG = "bg_load_run";
    public static final String UI_LOAD_FLAG = "ui_load_run";

    public static final String GP_DATA_COUNTRY = "gp_country";
    public static final String GP_DATA_DATE = "gp_date";
    public static final String GP_DATA_TIMESTAMP = "gp_timestamp";

    public final static String REMINDER_RINGTONE = "reminder_ringtone";

    public final static String WEEKEND_TITLE = "weekend_title";
    public final static String WEEKEND_IMAGE = "weekend_image";
    public final static String WEEKEND_1ST_DAY_TITLE = "weekend_1st_day_title";
    public final static String WEEKEND_2ND_DAY_TITLE = "weekend_2nd_day_title";
    public final static String WEEKEND_3RD_DAY_TITLE = "weekend_3rd_day_title";
    public final static String WEEKEND_1ST_DAY_TEXT = "weekend_1st_day_text";
    public final static String WEEKEND_2ND_DAY_TEXT = "weekend_2nd_day_text";
    public final static String WEEKEND_3RD_DAY_TEXT = "weekend_3rd_day_text";

    public static final boolean isNetworkAvailableAndConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworcConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworcConnected;
    }

    public static final boolean getUiLoadFlag(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(UI_LOAD_FLAG, false);
    }

    public static final void setUiLoadFlag(Context context, boolean flag){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putBoolean(UI_LOAD_FLAG, flag);
        editor.commit();
    }

    public static final boolean getBgLoadFlag(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(BG_LOAD_FLAG, false);
    }

    public static final void setBgLoadFlag(Context context, boolean flag){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putBoolean(BG_LOAD_FLAG, flag);
        editor.commit();
    }

    public static String getNextGpData(Context context){
        return getNextGpCountry(context)
                + "\n"
                + getNextGpDate(context);
    }

    public static String getNextGpCountry(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(GP_DATA_COUNTRY, "");
    }

    public static String getNextGpDate(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(GP_DATA_DATE, "");
    }

    public static String getNextGpTimeout(Context context){
        int timeout = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("reminder_interval", "0"));
        List<String> intervals = Arrays.asList(context.getResources().getStringArray(R.array.intervals_milliseconds));

        int index = intervals.indexOf(String.valueOf(timeout));

        String[] intervals_titles = context.getResources().getStringArray(R.array.intervals_title);

        return intervals_titles[index];
    }

    public static int getNextGpTime(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(GP_DATA_TIMESTAMP, 0);
    }

    public static void saveTimersData(String country, String date, int timestamp, Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);

        editor.putString(SystemUtils.GP_DATA_COUNTRY, country);
        editor.putString(SystemUtils.GP_DATA_DATE, date);
        editor.putInt(SystemUtils.GP_DATA_TIMESTAMP, timestamp);
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

    public static String loadRingtoneTitle(Context context){
        String ringtoneUriString = PreferenceManager.getDefaultSharedPreferences(context).getString(REMINDER_RINGTONE, "");
        Uri ringtoneUri = Uri.parse(ringtoneUriString);
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        return ringtone.getTitle(context);
    }

    public static void saveWeekendData(String weekendTitle, String weekendImageLink, Map<String, String> weekendData, Context context){
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putString(WEEKEND_TITLE, weekendTitle);
        try {
            editor.putString(WEEKEND_IMAGE, loadWeekendImage(weekendImageLink, context));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public static String getWeekendTitle(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_TITLE, "");
    }

    public static String getWeekendImage(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(WEEKEND_IMAGE, "");
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

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.edit();
    }

    private static String loadWeekendImage(String imageUrl, Context context) throws IOException {
        File sdPath = context.getFilesDir();

        sdPath = new File(sdPath.getAbsolutePath() + "/" + IMAGE_PATH);

        if(!sdPath.exists())
            sdPath.mkdirs();

        String filename = StringUtils.getImageNameFromURL(imageUrl);
        File imageOnMemory = new File(sdPath, filename);

        Bitmap image;

        OutputStream fOut;
        fOut = new FileOutputStream(imageOnMemory);

        InputStream in = new URL(imageUrl).openStream();
        if(in != null) {
            image = BitmapFactory.decodeStream(in);
            image.compress(Bitmap.CompressFormat.JPEG, 55, fOut);
        }

        fOut.flush();
        fOut.close();
        in.close();

        return filename;
    }
}
