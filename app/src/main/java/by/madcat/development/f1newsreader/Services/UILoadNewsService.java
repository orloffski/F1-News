package by.madcat.development.f1newsreader.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import by.madcat.development.f1newsreader.classesUI.NewsListFragment;
import by.madcat.development.f1newsreader.Interfaces.NewsLoadSender;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;
import by.madcat.development.f1newsreader.dataInet.LoadLinkListTask;

public class UILoadNewsService extends Service implements NewsLoadSender {
    private int countNewsToLoad;
    private int countNewsLoaded;
    private Intent intent;
    private static boolean isServiceRun;

    public UILoadNewsService() {
        this.intent = new Intent(NewsListFragment.BROADCAST_ACTION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.isServiceRun = true;

        InternetDataRouting dataRouting = InternetDataRouting.getInstance();
        LoadLinkListTask loadLinksTask = new LoadLinkListTask(dataRouting.getRoutingMap(), dataRouting.getMainSiteAdress(), getApplicationContext(), this);
        loadLinksTask.execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void checkNewsLoadCount(boolean loaded) {
        if(loaded)
            this.countNewsLoaded += 1;

        if(this.countNewsLoaded == this.countNewsToLoad)
            loadComplete();
    }

    @Override
    public void sendNewsLoadCount(int count) {
        if(count == 0){
            this.countNewsToLoad = 0;
            this.countNewsLoaded = 0;
        }else {
            this.countNewsToLoad = count;
        }
    }

    @Override
    public void cancelLinkLoad() {
        this.countNewsToLoad--;
        checkNewsLoadCount(false);
    }

    @Override
    public void loadStart() {
    }

    @Override
    public void loadComplete() {
        this.isServiceRun = false;
        sendNotification(countNewsToLoad);
    }

    @Override
    public void loadCanceled() {
        this.isServiceRun = false;
        sendNotification(0);
    }

    private void sendNotification(int count){
        this.intent = new Intent(NewsListFragment.BROADCAST_ACTION);
        this.intent.putExtra(NewsListFragment.SERVICE_DATA, count);
        sendBroadcast(intent);
    }

    public static boolean isServiceRun() {
        return isServiceRun;
    }
}
