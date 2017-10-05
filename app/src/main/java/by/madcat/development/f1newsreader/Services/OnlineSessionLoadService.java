package by.madcat.development.f1newsreader.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.Utils.JsonParseUtils;
import by.madcat.development.f1newsreader.DataInet.InternetDataRouting;

import static by.madcat.development.f1newsreader.classesUI.OnlineSessionFragment.BROADCAST_ACTION;

public class OnlineSessionLoadService extends Service {

    public final static String BROADCAST_SERVICE_ACTION = "online_session_service";
    public final static String BROADCAST_ACTION_DATA = "online_session_data";

    public final static String INTERVAL_DATA = "interval_data";

    private final static int INTERVAL = 20000;
    private int interval = 0;

    BroadcastReceiver receiver;

    private String data;
    private Timer timer;
    private TimerTask task;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        timer = new Timer();

        interval = intent.getIntExtra(INTERVAL_DATA, 0);

        timerTaskReSchedule(0, getInterval());

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                timerTaskReSchedule(0, getInterval());
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
        String jsonString = null;

        try {
            jsonString = DocParseUtils.getJsonString(InternetDataRouting.TEXT_ONLINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int session_id = JsonParseUtils.getSessionId(jsonString);
        if(session_id == 0)
            return "";

        String session_link = getResources().getString(R.string.session_link, session_id);

        try {
            jsonString = DocParseUtils.getJsonString(session_link);
            JsonParseUtils.getRaceMode(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private void sendDataLoading(){
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(BROADCAST_ACTION_DATA, data);
        sendBroadcast(intent);

        timerTaskReSchedule(getInterval(), getInterval());
    }

    private int getInterval(){
        if(this.interval == 0)
            return INTERVAL;
        else
            return interval;
    }
}
