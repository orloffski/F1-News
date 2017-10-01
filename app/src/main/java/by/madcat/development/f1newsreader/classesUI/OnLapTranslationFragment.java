package by.madcat.development.f1newsreader.classesUI;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.OnlineSessionLoadService;
import by.madcat.development.f1newsreader.Utils.JsonParseUtils;
import by.madcat.development.f1newsreader.adapters.OnLapTimingsAdapter;
import by.madcat.development.f1newsreader.dataInet.Models.RaceMode;
import by.madcat.development.f1newsreader.dataInet.Models.TimingElement;
import by.madcat.development.f1newsreader.styling.CustomViews.RaceTrackView;

import static android.content.Context.BIND_AUTO_CREATE;
import static by.madcat.development.f1newsreader.Services.OnlineSessionLoadService.BROADCAST_ACTION_DATA;
import static by.madcat.development.f1newsreader.Services.OnlineSessionLoadService.INTERVAL_DATA;

public class OnLapTranslationFragment extends Fragment{
    public final static String BROADCAST_ACTION = "online_session_receiver";

    private GridLayoutManager lLayout;
    private RecyclerView onlapTimings;
    private OnLapTimingsAdapter onLapAdapter;

    private RaceTrackView raceTrackView;

    public LinkedList<TimingElement> timings;
    private RaceMode raceMode;

    private ServiceConnection sConn;
    private boolean bound;

    BroadcastReceiver receiver;


    public static OnLapTranslationFragment newInstance() {
        return new OnLapTranslationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timings = new LinkedList<>();

        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // получение данных от сервиса
                String data = intent.getStringExtra(BROADCAST_ACTION_DATA);

                if (data == null || data.equals(""))
                    return;

                timings.clear();
                raceMode = JsonParseUtils.getRaceMode(data);

                if(raceMode == null || raceMode.getMode().equals("practice"))
                    return;


                if(raceMode.getMode().equals("race")){
                    timings.addAll(JsonParseUtils.getRaceTimings(data));
                }

                // start race animate
                if(!raceMode.getStatus().equals("completed")){
                    raceTrackView.updateRaceData(timings);
                    setOnLapTimings(timings);
                }else
                    raceTrackView.updateRaceData(null);
            }
        };

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_on_lap_translation, container, false);

        raceTrackView = (RaceTrackView)rootView.findViewById(R.id.map_view);
        onlapTimings = (RecyclerView)rootView.findViewById(R.id.onlap_timings);

        lLayout = new GridLayoutManager(getContext(), 6, GridLayoutManager.HORIZONTAL, false);
        onlapTimings.setHasFixedSize(true);
        onlapTimings.setLayoutManager(lLayout);

        loadOnlineSessionData();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!bound) return;
        unregisterService();
    }

    @Override
    public void onResume() {
        super.onResume();

        timings.clear();
        loadOnlineSessionData();

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!bound) return;
        unregisterService();
    }

    private void loadOnlineSessionData(){
        Intent intent = new Intent(getActivity(), OnlineSessionLoadService.class);
        intent.putExtra(INTERVAL_DATA, 15000);
        getActivity().bindService(intent, sConn, BIND_AUTO_CREATE);
    }

    private void unregisterService(){
        getActivity().unregisterReceiver(receiver);

        if (!bound) return;
        getActivity().unbindService(sConn);
        bound = false;
    }

    private void setOnLapTimings(LinkedList<TimingElement> timings){
        onLapAdapter = new OnLapTimingsAdapter(timings);
        onlapTimings.setAdapter(onLapAdapter);
    }
}