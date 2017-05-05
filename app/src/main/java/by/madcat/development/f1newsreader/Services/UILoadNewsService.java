package by.madcat.development.f1newsreader.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.classesUI.NewsListFragment;
import by.madcat.development.f1newsreader.Interfaces.NewsLoadSender;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;
import by.madcat.development.f1newsreader.dataInet.LoadLinkListTask;

public class UILoadNewsService extends Service implements NewsLoadSender {
    private Intent intent;
    private static boolean isServiceRun;
    private static boolean loadIsBG;

    public UILoadNewsService() {
        this.intent = new Intent(NewsListFragment.BROADCAST_ACTION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadIsBG = false;

        if(!SystemUtils.getBgLoadFlag(getApplicationContext())) {
            SystemUtils.setUiLoadFlag(getApplicationContext(), true);
            this.isServiceRun = true;

            InternetDataRouting dataRouting = InternetDataRouting.getInstance();
            LoadLinkListTask loadLinksTask = new LoadLinkListTask(dataRouting.getRoutingMap(), dataRouting.getMainSiteAdress(), getApplicationContext(), this);
            loadLinksTask.execute();
        }else{
            loadIsBG = true;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void sendNotification(int count) {
        this.intent = new Intent(NewsListFragment.BROADCAST_ACTION);
        this.intent.putExtra(NewsListFragment.SERVICE_DATA, count);
        sendBroadcast(intent);
    }


    public static boolean isServiceRun() {
        return isServiceRun;
    }
}
