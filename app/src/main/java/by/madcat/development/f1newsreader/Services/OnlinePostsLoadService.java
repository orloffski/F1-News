package by.madcat.development.f1newsreader.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import by.madcat.development.f1newsreader.dataInet.OnlinePost;

import static by.madcat.development.f1newsreader.classesUI.TextOnlineActivity.BROADCAST_ACTION;

public class OnlinePostsLoadService extends Service {

    private LinkedList<OnlinePost> posts;
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
        posts = new LinkedList<>();

        if (task != null) task.cancel();
        task = new TimerTask() {
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                posts.add(new OnlinePost("Test string skjnfkjsnjk dnkajs ndkjn kjsan kjdnaskj", sdf.format(new Date())));
                sendDataLoading();
            }
        };
        timer.schedule(task, 0, 1000);
    }

    void sendDataLoading(){
        String[] dates = new String[posts.size()];
        String[] onlinePosts = new String[posts.size()];
        int counter = 0;

        for(OnlinePost post : posts){
            dates[counter] = post.getOnlinePostTime();
            onlinePosts[counter] = post.getOnlinePostText();
            counter++;
        }

        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("dates", dates);
        intent.putExtra("posts", onlinePosts);
        sendBroadcast(intent);
    }
}
