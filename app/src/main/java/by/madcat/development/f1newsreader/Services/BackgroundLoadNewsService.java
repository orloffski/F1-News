package by.madcat.development.f1newsreader.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import by.madcat.development.f1newsreader.Interfaces.NewsLoadSender;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Receivers.NotificationDismissedReceiver;
import by.madcat.development.f1newsreader.Utils.PreferencesUtils;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.classesUI.NewsListActivity;
import by.madcat.development.f1newsreader.DataInet.InternetDataRouting;
import by.madcat.development.f1newsreader.DataInet.LoadLinkListTask;
import by.madcat.development.f1newsreader.DataInet.NewsLinkListToLoad;

public class BackgroundLoadNewsService extends IntentService implements NewsLoadSender{
    private static final String TAG = "BackgroundLoadNewsService";
    public static final int NOTIFICATION_ID = 1001001;
    public static final int SERVICE_INTENT_ID = 1001001;
    private static final int NOTIFICATION_INTENT_ID = 1001003;
    public static String NOTIFICATION_CODE = "by.madcat.development.f1newsreader";

    public static Intent newIntent(Context context){
        return new Intent(context, BackgroundLoadNewsService.class);
    }

    public BackgroundLoadNewsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(!NewsLinkListToLoad.getInstance(this, getApplicationContext()).isLock())
            runLoad();
        else {
            SystemUtils.addServiceToAlarmManager(
                    getApplicationContext(),
                    true,
                    Integer.parseInt(PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext())
                            .getString("refresh_interval", getApplication().getString(R.string.intervals_default_value)))
                    );
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

        countNews += SystemUtils.getNumberInIssetNotificationsCount(NOTIFICATION_ID, (NotificationManager)getSystemService(NOTIFICATION_SERVICE), getApplicationContext());

        PreferencesUtils.setIssetNotificationsCount(getApplicationContext(), countNews);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.get_new_news))
                .setSmallIcon(R.drawable.ic_notif_logo)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setNumber(countNews)
                .setContentTitle(resources.getString(R.string.get_new_news))
                .setContentText(resources.getQuantityString(R.plurals.news_plurals, countNews, countNews))
                .setContentIntent(pi)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setDeleteIntent(createOnDismissedIntent(getApplicationContext(), NOTIFICATION_ID));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void runLoad(){
        if(!SystemUtils.isNetworkAvailableAndConnected(getApplicationContext())) {
            SystemUtils.addServiceToAlarmManager(
                    getApplicationContext(),
                    true,
                    Integer.parseInt(PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext())
                            .getString("refresh_interval", getApplication().getString(R.string.intervals_default_value)))
                    );
            return;
        }

        InternetDataRouting dataRouting = InternetDataRouting.getInstance();
        LoadLinkListTask loadLinksTask = new LoadLinkListTask(dataRouting.getRoutingMap(), dataRouting.getMainSiteAdress(), getApplicationContext(), this);
        loadLinksTask.execute();
    }

    private PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra(NOTIFICATION_CODE, notificationId);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }
}
