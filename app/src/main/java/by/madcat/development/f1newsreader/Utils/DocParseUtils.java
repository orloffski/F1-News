package by.madcat.development.f1newsreader.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes;
import by.madcat.development.f1newsreader.dataInet.Models.OnlinePost;

public final class DocParseUtils {
    private static final String DOCUMENT_ENCODING = "UTF-8";
    private static final String LINK_ITEM = "item";
    private static final String LINK_TAG = "guid";
    private static final String LINK_DATE = "pubDate";
    private static final String NEWS_TITLE_PARSE = "post_title";
    private static final String NEWS_BODY_PARSE = "post_content";
    private static final String NEWS_BODY_TEXT_ELEMENTS_PARSE = "p";
    private static final String NEWS_BODY_TEXT_ELEMENTS_PARSE_2 = "blockquote";
    private static final String NEWS_BODY_BR_ELEMENT = "br";
    private static final String NEWS_BODY_H3_ELEMENTS_PARSE = "h3";
    private static final String NEWS_BODY_TABLE_ELEMENTS_PARSE = "table";
    private static final String NEWS_BODY_ROOT_ELEMENT = "body";
    private static final String NEWS_BODY_IMG_ELEMENT = "img";
    private static final String NEWS_DATE_PARSE = "post_date";
    private static final String NEWS_IMAGE_DIV_PARSE = "post_thumbnail";
    private static final String NEWS_IMAGE_TAG_PARSE = "img";
    private static final String NEWS_IMAGE_LINK_ATTR_PARSE = "src";

    private static final String NEXT_GP_TITLE = "stream_title";
    private static final String NEXT_GP_DATE = "stream_date";
    private static final String NEXT_GP_TIMESTAMP = "stream_countdown";
    private static final String NEXT_GP_TIMESTAMP_ATTR = "data-timestamp";

    private static final String NEXT_WEEKEND_GP_HEAD_STREAM = "widget stream widget_danger";
    private static final String NEXT_WEEKEND_GP_TITLE_STREAM = "widget_title";
    private static final String NEXT_WEEKEND_GP_BODY_STREAM = "list_featured";
    private static final String WORDS_TO_CLEAR_1 = "Online ";
    private static final String WORDS_TO_CLEAR_2 = "Стартовое поле ";

    static final String ONLINE_JSON_RACE_MODE = "mode";
    static final String ONLINE_JSON_RACE_FLAG = "flag";
    static final String ONLINE_JSON_RACE_SAFETY_CAR = "safety_car";
    static final String ONLINE_JSON_RACE_CURRENT_LAP = "current_lap";
    static final String ONLINE_JSON_RACE_TOTAL_LAPS = "total_laps";
    static final String ONLINE_JSON_RACE_TRACK_TEMP = "tracktemp";
    static final String ONLINE_JSON_RACE_AIR_TEMP = "airtemp";

    static final String ONLINE_JSON_ARRAY = "messages";
    static final String ONLINE_JSON_ELEMENT_DATE = "tm";
    static final String ONLINE_JSON_ELEMENT_MESSAGE = "msg";
    static final String ONLINE_JSON_SESSION_ID = "session_id";
    static final String ONLINE_JSON_ARRAY_RACE = "drivers";
    static final String ONLINE_JSON_ARRAY_RACE_NAME = "name";
    static final String ONLINE_JSON_ARRAY_RACE_POSITION = "position";
    static final String ONLINE_JSON_ARRAY_RACE_GAP = "gap";
    static final String ONLINE_JSON_ARRAY_RACE_INTERVAL = "interval";
    static final String ONLINE_JSON_ARRAY_RACE_PITS = "pits";
    static final String ONLINE_JSON_ARRAY_RACE_LASTLAP = "last_lap";
    static final String ONLINE_JSON_ARRAY_SESSION_BESTLAP = "best_lap";

    public static String getJsonString(String urlString) throws IOException {
        String line;
        StringBuilder doc = new StringBuilder();

        URL url = new URL(urlString);
        InputStreamReader isReader = new InputStreamReader(url.openStream());
        BufferedReader reader = new BufferedReader(isReader);

        while((line = reader.readLine()) != null)
            doc.append(line);

        return doc.toString();
    }

    public static org.jsoup.nodes.Document getJsDoc(String urlString) throws IOException {
        String line;
        StringBuilder doc = new StringBuilder();

        URL url = new URL(urlString);
        InputStreamReader isReader = new InputStreamReader(url.openStream());
        BufferedReader reader = new BufferedReader(isReader);

        while((line = reader.readLine()) != null)
            doc.append(line);

        org.jsoup.nodes.Document jsDoc = Jsoup.parse(doc.toString(), DOCUMENT_ENCODING);

        reader.close();
        isReader.close();

        return jsDoc;
    }

    public static String getNewsTitle(Document jsDoc){
        return jsDoc.getElementsByClass(NEWS_TITLE_PARSE).text();
    }

    public static String getNewsBody(Document jsDoc){
        try {
            StringBuilder newsBody = new StringBuilder();
            Element news_content = jsDoc.getElementsByClass(NEWS_BODY_PARSE).first();
            for(Element element : news_content.children())
                newsBody.append(element.toString());
            return newsBody.toString();
        }catch (Exception e){
            return "";
        }
    }

    public static ArrayList<String> getNewsBodyImageLinks(Document jsDoc){
        ArrayList<String> images_array = new ArrayList<>();

        try {
            Element news_content = jsDoc.getElementsByClass(NEWS_BODY_PARSE).first();
            Elements news_body_images = news_content.getElementsByTag(NEWS_BODY_IMG_ELEMENT);

            if(news_body_images.size() == 0)
                return null;

            for(Element imageLink : news_body_images){
                String link = imageLink.attr(NEWS_IMAGE_LINK_ATTR_PARSE);
                if(!link.contains("http"))
                    link = "http:" + link;

                images_array.add(link);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return images_array;
    }

    public static String getNewsDate(Document jsDoc, Date rssDate){
        String dateTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault());

        Elements news_date = jsDoc.getElementsByClass(NEWS_DATE_PARSE);
        if(news_date.isEmpty()){
            dateTime = simpleDateFormat.format(new Date());
        }else{
            dateTime = news_date.text();
        }

        try {
            Date dateFromNews = simpleDateFormat.parse(dateTime);
            dateTime = simpleDateFormat.format(DateUtils.getEarlyDate(dateFromNews, rssDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static String getNewsImage(Document jsDoc){
        String image = "";

        Elements news_image_div = jsDoc.getElementsByClass(NEWS_IMAGE_DIV_PARSE);
        if(news_image_div.first() != null) {
            Elements linkDivs = news_image_div.first().getElementsByTag(NEWS_IMAGE_TAG_PARSE);
            image = linkDivs.first().attr(NEWS_IMAGE_LINK_ATTR_PARSE);
        }

        return image;
    }

    public static Map<String, Map<NewsTypes, Date>> getNewsLinkList(Document jsDoc){
        Map<String, Map<NewsTypes, Date>> links = new HashMap<>();

        for(Element item : jsDoc.getElementsByTag(LINK_ITEM)){
            String link = item.getElementsByTag(LINK_TAG).first().text();
            if(!link.contains("https"))
                link = link.replace("http", "https");

            Date date = null;
            try {
                date = DateFormat.getInstance().parse(item.getElementsByTag(LINK_DATE).first().text());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            NewsTypes type = StringUtils.getNewsSection(link);
            if(type != null) {
                Map<NewsTypes, Date> tmp = new HashMap<>();
                tmp.put(type, date);
                links.put(link, tmp);
            }
        }

        return links;
    }

    public static ArrayList<View> getViews(String htmlText, Context context){
        ArrayList<View> views = new ArrayList<>();


        Document jsDoc = Jsoup.parse(htmlText, DOCUMENT_ENCODING);
        Element body = jsDoc.getElementsByTag(NEWS_BODY_ROOT_ELEMENT).first();

        StringBuilder newsBodyTmp = new StringBuilder();
        for(Element child : body.children()){

            if(child.tagName().equals(NEWS_BODY_TABLE_ELEMENTS_PARSE)){
                newsBodyTmp.append(child.toString());
                continue;
            }

            if(child.tagName().equals(NEWS_BODY_H3_ELEMENTS_PARSE)){
                newsBodyTmp.append(child.toString());
                continue;
            }

            if(child.tagName().equals(NEWS_BODY_TEXT_ELEMENTS_PARSE) ||
                    child.tagName().equals(NEWS_BODY_TEXT_ELEMENTS_PARSE_2)) {
                String modifierTag;

                if (child.children().size() != 0) {
                    for (Element children : child.children()) {
                        modifierTag = children.tagName();

                        if(modifierTag.equals("img")) {
                            if(newsBodyTmp.length() != 0){
                                views.add(createWebView(context, newsBodyTmp.toString()));
                                newsBodyTmp.setLength(0);
                            }

                            String image = StringUtils.getImageNameFromURL(children.attr(NEWS_IMAGE_LINK_ATTR_PARSE));
                            View bodyImage = createView(image, modifierTag, "ImageView", context);
                            views.add(bodyImage);
                        }

                        if(modifierTag.equals("iframe")){
                            newsBodyTmp.append(children.toString());
                        }

                        if(modifierTag.equals("twitter-video twitter-video-error")){
                            newsBodyTmp.append(children.toString());
                        }
                    }
                }

                if(child.text().length() != 0){
                    newsBodyTmp.append(child.toString());
                }
            }
        }

        if(newsBodyTmp.length() != 0){
            views.add(createWebView(context, newsBodyTmp.toString()));
            newsBodyTmp.setLength(0);
        }

        // если новость была загружена до внедрения CustomView у нее нет тегов, грузим без оформления
        if(views.size() == 0){
            WebView web = createWebView(context, htmlText);
            views.add(web);
        }

        return views;
    }

    private static WebView createWebView(Context context, String htmlText){
        final WebView web = new WebView(context);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebClient());
        web.loadData(htmlText, null, null);
        return web;
    }

    private static String getNextGpTitle(Document jsDoc){
        return jsDoc.getElementsByClass(NEXT_GP_TITLE).text();
    }

    private static String getNextGpDate(Document jsDoc){
        return jsDoc.getElementsByClass(NEXT_GP_DATE).text();
    }

    private static String getNextGpTimestamp(Document jsDoc){
        String timestamp = "";

        Elements next_gp_timestamps = jsDoc.getElementsByClass(NEXT_GP_TIMESTAMP);
        if(next_gp_timestamps.first() != null) {
            timestamp = next_gp_timestamps.first().attr(NEXT_GP_TIMESTAMP_ATTR);
        }

        return timestamp;
    }

    private static View createView(final String text, final String modifier, String viewType, final Context context){
        View view = null;

        switch (viewType){
            case "ImageView":
                final String pathToImage = SystemUtils.getImagesPath(context) + "/" + text;

                Bitmap image = BitmapFactory.decodeFile(pathToImage);
                if(image == null)
                    image = BitmapFactory.decodeResource(context.getResources() ,R.drawable.f1_logo);

                int imageHeight = SystemUtils.getResizedImageHeight(image);

                view = new ImageView(context);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        imageHeight));

                Glide.with(context).load(pathToImage).asBitmap().placeholder(R.drawable.f1_logo).into((ImageView) view);

                view.setPadding(0, 10, 0, 0);
                break;
        }

        switch (modifier){
            case "b": case "span":  // на портале стиль жирного текста ради стиля
                ((TextView)view).setTypeface(null, Typeface.BOLD);
                break;
            case "i":
                ((TextView)view).setTypeface(null, Typeface.ITALIC);
                break;
            case "h3":
                ((TextView)view).setAllCaps(true);
                ((TextView)view).setTypeface(null, Typeface.BOLD);
                break;
        }

        return view;
    }

    public static void loadTimersData(String urlString, Context context) throws IOException {
        org.jsoup.nodes.Document jsDoc = DocParseUtils.getJsDoc(urlString);

        PreferencesUtils.saveTimersData(
                getNextGpTitle(jsDoc),
                getNextGpDate(jsDoc),
                Integer.parseInt(getNextGpTimestamp(jsDoc)),
                context);
    }

    public static void loadNextGranPrixWeekend(String urlString, Context context) throws IOException{
        org.jsoup.nodes.Document jsDoc = DocParseUtils.getJsDoc(urlString);

        Element weekend = jsDoc.getElementsByClass(NEXT_WEEKEND_GP_HEAD_STREAM).first();
        String weekendTitle = weekend.getElementsByClass(NEXT_WEEKEND_GP_TITLE_STREAM).text();

        String weekendTrackMap = "";
        Elements links = jsDoc.getElementsByTag("a");
        for(Element link : links){
            if(link.text().equals("Трасса и статистика")){
                Document subDoc = DocParseUtils.getJsDoc(link.attr("href"));
                Element weekendTrack = subDoc.getElementsByAttributeValueContaining("src", "-track.png").first();
                if(weekendTrack == null)
                    weekendTrack = subDoc.getElementsByAttributeValueContaining("src", "_track.png").first();
                weekendTrackMap = StringUtils.getImageNameFromURL(weekendTrack.attr("src"));
            }

        }

        Element weekendTable = weekend.getElementsByClass(NEXT_WEEKEND_GP_BODY_STREAM).first();

        PreferencesUtils.saveWeekendData(weekendTitle, weekendTrackMap, getWeekendData(weekendTable), context);
    }

    private static Map<String, String> getWeekendData(Element weekendTable){
        Map<String, String> weekendData = new LinkedHashMap<>();

        Elements theads = weekendTable.getElementsByTag("thead");
        Elements tbodies = weekendTable.getElementsByTag("tbody");

        ArrayList<String> headList = new ArrayList<>();
        ArrayList<String> bodyList = new ArrayList<>();

        for(Element head : theads)
            if(!head.text().equals(""))
                headList.add(head.text());

        for(Element body : tbodies) {
            if(!body.equals(tbodies.first())) {
                bodyList.add(StringUtils.addNewLineCharInLongLine(
                        body.text().replace(WORDS_TO_CLEAR_1, "").replace(WORDS_TO_CLEAR_2, "")));
            }
        }

        for(int i = 0; i < headList.size(); i++)
            weekendData.put(headList.get(i), bodyList.get(i));

        return weekendData;
    }

    public static LinkedList<OnlinePost> getOnlinePosts(Document jsDoc){
        LinkedList<OnlinePost> posts = new LinkedList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsDoc.text());
            JSONArray jsonArray = jsonObject.getJSONArray(ONLINE_JSON_ARRAY);

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject currJsonObject = jsonArray.getJSONObject(i);
                posts.add(new OnlinePost(currJsonObject.getString(ONLINE_JSON_ELEMENT_MESSAGE), currJsonObject.getString(ONLINE_JSON_ELEMENT_DATE)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return posts;
    }
}
