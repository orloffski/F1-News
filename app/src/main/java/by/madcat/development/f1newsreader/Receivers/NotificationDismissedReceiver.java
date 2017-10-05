package by.madcat.development.f1newsreader.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import by.madcat.development.f1newsreader.services.BackgroundLoadNewsService;
import by.madcat.development.f1newsreader.utils.PreferencesUtils;

public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt(BackgroundLoadNewsService.NOTIFICATION_CODE);

        if(notificationId == BackgroundLoadNewsService.NOTIFICATION_ID)
            PreferencesUtils.clearIssetNotificationsCount(context);
    }
}
