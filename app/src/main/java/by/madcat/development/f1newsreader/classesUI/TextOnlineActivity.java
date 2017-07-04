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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.OnlinePostsLoadService;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.adapters.OnlinePostsAdapter;
import by.madcat.development.f1newsreader.dataInet.OnlinePost;

import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ARRAY;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ELEMENT_DATE;
import static by.madcat.development.f1newsreader.Utils.DocParseUtils.ONLINE_JSON_ELEMENT_MESSAGE;

public class TextOnlineActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public final static String BROADCAST_ACTION = "online_posts_receiver";
    public final static String BIND_FLAG = "online_posts_bind";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private OnlinePostsAdapter adapter;

    public LinkedList<OnlinePost> posts;
    BroadcastReceiver receiver;

    boolean bound = false;
    ServiceConnection sConn;

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

        recyclerView = (RecyclerView) findViewById(R.id.online_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                posts.clear();

                String data = intent.getStringExtra("online_posts_data");

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray(ONLINE_JSON_ARRAY);

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject currJsonObject = jsonArray.getJSONObject(i);
                        posts.add(
                                new OnlinePost(
                                        currJsonObject.getString(ONLINE_JSON_ELEMENT_MESSAGE),
                                        currJsonObject.getString(ONLINE_JSON_ELEMENT_DATE)
                                )
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
            }
        };

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(receiver, filter);

        setTitle(SystemUtils.getNextGpCountry(this));
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
        unbindService(sConn);
        Intent intent = new Intent(this, OnlinePostsLoadService.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);

        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadOnlinePostsData(){
        Intent intent = new Intent(this, OnlinePostsLoadService.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);
    }
}
