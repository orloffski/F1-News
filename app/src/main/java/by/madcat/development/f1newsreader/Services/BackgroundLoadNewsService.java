package by.madcat.development.f1newsreader.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import by.madcat.development.f1newsreader.Interfaces.NewsLoadSender;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.classesUI.NewsListActivity;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;
import by.madcat.development.f1newsreader.dataInet.LoadLinkListTask;
import by.madcat.development.f1newsreader.dataInet.NewsLinkListToLoad;

public class BackgroundLoadNewsService extends IntentService implements NewsLoadSender{
    private static final String TAG = "BackgroundLoadNewsService";
    public static final int NOTIFICATION_ID = 1;
    private static final int SERVICE_INTENT_ID = 1;
    private static final int NOTIFICATION_INTENT_ID = 3;

    public static Intent newIntent(Context context){
        return new Intent(context, BackgroundLoadNewsService.class);
    }

    public BackgroundLoadNewsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(!NewsLinkListToLoad.getInstance(this).isLock())
            runLoad();
    }

    public static void setServiceAlarm(Context context, boolean isOn, int timePause){
        Intent i = BackgroundLoadNewsService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, SERVICE_INTENT_ID, i, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        SystemUtils.stopOldService(alarmManager, i, context);

        if(isOn) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + timePause, timePause, pi);
            }else{
                alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + timePause, timePause, pi);
            }
        }
        else{
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public void sendNotification(int countNews){
        if(countNews != 0)
            sendMessage(countNews);
    }

    public void sendMessage(int countNews){
        Resources resources = getResources();
        Intent i = NewsListActivity.newIntent(this);
        PendingIntent pi = PendingIntent.getActivity(this, NOTIFICATION_INTENT_ID, i, PendingIntent.FLAG_CANCEL_CURRENT);

        countNews += SystemUtils.getNumberInIssetNotification(NOTIFICATION_ID, (NotificationManager)getSystemService(NOTIFICATION_SERVICE));

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.get_new_news))
                .setSmallIcon(R.drawable.ic_notif_logo)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setNumber(countNews)
                .setContentTitle(resources.getString(R.string.get_new_news))
                .setContentText(resources.getQuantityString(R.plurals.news_plurals, countNews, countNews))
                .setContentIntent(pi)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void runLoad(){
        if(!SystemUtils.isNetworkAvailableAndConnected(getApplicationContext()))
            return;

        InternetDataRouting dataRouting = InternetDataRouting.getInstance();
        LoadLinkListTask loadLinksTask = new LoadLinkListTask(dataRouting.getRoutingMap(), dataRouting.getMainSiteAdress(), getApplicationContext(), this);
        loadLinksTask.execute();
    }
}
