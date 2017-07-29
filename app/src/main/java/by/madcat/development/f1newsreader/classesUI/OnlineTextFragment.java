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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webianks.library.PopupBubble;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.OnlinePostsLoadService;
import by.madcat.development.f1newsreader.Utils.JsonParseUtils;
import by.madcat.development.f1newsreader.Utils.PreferencesUtils;
import by.madcat.development.f1newsreader.adapters.OnlinePostsAdapter;
import by.madcat.development.f1newsreader.dataInet.OnlinePost;

import static android.content.Context.BIND_AUTO_CREATE;
import static by.madcat.development.f1newsreader.Services.OnlinePostsLoadService.BROADCAST_ACTION_DATA;
import static by.madcat.development.f1newsreader.Services.OnlinePostsLoadService.BROADCAST_SERVICE_ACTION;

public class OnlineTextFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public final static String BROADCAST_ACTION = "online_posts_receiver";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private OnlinePostsAdapter adapter;

    public LinkedList<OnlinePost> posts;
    BroadcastReceiver receiver;

    private PopupBubble popupBubble;

    boolean bound = false;
    private ServiceConnection sConn;

    public static OnlineTextFragment newInstance() {
        return new OnlineTextFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        posts = new LinkedList<>();

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

                String data = intent.getStringExtra(BROADCAST_ACTION_DATA);

                if (data == null || data.equals(""))
                    return;

                if(posts.isEmpty()) {
                    // первоначальная загрузка данных трансляции
                    posts.clear();
                    posts.addAll(JsonParseUtils.getPostsFromJsonString(data));
                    adapter.notifyDataSetChanged();
                }else{
                    // добавление первого нового поста трансляции
                    if(JsonParseUtils.getPostFromJsonString(data, 0) != null) {
                        posts.add(0, JsonParseUtils.getPostFromJsonString(data, 0));
                        adapter.notifyItemInserted(0);
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_online, container, false);

        loadOnlinePostsData();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_online);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new OnlinePostsAdapter(posts);

        popupBubble = (PopupBubble) view.findViewById(R.id.popup_bubble);

        recyclerView = (RecyclerView) view.findViewById(R.id.online_posts);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(linearLayoutManager.findFirstVisibleItemPosition() != 0){
                    popupBubble.show();
                }else{
                    popupBubble.hide();
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        popupBubble.setRecyclerView(recyclerView);
        popupBubble.withAnimation(true);
        popupBubble.hide();

        popupBubble.setPopupBubbleListener(new PopupBubble.PopupBubbleClickListener() {
            @Override
            public void bubbleClicked(Context context) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        getActivity().setTitle(PreferencesUtils.getNextGpCountry(getActivity()));

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!bound) return;
        unregisterService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!bound) return;
        unregisterService();
    }

    @Override
    public void onRefresh() {
        // обновление текстовой трансляции
        Intent intent = new Intent(BROADCAST_SERVICE_ACTION);
        getActivity().sendBroadcast(intent);

        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadOnlinePostsData(){
        Intent intent = new Intent(getActivity(), OnlinePostsLoadService.class);
        getActivity().bindService(intent, sConn, BIND_AUTO_CREATE);
    }

    private void unregisterService(){
        getActivity().unregisterReceiver(receiver);

        if (!bound) return;
        getActivity().unbindService(sConn);
        bound = false;
    }
}
