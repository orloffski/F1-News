package by.madcat.development.f1newsreader.dataInet;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import by.madcat.development.f1newsreader.Utils.DateUtils;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.Utils.StringUtils;
import by.madcat.development.f1newsreader.classesUI.NewsLoadSender;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class LoadNewsTask extends AsyncTask<Void, Void, ArrayList<String>> {

    public static final String IMAGE_PATH = "F1NewsImages";

    private ArrayList<String> dataLink;
    private Context context;
    private NewsLoadSender sender;

    public LoadNewsTask(ArrayList<String> dataLink, Context context, NewsLoadSender sender){
        this.dataLink = dataLink;
        this.context = context;
        this.sender = sender;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> newsData = new ArrayList<>();
        try {
            newsData = loadNewsData(dataLink.get(0), NewsTypes.valueOf(dataLink.get(1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsData;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);

        if(strings == null)
            return;

        ContentValues contentValues = new ContentValues();
        contentValues.put(News.COLUMN_TITLE, strings.get(0));
        contentValues.put(News.COLUMN_NEWS, strings.get(1));
        contentValues.put(News.COLUMN_NEWS_TYPE, strings.get(2));
        contentValues.put(News.COLUMN_LINK_NEWS, strings.get(3));
        contentValues.put(News.COLUMN_DATE, strings.get(4));
        contentValues.put(News.COLUMN_IMAGE, strings.get(5));
        contentValues.put(News.COLUMN_READ_FLAG, String.valueOf(0));

        context.getContentResolver().insert(News.CONTENT_URI, contentValues);
        sender.sendNewsLoadToAdapter();
    }

    public ArrayList<String> loadNewsData(String urlString, NewsTypes type) throws IOException {

        ArrayList<String> newsData = new ArrayList<>();

        org.jsoup.nodes.Document jsDoc = DocParseUtils.getJsDoc(urlString);

        // news title
        newsData.add(DocParseUtils.getNewsTitle(jsDoc));

        // news body
        newsData.add(DocParseUtils.getNewsBody(jsDoc));

        // news type
        newsData.add(type.toString());

        // news link
        newsData.add(urlString);

        // news date
        String dateTime = DateUtils.transformDateTime(DocParseUtils.getNewsDate(jsDoc));
        newsData.add(dateTime);

        // news image
        String image = "";
        if(!(image = DocParseUtils.getNewsImage(jsDoc)).equals("")) {
            loadNewsImage(image);
            newsData.add(StringUtils.getImageNameFromURL(image));
        }else
            newsData.add("");

        return newsData;
    }

    private void loadNewsImage(String imageUrl) throws IOException {
        File sdPath = context.getFilesDir();

        sdPath = new File(sdPath.getAbsolutePath() + "/" + IMAGE_PATH);

        if(!sdPath.exists())
            sdPath.mkdirs();

        String filename = StringUtils.getImageNameFromURL(imageUrl);
        File imageOnMemory = new File(sdPath, filename);

        Bitmap image = null;

        OutputStream fOut = new FileOutputStream(imageOnMemory);

        InputStream in = new URL(imageUrl).openStream();
        image = BitmapFactory.decodeStream(in);
        image.compress(Bitmap.CompressFormat.JPEG, 55, fOut);

        fOut.flush();
        fOut.close();
        in.close();
    }
}
