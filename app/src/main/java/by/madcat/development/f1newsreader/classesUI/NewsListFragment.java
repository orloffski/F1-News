package by.madcat.development.f1newsreader.classesUI;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;


public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public interface NewsOpenListener{
        public void sectionItemOpen(NewsTypes type, int positionID);
        public void setSectionItemsCount(int count);
        public void setSectionNewsLinks(ArrayList<String> links);
    }


    public static final String NEWS_TYPE = "news_type";
    private static final int NEWS_LOADER = 0;

    private NewsTypes type;
    private NewsOpenListener newsOpenListener;
    private NewsListAdapter adapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case NEWS_LOADER:
                String selection = News.COLUMN_NEWS_TYPE + "=?";
                String[] selectionArgs = new String[]{};
                switch (type){
                    case NEWS:
                        selectionArgs = new String[] {String.valueOf(NewsTypes.NEWS)};
                        break;
                    case MEMUAR:
                        selectionArgs = new String[] {String.valueOf(NewsTypes.MEMUAR)};
                        break;
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
        adapter.swapCursor(data);
        newsOpenListener.setSectionItemsCount((data != null) ? data.getCount() : 0);
        newsOpenListener.setSectionNewsLinks(newsLink);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
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
        newsOpenListener = (NewsOpenListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        newsOpenListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.type = NewsTypes.valueOf(getArguments().getString(NEWS_TYPE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        adapter = new NewsListAdapter(new NewsListAdapter.ClickListener() {
            @Override
            public void onClick(int positionID) {
                newsOpenListener.sectionItemOpen(type, positionID);
            }
        }, getActivity());

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(NEWS_LOADER, null, this);
    }
}
