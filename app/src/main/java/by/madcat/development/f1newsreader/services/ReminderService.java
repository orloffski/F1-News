package by.madcat.development.f1newsreader.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.utils.PreferencesUtils;
import by.madcat.development.f1newsreader.utils.SystemUtils;
import by.madcat.development.f1newsreader.classesUI.NewsListActivity;

public class ReminderService extends IntentService {

    public static final int NOTIFICATION_ID = 1001002;
    private static final int SERVICE_INTENT_ID = 1001002;
    private static final int NOTIFICATION_INTENT_ID = 1001004;

    private static final String TAG = "ReminderService";
    public static final String VIBRO_IS_ON = "vibro_is_on";
    public static final String RINGTONE_URI = "ringtone_uri";
    public static final String TIME_PAUSE = "time_pause";

    public ReminderService() {
        super(TAG);
    }

    public static Intent newIntent(Context context, boolean vibroIsOn, String ringtoneUri, int timePause){
        Intent intent = new Intent(context, ReminderService.class);
        intent.putExtra(VIBRO_IS_ON, vibroIsOn);
        intent.putExtra(RINGTONE_URI, ringtoneUri);
        intent.putExtra(TIME_PAUSE, timePause);

        return intent;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        runReminder(intent);
    }

    public static void setServiceAlarm(Context context, boolean isOn, int timePause, boolean vibroIsOn, String ringtoneUri){
        Intent i = ReminderService.newIntent(context, vibroIsOn, ringtoneUri, timePause);
        PendingIntent pi = PendingIntent.getService(context, SERVICE_INTENT_ID, i, PendingIntent.FLAG_ONE_SHOT);
        int delay = (int)(PreferencesUtils.getNextGpTime(context) - System.currentTimeMillis()/1000 - timePause/1000);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, delay);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if(isOn && delay > 0) {
            if(Build.VERSION.SDK_INT >= 23){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            }else if (Build.VERSION.SDK_INT >= 19){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            }else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            }
        }
        else{
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    private void runReminder(final Intent intent){
        Resources resources = getResources();
        Intent i = NewsListActivity.newIntent(getApplicationContext());
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), NOTIFICATION_INTENT_ID, i, 0);

        long[] vibrate;
        if(intent.getBooleanExtra(VIBRO_IS_ON, false)){
            vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 };
        }else{
            vibrate = new long[]{};
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.reminder_ticker))
                .setSmallIcon(R.drawable.ic_notif_logo)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(resources.getString(R.string.reminder_ticker))
                .setContentText(getReminderNotification())
                .setSound(Uri.parse(intent.getStringExtra(ReminderService.RINGTONE_URI)))
                .setVibrate(vibrate)
                .setContentIntent(pi)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private String getReminderNotification(){
        return getResources().getString(R.string.reminder_content_text,
                PreferencesUtils.getNextGpCountry(getApplicationContext()),
                SystemUtils.getNextGpTimeout(getApplicationContext()));
    }
}
