package by.madcat.development.f1newsreader.classesUI;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.Interfaces.NewsOpenListener;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Services.UILoadNewsService;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.adapters.NewsCardsAdapter;
import by.madcat.development.f1newsreader.adapters.NewsListAdapter;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;


public class NewsListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String NEWS_TYPE = "news_type";
    private static final int NEWS_LOADER = 0;
    public static final String SERVICE_DATA = "service_data";

    public final static String BROADCAST_ACTION = "by.madcat.development.f1newsreader";

    private NewsTypes type;
    private NewsOpenListener newsOpenListener;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    BroadcastReceiver receiver;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case NEWS_LOADER:
                String selection;
                String[] selectionArgs;

                if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("hide_read_news", false)){
                    selection = News.COLUMN_NEWS_TYPE + "=? and " + News.COLUMN_READ_FLAG + "=?";
                    selectionArgs = new String[]{String.valueOf(type), String.valueOf(0)};
                }else {
                    selection = News.COLUMN_NEWS_TYPE + "=?";
                    selectionArgs = new String[]{String.valueOf(type)};
                }

                return new CursorLoader(getActivity(), News.CONTENT_URI, null, selection, selectionArgs, News.COLUMN_DATE + " COLLATE NOCASE DESC");
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<String> newsLink = new ArrayList<>();

        if(data != null && data.moveToFirst()){
            int idIndex = data.getColumnIndex(News._ID);

            do {
                newsLink.add(data.getString(idIndex));
            }while(data.moveToNext());

            data.moveToFirst();
        }

        if(adapter instanceof NewsListAdapter)((NewsListAdapter)adapter).swapCursor(data);
        else if (adapter instanceof NewsCardsAdapter)((NewsCardsAdapter)adapter).swapCursor(data);

        newsOpenListener.setSectionItemsCount((data != null) ? data.getCount() : 0);
        newsOpenListener.setSectionNewsLinks(newsLink);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(adapter instanceof NewsListAdapter)((NewsListAdapter)adapter).swapCursor(null);
        else if (adapter instanceof NewsCardsAdapter)((NewsCardsAdapter)adapter).swapCursor(null);
    }

    public NewsListFragment() {
    }

    public static NewsListFragment newInstance(NewsTypes type) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(NEWS_TYPE, String.valueOf(type));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
        newsOpenListener = (NewsOpenListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        newsOpenListener = null;
        context.unregisterReceiver(receiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.type = NewsTypes.valueOf(getArguments().getString(NEWS_TYPE));
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                swipeRefreshLayout.setRefreshing(false);

                int data = intent.getIntExtra(SERVICE_DATA, 0);
                Toast.makeText(context, createLoadMessage(data), Toast.LENGTH_SHORT).show();
            }
        };

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        context.registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        String listStyle = PreferenceManager.getDefaultSharedPreferences(context).getString("list_news_view", "list");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreNews();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        if(listStyle.equals("list")) {
            adapter = new NewsListAdapter(new NewsListAdapter.ClickListener() {
                @Override
                public void onClick(int positionID) {
                    newsOpenListener.sectionItemOpen(type, positionID);
                }
            }, getActivity());
        }else{
            adapter = new NewsCardsAdapter(new NewsCardsAdapter.ClickListener(){
                @Override
                public void onClick(int positionID) {
                    newsOpenListener.sectionItemOpen(type, positionID);
                }
            }, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(NEWS_LOADER, null, this);
    }

    private String createLoadMessage(int count){
        String message;

        if(count == 0)
            message = getString(R.string.no_isset_news_to_load);
        else {
            message = context.getResources().getQuantityString(R.plurals.news_plurals, count, count);
        }

        return message;
    }

    public void updateNewsList(){
        getLoaderManager().restartLoader(NEWS_LOADER, null, this);
    }

    public NewsTypes getNewsType(){
        return this.type;
    }

    public void loadMoreNews(){
        if(!SystemUtils.isNetworkAvailableAndConnected(context)) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, getString(R.string.network_not_available), Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(context, UILoadNewsService.class);
            context.startService(intent);
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
