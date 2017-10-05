package by.madcat.development.f1newsreader.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.services.ReminderService;

public class ReminderBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int reminder_interval = Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("reminder_interval", context.getString(R.string.intervals_default_value)));
        ReminderService.setServiceAlarm(context, true, reminder_interval, getVibroIsOn(intent), getRingtoneUriString(intent));
    }

    public boolean getVibroIsOn(Intent intent){
        return intent.getBooleanExtra(ReminderService.VIBRO_IS_ON, false);
    }

    public String getRingtoneUriString(Intent intent){
        return intent.getStringExtra(ReminderService.RINGTONE_URI);
    }
}
