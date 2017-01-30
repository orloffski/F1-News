package by.madcat.development.f1newsreader.classesUI;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;

import java.io.File;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.DateUtils;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;
import by.madcat.development.f1newsreader.dataInet.LoadNewsTask;


public class NewsPageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String NEWS_URI = "news_uri";

    private static final int LOADER = 0;

    private Uri newsUri;

    private TextView title;
    private HtmlTextView htmlTextView;
    private ImageView image;
    private TextView date;
    private ImageButton shareBtn;

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
        htmlTextView = (HtmlTextView) view.findViewById(R.id.html_text_view);
        image = (ImageView) view.findViewById(R.id.content_image);
        date = (TextView) view.findViewById(R.id.content_date);
        shareBtn = (ImageButton) view.findViewById(R.id.shareBtn);

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

            title.setText(data.getString(titleIndex));
            htmlTextView.setHtmlText(data.getString(newsIndex));

            if(!data.getString(imageIndex).isEmpty()) {
                String pathToImage = getActivity().getFilesDir() + "/" + LoadNewsTask.IMAGE_PATH + "/" + data.getString(imageIndex);
                File imageFile = new File(pathToImage);
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                image.setImageBitmap(bitmap);
            }else{
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.logo);
                image.setImageBitmap(bitmap);
            }

            date.setText(DateUtils.untransformDateTime(data.getString(dateIndex)));
            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, data.getString(linkIndex));
                    startActivity(Intent.createChooser(shareIntent, "Share news"));
                }
            });

            ((NewsPageActivity)getActivity()).setNewsData(newsUri, data.getString(titleIndex));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
