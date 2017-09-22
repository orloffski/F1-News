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
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.DateUtils;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.data.DatabaseDescription.News;
import by.madcat.development.f1newsreader.styling.CustomViews.RobotoMediumTextView;
import by.madcat.development.f1newsreader.styling.CustomViews.RobotoRegularTextView;

public class NewsPageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String NEWS_URI = "news_uri";

    private static final int LOADER = 0;

    private Uri newsUri;

    private RobotoMediumTextView title;
    private HtmlTextView htmlTextView;
    private ImageView imageView;
    private RobotoRegularTextView date;

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

        title = (RobotoMediumTextView) view.findViewById(R.id.content_title);
        htmlTextView = (HtmlTextView) view.findViewById(R.id.html_text_view);
        imageView = (ImageView) view.findViewById(R.id.content_image);
        imageView.setPadding(20, 0, 20, 0);
        date = (RobotoRegularTextView) view.findViewById(R.id.content_date);

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
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        if(data != null && data.moveToFirst()){
            int titleIndex = data.getColumnIndex(News.COLUMN_TITLE);
            int newsIndex = data.getColumnIndex(News.COLUMN_NEWS);
            int imageIndex = data.getColumnIndex(News.COLUMN_IMAGE);
            int dateIndex = data.getColumnIndex(News.COLUMN_DATE);
            final int linkIndex = data.getColumnIndex(News.COLUMN_LINK_NEWS);
            int typeIndex = data.getColumnIndex(News.COLUMN_NEWS_TYPE);

            title.setText(data.getString(titleIndex));

            htmlTextView.setHtmlText(data.getString(newsIndex));

            String pathToImage;
            if(!data.getString(imageIndex).isEmpty()) {
                pathToImage = SystemUtils.getImagesPath(getContext()) + "/" + data.getString(imageIndex);
            }else{
                pathToImage = "";
            }
            Glide.with(getContext()).load(pathToImage).asBitmap().placeholder(R.drawable.f1_logo).into(imageView);

            date.setText(DateUtils.untransformDateTime(data.getString(dateIndex)));

            ((NewsPageActivity)getActivity()).setNewsData(newsUri, data.getString(linkIndex), data.getString(typeIndex));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
