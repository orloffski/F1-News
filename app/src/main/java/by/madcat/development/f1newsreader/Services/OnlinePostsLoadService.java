package by.madcat.development.f1newsreader.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.Utils.JsonParseUtils;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;
import by.madcat.development.f1newsreader.Models.OnlinePost;

import static by.madcat.development.f1newsreader.classesUI.OnlineTextFragment.BROADCAST_ACTION;

public class OnlinePostsLoadService extends Service {

    public final static String BROADCAST_SERVICE_ACTION = "online_posts_service";
    public final static String BROADCAST_ACTION_DATA = "online_posts_data";

    private final static int SHORT_INTERVAL = 5000;
    private final static int LONG_INTERVAL = 60000;

    BroadcastReceiver receiver;

    private String data;
    private Timer timer;
    private TimerTask task;

    private LinkedList<OnlinePost> posts;
    private LinkedList<OnlinePost> newPosts;
    private boolean longInterval;

    @Override
    public IBinder onBind(Intent intent) {
        timer = new Timer();
        longInterval = true;

        timerTaskReSchedule(0, LONG_INTERVAL);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                newPosts = null;
                timerTaskReSchedule(SHORT_INTERVAL, SHORT_INTERVAL);
            }
        };

        IntentFilter filter = new IntentFilter(BROADCAST_SERVICE_ACTION);
        registerReceiver(receiver, filter);

        return new Binder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        timer.cancel();

        unregisterReceiver(receiver);

        return super.onUnbind(intent);
    }

    void sendDataLoading(){
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(BROADCAST_ACTION_DATA, data);
        sendBroadcast(intent);

        if(longInterval)
            timerTaskReSchedule(LONG_INTERVAL, LONG_INTERVAL);
        else
            timerTaskReSchedule(SHORT_INTERVAL, SHORT_INTERVAL);
    }

    String loadOnlinePosts(){
        String jsonString = null;

        try {
            jsonString = DocParseUtils.getJsonString(InternetDataRouting.TEXT_ONLINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(jsonString != null) {
            if(posts == null) {
                posts = (LinkedList<OnlinePost>) JsonParseUtils.getPostsFromJsonString(jsonString);

                return jsonString;
            }else{
                longInterval = false;

                if(newPosts == null)
                    newPosts = (LinkedList<OnlinePost>) JsonParseUtils.getPostsFromJsonString(jsonString);

                if(getFirstNewPostIndex() == -1)
                    return "";

                OnlinePost post = newPosts.get(getFirstNewPostIndex());
                posts.add(0, post);

                return JsonParseUtils.putOnlinePostToJsonString(post);
            }
        }

        return "";
    }

    private void timerTaskReSchedule(int delay, int interval){
        if (timer != null) timer.cancel();
        timer = new Timer();

        task = new TimerTask() {
            public void run() {
                data = loadOnlinePosts();
                sendDataLoading();
            }
        };
        timer.schedule(task, delay, interval);
    }

    private int getFirstNewPostIndex(){
        int index = -1;

        if(newPosts.size() == posts.size()){
            longInterval = true;
            newPosts = null;
            return index;
        }

        for(int i = 0; i < newPosts.size(); i++){
            if(newPosts.get(i).getOnlinePostTime().equals(posts.get(0).getOnlinePostTime())) {
                index = i - 1;
                break;
            }
        }

        return index;
    }
}
