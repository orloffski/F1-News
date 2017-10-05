package by.madcat.development.f1newsreader.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.utils.SystemUtils;

public class ServiceBootBroadcastReceiver extends BroadcastReceiver {
    public ServiceBootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SystemUtils.addServiceToAlarmManager(
                context,
                PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getBoolean("refresh_interval_on", false),
                Integer.parseInt(PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getString("refresh_interval", context.getString(R.string.intervals_default_value)))
        );
    }
}
