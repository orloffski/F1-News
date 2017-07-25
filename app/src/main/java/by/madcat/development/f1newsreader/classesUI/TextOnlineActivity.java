package by.madcat.development.f1newsreader.classesUI;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.webianks.library.PopupBubble;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.OnlinePostsLoadService;
import by.madcat.development.f1newsreader.Utils.JsonParseUtils;
import by.madcat.development.f1newsreader.Utils.PreferencesUtils;
import by.madcat.development.f1newsreader.adapters.OnlinePostsAdapter;
import by.madcat.development.f1newsreader.dataInet.OnlinePost;

import static by.madcat.development.f1newsreader.Services.OnlinePostsLoadService.BROADCAST_ACTION_DATA;
import static by.madcat.development.f1newsreader.Services.OnlinePostsLoadService.BROADCAST_SERVICE_ACTION;

public class TextOnlineActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public final static String BROADCAST_ACTION = "online_posts_receiver";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private OnlinePostsAdapter adapter;

    public LinkedList<OnlinePost> posts;
    BroadcastReceiver receiver;

    private PopupBubble popupBubble;

    boolean bound = false;
    private ServiceConnection sConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        posts = new LinkedList<>();

        setContentView(R.layout.activity_text_online);

        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };

        loadOnlinePostsData();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_online);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new OnlinePostsAdapter(posts);

        popupBubble = (PopupBubble) findViewById(R.id.popup_bubble);

        recyclerView = (RecyclerView) findViewById(R.id.online_posts);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        popupBubble.setRecyclerView(recyclerView);
        popupBubble.withAnimation(true);
        popupBubble.hide();

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

                        if(PreferencesUtils.getAutoscrollingFlag(getApplicationContext()))
                            recyclerView.smoothScrollToPosition(0);
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(receiver, filter);

        setTitle(PreferencesUtils.getNextGpCountry(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.online_text_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.autoscroll_top).setChecked(PreferencesUtils.getAutoscrollingFlag(getApplicationContext()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.autoscroll_top:
                if(item.isChecked()){
                    PreferencesUtils.disableAutoScrolling(getApplicationContext());
                    item.setChecked(false);
                }else{
                    PreferencesUtils.enableAutoScrolling(getApplicationContext());
                    item.setChecked(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);

        if (!bound) return;
        unbindService(sConn);
        bound = false;
    }

    @Override
    public void onRefresh() {
        // обновление текстовой трансляции
        Intent intent = new Intent(BROADCAST_SERVICE_ACTION);
        sendBroadcast(intent);

        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadOnlinePostsData(){
        Intent intent = new Intent(this, OnlinePostsLoadService.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);
    }
}
