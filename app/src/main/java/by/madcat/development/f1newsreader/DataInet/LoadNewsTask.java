package by.madcat.development.f1newsreader.DataInet;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import by.madcat.development.f1newsreader.Utils.DateUtils;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.Utils.StringUtils;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.DataSQLite.DatabaseDescription.News;
import by.madcat.development.f1newsreader.DataSQLite.DatabaseDescription.NewsTypes;

public class LoadNewsTask extends AsyncTask<Void, Void, Void> {

    private ArrayList<String> dataLink;
    private Context context;
    private ArrayList<String> bodyImages;
    private NewsLinkListToLoad linksList;

    private boolean newsLoaded = false;

    public LoadNewsTask(ArrayList<String> dataLink, Context context, NewsLinkListToLoad linksList){
        this.dataLink = dataLink;
        this.context = context;
        this.linksList = linksList;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<String> newsData;
        try {
            newsData = loadNewsData(dataLink.get(0), NewsTypes.valueOf(dataLink.get(1)), new Date(dataLink.get(2)));
            saveNewsData(newsData);

            if(bodyImages != null)
                for(String imageLink : bodyImages)
                    loadNewsImage(imageLink);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        linksList.removeLoadNewsTask(this);
    }

    public ArrayList<String> loadNewsData(String urlString, NewsTypes type, Date rssDate) throws IOException {

        ArrayList<String> newsData = new ArrayList<>();

        Document jsDoc = DocParseUtils.getJsDoc(urlString);

        // news title
        newsData.add(DocParseUtils.getNewsTitle(jsDoc));

        // news body
        newsData.add(DocParseUtils.getNewsBody(jsDoc));

        // news type
        newsData.add(type.toString());

        // news link
        newsData.add(urlString);

        // news date
        String dateTime = DateUtils.transformDateTime(DocParseUtils.getNewsDate(jsDoc, rssDate));
        newsData.add(dateTime);

        // news image
        String image = "";
        if(!(image = DocParseUtils.getNewsImage(jsDoc)).equals("")) {
            if(loadNewsImage(image))
                newsData.add(StringUtils.getImageNameFromURL(image));
            else
                newsData.add("");
        }else
            newsData.add("");

        // news body images
        bodyImages = DocParseUtils.getNewsBodyImageLinks(jsDoc);

        return newsData;
    }

    private void saveNewsData(ArrayList<String> strings){
        if(strings == null || strings.get(1).equals(""))
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
        this.newsLoaded = true;
    }

    private boolean loadNewsImage(String imageUrl) throws IOException {
        File sdPath = new File(SystemUtils.getImagesPath(context));

        if(!sdPath.exists())
            sdPath.mkdirs();

        String filename = StringUtils.getImageNameFromURL(imageUrl);
        File imageOnMemory = new File(sdPath, filename);

        Bitmap image = null;

        OutputStream fOut = new FileOutputStream(imageOnMemory);

        InputStream in = new URL(imageUrl).openStream();
        if(in != null) {
            image = BitmapFactory.decodeStream(in);
            if(image != null)
                image.compress(Bitmap.CompressFormat.JPEG, 55, fOut);
        }

        fOut.flush();
        fOut.close();
        in.close();

        if(image != null)
            return true;
        else
            return false;
    }
}
