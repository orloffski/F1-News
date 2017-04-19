package by.madcat.development.f1newsreader.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

public class SystemUtils {

    public static final String BG_LOAD_FLAG = "bg_load_run";
    public static final String UI_LOAD_FLAG = "ui_load_run";

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
}
