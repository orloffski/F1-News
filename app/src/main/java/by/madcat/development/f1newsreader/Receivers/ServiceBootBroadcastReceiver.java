package by.madcat.development.f1newsreader.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.BackgroundLoadNewsService;
import by.madcat.development.f1newsreader.Utils.SystemUtils;

public class ServiceBootBroadcastReceiver extends BroadcastReceiver {
    public ServiceBootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SystemUtils.addServiceToAlarmManager(
                context,
                PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getBoolean("refresh_interval_on", false)
        );
    }
}
