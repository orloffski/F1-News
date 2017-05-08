package by.madcat.development.f1newsreader.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import by.madcat.development.f1newsreader.classesUI.NewsListFragment;
import by.madcat.development.f1newsreader.Interfaces.NewsLoadSender;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;
import by.madcat.development.f1newsreader.dataInet.LoadLinkListTask;
import by.madcat.development.f1newsreader.dataInet.NewsLinkListToLoad;

public class UILoadNewsService extends Service implements NewsLoadSender {
    private Intent intent;

    public UILoadNewsService() {
        this.intent = new Intent(NewsListFragment.BROADCAST_ACTION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(NewsLinkListToLoad.getInstance(this).getNewsCount() == 0) {
            InternetDataRouting dataRouting = InternetDataRouting.getInstance();
            LoadLinkListTask loadLinksTask = new LoadLinkListTask(dataRouting.getRoutingMap(), dataRouting.getMainSiteAdress(), getApplicationContext(), this);
            loadLinksTask.execute();
        }else{
            sendNotification(-1);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void sendNotification(int count) {
        this.intent = new Intent(NewsListFragment.BROADCAST_ACTION);
        this.intent.putExtra(NewsListFragment.SERVICE_DATA, count);
        sendBroadcast(intent);
    }
}
