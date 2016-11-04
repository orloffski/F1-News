package by.madcat.development.f1newsreader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;


public class NewsPageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String SECTION_ID = "section_id";
    private static final String POSITION_ID = "position_id";

    private static final int LOADER = 0;

    private int sectionID;
    private int positionID;

    private Uri newsUri;

    private TextView title;
    private TextView text;
    private TextView link;
    private TextView image;

    public NewsPageFragment() {
    }

    public static NewsPageFragment newInstance(int sectionID, int positionID) {


        NewsPageFragment fragment = new NewsPageFragment();
        Bundle args = new Bundle();
        args.putInt(SECTION_ID, sectionID);
        args.putInt(POSITION_ID, positionID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionID = getArguments().getInt(SECTION_ID);
            positionID = getArguments().getInt(POSITION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_page, container, false);

        title = (TextView) view.findViewById(R.id.content_title);
        text = (TextView) view.findViewById(R.id.content_text);
        link = (TextView) view.findViewById(R.id.content_link);
        image = (TextView) view.findViewById(R.id.content_image);

        switch (sectionID){
            case R.id.nav_news:
                newsUri = News.buildNewsUri(positionID);
                break;
            case R.id.nav_memuar:
                newsUri = Memuar.buildMemuarUri(positionID);
                break;
        }

        getLoaderManager().initLoader(LOADER, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;

        switch (id){
            case LOADER:
                cursorLoader = new CursorLoader(getActivity(),
                        newsUri,
                        null,
                        null,
                        null,
                        null);
                break;
            default:
                cursorLoader = null;
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            int titleIndex = 0;
            int newsIndex = 0;
            int linkIndex = 0;
            int imageIndex = 0;

            switch (sectionID){
                case R.id.nav_news:
                    titleIndex = data.getColumnIndex(News.COLUMN_TITLE);
                    newsIndex = data.getColumnIndex(News.COLUMN_NEWS);
                    linkIndex = data.getColumnIndex(News.COLUMN_LINK_NEWS);
                    imageIndex = data.getColumnIndex(News.COLUMN_IMAGE);
                    break;
                case R.id.nav_memuar:
                    titleIndex = data.getColumnIndex(Memuar.COLUMN_TITLE);
                    newsIndex = data.getColumnIndex(Memuar.COLUMN_NEWS);
                    linkIndex = data.getColumnIndex(Memuar.COLUMN_LINK_NEWS);
                    imageIndex = data.getColumnIndex(Memuar.COLUMN_IMAGE);
                    break;
            }

            title.setText(data.getString(titleIndex));
            text.setText(data.getString(newsIndex));
            link.setText(data.getString(linkIndex));
            image.setText(data.getString(imageIndex));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
