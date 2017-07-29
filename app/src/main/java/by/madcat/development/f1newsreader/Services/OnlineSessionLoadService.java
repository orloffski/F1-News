package by.madcat.development.f1newsreader.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.Utils.JsonParseUtils;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;

import static by.madcat.development.f1newsreader.classesUI.OnlineSessionFragment.BROADCAST_ACTION;

public class OnlineSessionLoadService extends Service {

    public final static String BROADCAST_SERVICE_ACTION = "online_session_service";
    public final static String BROADCAST_ACTION_DATA = "online_session_data";

    private final static int INTERVAL = 60000;

    BroadcastReceiver receiver;

    private String data;
    private Timer timer;
    private TimerTask task;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("test", "session service bind");
        timer = new Timer();

        timerTaskReSchedule(0, INTERVAL);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                timerTaskReSchedule(0, INTERVAL);
            }
        };

        IntentFilter filter = new IntentFilter(BROADCAST_SERVICE_ACTION);
        registerReceiver(receiver, filter);

        return new Binder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        timer.cancel();

        unregisterReceiver(receiver);

        return super.onUnbind(intent);
    }

    private void timerTaskReSchedule(int delay, int interval){
        Log.d("test", "timerTaskReSchedule");
        if (timer != null) timer.cancel();
        timer = new Timer();

        task = new TimerTask() {
            public void run() {
                data = loadOnlineSessionData();
                sendDataLoading();
            }
        };
        timer.schedule(task, delay, interval);
    }

    private String loadOnlineSessionData(){
        Log.d("test", "loadOnlineSessionData start");
        String jsonString = null;

        try {
            jsonString = DocParseUtils.getJsonString(InternetDataRouting.TEXT_ONLINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int session_id = JsonParseUtils.getSessionId(jsonString);
        if(session_id == 0)
            return "";

        Log.d("test", "session id: " + session_id);

        String session_link = getResources().getString(R.string.session_link, session_id);

        Log.d("test", "session link: " + session_link);

        try {
            jsonString = DocParseUtils.getJsonString(session_link);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("test", "data: " + jsonString);
        return jsonString;
    }

    private void sendDataLoading(){
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(BROADCAST_ACTION_DATA, data);
        sendBroadcast(intent);

        timerTaskReSchedule(INTERVAL, INTERVAL);
    }
}
