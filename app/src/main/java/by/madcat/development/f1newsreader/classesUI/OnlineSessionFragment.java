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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.OnlineSessionLoadService;
import by.madcat.development.f1newsreader.Utils.JsonParseUtils;
import by.madcat.development.f1newsreader.Utils.PreferencesUtils;
import by.madcat.development.f1newsreader.adapters.OnlineSessionAdapter;
import by.madcat.development.f1newsreader.dataInet.Models.RaceMode;
import by.madcat.development.f1newsreader.dataInet.Models.TimingElement;

import static android.content.Context.BIND_AUTO_CREATE;
import static by.madcat.development.f1newsreader.Services.OnlineSessionLoadService.BROADCAST_ACTION_DATA;
import static by.madcat.development.f1newsreader.Services.OnlineSessionLoadService.BROADCAST_SERVICE_ACTION;

public class OnlineSessionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public final static String BROADCAST_ACTION = "online_session_receiver";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private OnlineSessionAdapter adapter;

    private SessionStatusFragment sessionStatusFragment;

    public LinkedList<TimingElement> timings;
    private RaceMode raceMode;
    BroadcastReceiver receiver;

    private ServiceConnection sConn;
    private boolean bound;

    public static OnlineSessionFragment newInstance() {
        return new OnlineSessionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timings = new LinkedList<>();

        sessionStatusFragment = new SessionStatusFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.track_status, sessionStatusFragment);
        fragmentTransaction.commit();

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

                sessionStatusFragment.updateRace(raceMode);

                if(raceMode.getMode().equals("race")){
                    timings.addAll(JsonParseUtils.getRaceTimings(data));
                }else if(raceMode.getMode().equals("practice")){
                    timings.addAll(JsonParseUtils.getPracticeTimings(data));
                }

                adapter.notifyDataSetChanged();
            }
        };

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_session, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_session);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new OnlineSessionAdapter(timings, getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.online_session);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        getActivity().setTitle(PreferencesUtils.getNextGpCountry(getActivity()));

        loadOnlineSessionData();

        return view;
    }

    @Override
    public void onRefresh() {
        Intent intent = new Intent(BROADCAST_SERVICE_ACTION);
        getActivity().sendBroadcast(intent);

        swipeRefreshLayout.setRefreshing(false);
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
        getActivity().bindService(intent, sConn, BIND_AUTO_CREATE);
    }

    private void unregisterService(){
        getActivity().unregisterReceiver(receiver);

        if (!bound) return;
        getActivity().unbindService(sConn);
        bound = false;
    }
}
