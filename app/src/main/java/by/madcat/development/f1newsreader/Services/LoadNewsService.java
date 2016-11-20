package by.madcat.development.f1newsreader.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.classesUI.NewsListActivity;
import by.madcat.development.f1newsreader.classesUI.NewsLoadSender;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;
import by.madcat.development.f1newsreader.dataInet.LoadLinkListTask;

public class LoadNewsService extends IntentService implements NewsLoadSender{
    private static final String TAG = "LoadNewsService";

    private int countNewsToLoad;
    private int countLoadedNews;

    public static Intent newIntent(Context context){
        return new Intent(context, LoadNewsService.class);
    }

    public LoadNewsService() {
        super(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isNetworkAvailableAndConnected())
            return Service.START_NOT_STICKY;

        InternetDataRouting dataRouting = InternetDataRouting.getInstance();
        LoadLinkListTask loadLinksTask = new LoadLinkListTask(dataRouting.getRoutingMap(), getApplicationContext(), this);
        loadLinksTask.execute();

        return Service.START_NOT_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public static void setServiceAlarm(Context context, boolean isOn, int timePause){
        Intent i = LoadNewsService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if(isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), timePause, pi);
        }
        else{
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

    private void sendNotification(int countNews){
        Resources resources = getResources();
        Intent i = NewsListActivity.newIntent(this);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.get_new_news))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(resources.getString(R.string.get_new_news))
                .setContentText(resources.getQuantityString(R.plurals.news_plurals, countNews, countNews))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notification);
    }

    @Override
    public void sendNewsCountToAdapter(int count) {
        this.countNewsToLoad = count;
        this.countLoadedNews = 0;
    }

    @Override
    public void sendNewsLoadToAdapter() {
        this.countLoadedNews +=1;

        if(this.countNewsToLoad == this.countLoadedNews)
            this.loadComplete();
    }

    @Override
    public void loadComplete() {
        if(countNewsToLoad == 0)
            return;

        sendNotification(countNewsToLoad);
    }

    @Override
    public void loadCanceled() {

    }
}
