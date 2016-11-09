package by.madcat.development.f1newsreader.classesUI;

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

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;


public class NewsPageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String NEWS_URI = "news_uri";

    private static final int LOADER = 0;

    private Uri newsUri;

    private TextView title;
    private TextView text;
    private TextView link;
    private TextView image;

    public NewsPageFragment() {
    }

    public static NewsPageFragment newInstance(Uri newsUri) {
        NewsPageFragment fragment = new NewsPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(NEWS_URI, newsUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsUri = getArguments().getParcelable(NEWS_URI);
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
            int titleIndex = data.getColumnIndex(News.COLUMN_TITLE);
            int newsIndex = data.getColumnIndex(News.COLUMN_NEWS);
            int linkIndex = data.getColumnIndex(News.COLUMN_LINK_NEWS);
            int imageIndex = data.getColumnIndex(News.COLUMN_IMAGE);

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
