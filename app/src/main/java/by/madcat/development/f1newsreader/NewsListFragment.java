package by.madcat.development.f1newsreader;


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

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;


public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public interface NewsOpenListener{
        public void sectionItemOpen(int sectionID, int positionID);
        public void setSectionItemsCount(int count);
    }


    public static final String SECTION_ID = "section_id";
    private static final int NEWS_LOADER = 0;
    private static final int MEMUAR_LOADER = 1;

    private int sectionID;
    private NewsOpenListener newsOpenListener;
    private NewsListAdapter adapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case NEWS_LOADER:
                return new CursorLoader(getActivity(), News.CONTENT_URI, null, null, null, News.COLUMN_DATE + " COLLATE NOCASE DESC");
            case MEMUAR_LOADER:
                return new CursorLoader(getActivity(), Memuar.CONTENT_URI, null, null, null, Memuar.COLUMN_DATE + " COLLATE NOCASE DESC");
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        newsOpenListener.setSectionItemsCount((data != null) ? data.getCount() : 0);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public NewsListFragment() {
    }

    public static NewsListFragment newInstance(int sectionID) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putInt(SECTION_ID, sectionID);
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
            this.sectionID = getArguments().getInt(SECTION_ID);
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
            public void onClick(int sectionID, int positionID) {
                newsOpenListener.sectionItemOpen(sectionID, positionID);
            }
        }, sectionID);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider(getContext()));
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        switch (sectionID){
            case R.id.nav_news:
                getLoaderManager().initLoader(NEWS_LOADER, null, this);
                break;
            case R.id.nav_memuar:
                getLoaderManager().initLoader(MEMUAR_LOADER, null, this);
                break;
        }
    }
}
