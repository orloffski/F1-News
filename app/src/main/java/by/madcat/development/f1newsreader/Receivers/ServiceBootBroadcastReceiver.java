package by.madcat.development.f1newsreader.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.BackgroundLoadNewsService;

public class ServiceBootBroadcastReceiver extends BroadcastReceiver {
    public ServiceBootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int refresh_interval = Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("refresh_interval", context.getString(R.string.intervals_default_value)));
        BackgroundLoadNewsService.setServiceAlarm(context, true, refresh_interval);
    }
}
