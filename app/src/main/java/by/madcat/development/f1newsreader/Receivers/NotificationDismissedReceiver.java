package by.madcat.development.f1newsreader.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import by.madcat.development.f1newsreader.Services.BackgroundLoadNewsService;
import by.madcat.development.f1newsreader.Utils.SystemUtils;

public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt(BackgroundLoadNewsService.NOTIFICATION_CODE);

        if(notificationId == BackgroundLoadNewsService.NOTIFICATION_ID)
            SystemUtils.clearIssetNotificationsCount(context);
    }
}
