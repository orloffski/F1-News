package by.madcat.development.f1newsreader.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;
import by.madcat.development.f1newsreader.dataInet.OnlinePost;

import static by.madcat.development.f1newsreader.classesUI.TextOnlineActivity.BROADCAST_ACTION;

public class OnlinePostsLoadService extends Service {

    private String data;
    private Timer timer;
    private TimerTask task;

    @Override
    public IBinder onBind(Intent intent) {
        timer = new Timer();

        schedule();

        return new Binder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        timer.cancel();

        return super.onUnbind(intent);
    }

    void schedule(){
        if (task != null) task.cancel();
        task = new TimerTask() {
            public void run() {
                data = loadOnlinePosts();
                sendDataLoading();
            }
        };
        timer.schedule(task, 0, 15000);
    }

    void sendDataLoading(){
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("online_posts_data", data);
        sendBroadcast(intent);
    }

    String loadOnlinePosts(){
        Document jsDoc = null;
        try {
            jsDoc = DocParseUtils.getJsDoc(InternetDataRouting.TEXT_ONLINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(jsDoc != null)
            return jsDoc.text();

        return "";
    }
}
