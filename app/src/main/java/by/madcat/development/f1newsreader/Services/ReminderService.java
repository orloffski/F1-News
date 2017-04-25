package by.madcat.development.f1newsreader.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.classesUI.NewsListActivity;

public class ReminderService extends IntentService {

    private static final String TAG = "ReminderService";
    public static final String VIBRO_IS_ON = "vibro_is_on";
    public static final String RINGTONE_URI = "ringtone_uri";
    public static final String TIME_PAUSE = "time_pause";

    public ReminderService() {
        super(TAG);
    }

    public static Intent newIntent(Context context, boolean vibroIsOn, String ringtoneUri, long timePause){
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
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if(isOn) {
            Log.d("test", "set alarm manager");
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), timePause, pi);
        }
        else{
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    private void runReminder(final Intent intent){
        Log.d("test", "run reminder");
        Timer timer = new Timer();

        TimerTask notificationTimerTask = new TimerTask() {
            @Override
            public void run() {
                Resources resources = getResources();
                Intent i = NewsListActivity.newIntent(getApplicationContext());
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);

                Resources res = getApplicationContext().getResources();

                Notification notification = new NotificationCompat.Builder(getApplicationContext())
                        .setTicker(resources.getString(R.string.reminder_ticker))
                        .setSmallIcon(R.drawable.ic_notif_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setContentTitle(resources.getString(R.string.reminder_ticker))
                        .setContentText(resources.getString(R.string.reminder_content_text))
                        .setSound(Uri.parse(intent.getStringExtra(ReminderService.RINGTONE_URI)))
                        .setVibrate((new long[] { 1000, 1000, 1000, 1000, 1000 }))
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                Log.d("test", "send notification");
                notificationManager.notify(0, notification);
            }
        };

        TimerTask runReminderTimerTask = new TimerTask() {
            @Override
            public void run() {
                runReminder(intent);
            }
        };

        if(System.currentTimeMillis()/1000 > SystemUtils.getNextGpTime(this) - intent.getIntExtra(TIME_PAUSE, 0)){
            long delay = 1000 * 60 * 60 * 3;
            Log.d("test", "start reload whith: " + delay/1000 + " seconds");
            timer.schedule(runReminderTimerTask, delay);
        }else{
            long delay = SystemUtils.getNextGpTime(this) - System.currentTimeMillis()/1000 - intent.getIntExtra(TIME_PAUSE, 0)/1000;
            Log.d("test", "start timer whith: " + delay + " seconds");
            timer.schedule(notificationTimerTask, delay);
        }
    }
}
