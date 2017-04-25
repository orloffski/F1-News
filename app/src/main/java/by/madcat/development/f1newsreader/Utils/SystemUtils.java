package by.madcat.development.f1newsreader.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import by.madcat.development.f1newsreader.R;

public class SystemUtils {

    public static final String BG_LOAD_FLAG = "bg_load_run";
    public static final String UI_LOAD_FLAG = "ui_load_run";

    public static final String GP_DATA_COUNTRY = "gp_country";
    public static final String GP_DATA_DATE = "gp_date";
    public static final String GP_DATA_TIMESTAMP = "gp_timestamp";

    public final static String REMINDER_RINGTONE = "reminder_ringtone";

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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(UI_LOAD_FLAG, flag);
        editor.commit();
    }

    public static final boolean getBgLoadFlag(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(BG_LOAD_FLAG, false);
    }

    public static final void setBgLoadFlag(Context context, boolean flag){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(BG_LOAD_FLAG, flag);
        editor.commit();
    }

    public static void loadTimersData(String urlString, Context context) throws IOException {
        org.jsoup.nodes.Document jsDoc = DocParseUtils.getJsDoc(urlString);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GP_DATA_COUNTRY, DocParseUtils.getNextGpTitle(jsDoc));
        editor.putString(GP_DATA_DATE, DocParseUtils.getNextGpDate(jsDoc));
        editor.putInt(GP_DATA_TIMESTAMP, Integer.parseInt(DocParseUtils.getNextGpTimestamp(jsDoc)));
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

    public static void saveRingtoneData(String ringtonUri, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
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
}
