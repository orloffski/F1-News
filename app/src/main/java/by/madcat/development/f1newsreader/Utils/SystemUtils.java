package by.madcat.development.f1newsreader.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

public class SystemUtils {

    public static final String LOAD_FLAG = "load_run";

    public static final boolean isNetworkAvailableAndConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworcConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworcConnected;
    }

    public static final boolean getLoadFlag(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(LOAD_FLAG, false);
    }

    public static final void setLoadFlag(Context context, boolean flag){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOAD_FLAG, flag);
        editor.commit();
    }
}
