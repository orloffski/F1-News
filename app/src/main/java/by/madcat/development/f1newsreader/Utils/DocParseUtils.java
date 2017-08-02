package by.madcat.development.f1newsreader.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.classesUI.VideoFragment;
import by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes;
import by.madcat.development.f1newsreader.dataInet.Models.OnlinePost;

public final class DocParseUtils {
    public static final String DOCUMENT_ENCODING = "UTF-8";
    public static final String LINK_ITEM = "item";
    public static final String LINK_TAG = "guid";
    public static final String LINK_DATE = "pubDate";
    public static final String NEWS_TITLE_PARSE = "post_title";
    public static final String NEWS_BODY_PARSE = "post_content";
    public static final String NEWS_BODY_TEXT_ELEMENTS_PARSE = "p";
    public static final String NEWS_BODY_TEXT_ELEMENTS_PARSE_2 = "blockquote";
    public static final String NEWS_BODY_H3_ELEMENTS_PARSE = "h3";
    public static final String NEWS_BODY_TABLE_ELEMENTS_PARSE = "table";
    public static final String NEWS_BODY_TABLE_TBODY_PARSE = "tbody";
    public static final String NEWS_BODY_ROOT_ELEMENT = "body";
    public static final String NEWS_BODY_BR_ELEMENT = "br";
    public static final String NEWS_BODY_IMG_ELEMENT = "img";
    public static final String NEWS_DATE_PARSE = "post_date";
    public static final String NEWS_IMAGE_DIV_PARSE = "post_thumbnail";
    public static final String NEWS_IMAGE_TAG_PARSE = "img";
    public static final String NEWS_IMAGE_LINK_ATTR_PARSE = "src";

    public static final String NEXT_GP_TITLE = "stream_title";
    public static final String NEXT_GP_DATE = "stream_date";
    public static final String NEXT_GP_TIMESTAMP = "stream_countdown";
    public static final String NEXT_GP_TIMESTAMP_ATTR = "data-timestamp";

    public static final String NEXT_WEEKEND_GP_HEAD_STREAM = "widget stream widget_danger";
    public static final String NEXT_WEEKEND_GP_IMAGE_STREAM = "stream_wrap";
    public static final String NEXT_WEEKEND_GP_TITLE_STREAM = "widget_title";
    public static final String NEXT_WEEKEND_GP_BODY_STREAM = "list_featured";
    public static final String NEXT_WEEKEND_GP_BIMAGE_LINK_ID = "style";
    public static final String WORDS_TO_CLEAR_1 = "Online ";
    public static final String WORDS_TO_CLEAR_2 = "Стартовое поле ";
    public static final String WORDS_TO_CLEAR_3 = "background: url(";
    public static final String WORDS_TO_CLEAR_4 = ");";

    public static final String VIDEO_ONLINE_CONTAINER = "video";

    public static final String ONLINE_JSON_RACE_MODE = "mode";
    public static final String ONLINE_JSON_RACE_FLAG = "flag";
    public static final String ONLINE_JSON_RACE_SAFETY_CAR = "safety_car";
    public static final String ONLINE_JSON_RACE_CURRENT_LAP = "current_lap";
    public static final String ONLINE_JSON_RACE_TOTAL_LAPS = "total_laps";
    public static final String ONLINE_JSON_RACE_TRACK_TEMP = "tracktemp";
    public static final String ONLINE_JSON_RACE_AIR_TEMP = "airtemp";

    public static final String ONLINE_JSON_ARRAY = "messages";
    public static final String ONLINE_JSON_ELEMENT_DATE = "tm";
    public static final String ONLINE_JSON_ELEMENT_MESSAGE = "msg";
    public static final String ONLINE_JSON_SESSION_ID = "session_id";
    public static final String ONLINE_JSON_ARRAY_RACE = "drivers";
    public static final String ONLINE_JSON_ARRAY_RACE_NAME = "name";
    public static final String ONLINE_JSON_ARRAY_RACE_POSITION = "position";
    public static final String ONLINE_JSON_ARRAY_RACE_GAP = "gap";
    public static final String ONLINE_JSON_ARRAY_RACE_INTERVAL = "interval";
    public static final String ONLINE_JSON_ARRAY_RACE_PITS = "pits";
    public static final String ONLINE_JSON_ARRAY_RACE_LASTLAP = "last_lap";
    public static final String ONLINE_JSON_ARRAY_SESSION_BESTLAP = "best_lap";

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

        }

        return images_array;
    }

    public static String getNewsDate(Document jsDoc, Date rssDate){
        String dateTime = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm");

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

            Date date = new Date(item.getElementsByTag(LINK_DATE).first().text());

            NewsTypes type = StringUtils.getNewsSection(link);
            if(type != null) {
                Map<NewsTypes, Date> tmp = new HashMap<>();
                tmp.put(type, date);
                links.put(link, tmp);
            }
        }

        return links;
    }

    public static ArrayList<View> getViews(String htmlText, Context context, FragmentManager fragmentManager){
        ArrayList<View> views = new ArrayList<>();


        Document jsDoc = Jsoup.parse(htmlText.toString(), DOCUMENT_ENCODING);
        Element body = jsDoc.getElementsByTag(NEWS_BODY_ROOT_ELEMENT).first();
        for(Element child : body.children()){

            if(child.tagName().equals(NEWS_BODY_TABLE_ELEMENTS_PARSE)){
                View table = createView(child.toString(), "", "TableView", context, fragmentManager);
                views.add(table);
            }

            if(child.tagName().equals(NEWS_BODY_H3_ELEMENTS_PARSE)){
                View table = createView(child.text(), NEWS_BODY_H3_ELEMENTS_PARSE, "TextView", context, fragmentManager);
                views.add(table);
            }

            if(child.tagName().equals(NEWS_BODY_TEXT_ELEMENTS_PARSE) || child.tagName().equals(NEWS_BODY_TEXT_ELEMENTS_PARSE_2)) {
                String modifiedText = "";
                String modifierTag = "";
                int lengthOfChildrens = 0;

                if (child.children().size() != 0) {
                    for (Element children : child.children()) {
                        modifiedText = children.text();
                        modifierTag = children.tagName();

                        // пока все ссылки выброшены
                        if (modifierTag.equals("a"))
                            break;

                        if(modifierTag.equals("img")) {
                            String image = StringUtils.getImageNameFromURL(children.attr(NEWS_IMAGE_LINK_ATTR_PARSE));
                            View bodyImage = createView(image, modifierTag, "ImageView", context, fragmentManager);
                            views.add(bodyImage);
                        }

                        if(modifierTag.equals("iframe")){
                            String videoLink = StringUtils.getVideoIdFromURL(children.attr(NEWS_IMAGE_LINK_ATTR_PARSE));
                            View bodyVideo = createView(videoLink, modifierTag, "VideoView", context, fragmentManager);
                            views.add(bodyVideo);
                        }

                        if(modifierTag.equals("twitter-video twitter-video-error")){
                            View twitterView = createView(modifiedText, modifierTag, "TwitterView", context, fragmentManager);
                            views.add(twitterView);
                        }

                        // при повторении нескольких модификаторов между парой кусков текста есть пробел
                        if (lengthOfChildrens != 0)
                            lengthOfChildrens += 1;

                        // переходы на новую строку в блоке <p> и пустые абзацы не обрабатываются
                        if (!modifierTag.equals(NEWS_BODY_BR_ELEMENT) && modifiedText.length() != 0) {
                            lengthOfChildrens += modifiedText.length();
                            View headerText = createView(modifiedText.trim(), modifierTag, "TextView", context, fragmentManager);
                            views.add(headerText);
                        }
                    }
                }

                // пустые абзацы выбрасываем
                if (child.text().length() != 0) {
                    View text = createView(child.text().toString().substring(lengthOfChildrens).trim(),
                            "", "TextView", context, fragmentManager);
                    views.add(text);
                }
            }
        }

        // если новость была загружена до внедрения CustomView у нее нет тегов, грузим без оформления
        if(views.size() == 0){
            View text = createView(htmlText, "", "TextView", context, fragmentManager);
            views.add(text);
        }

        return views;
    }

    public static String getNextGpTitle(Document jsDoc){
        return jsDoc.getElementsByClass(NEXT_GP_TITLE).text();
    }

    public static String getNextGpDate(Document jsDoc){
        return jsDoc.getElementsByClass(NEXT_GP_DATE).text();
    }

    public static String getNextGpTimestamp(Document jsDoc){
        String timestamp = "";

        Elements next_gp_timestamps = jsDoc.getElementsByClass(NEXT_GP_TIMESTAMP);
        if(next_gp_timestamps.first() != null) {
            timestamp = next_gp_timestamps.first().attr(NEXT_GP_TIMESTAMP_ATTR);
        }

        return timestamp;
    }

    private static View createView(final String text, final String modifier, String viewType, final Context context, FragmentManager fragmentManager){
        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        View view = null;

        switch (viewType){
            case "TextView":
                view = new TextView(context);
                ((TextView)view).setText(text);
                view.setLayoutParams(textViewLayoutParams);
                view.setPadding(0, 5, 0, 0);
                break;
            case "TableView":
                view = new TableLayout(context);
                view.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

                view = completeTheTable(view, text, context);
                view.setPadding(0, 10, 0, 0);
                break;
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
            case "VideoView":
                view = new LinearLayout(context);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                view.setId(1);
                view.setPadding(0,10,0,0);

                fragmentManager.beginTransaction().add(1, VideoFragment.newInstance(text)).commitAllowingStateLoss();
                break;
            case "TwitterView":
                view = new WebView(context);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                ((WebView)view).getSettings().setJavaScriptEnabled(true);
                ((WebView)view).loadData(text, "text/html; charset=utf-8", "UTF-8");
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

    private static View completeTheTable(View tableView, String table, Context context){
        Document jsDoc = Jsoup.parse(table.toString(), DOCUMENT_ENCODING);
        Element body = jsDoc.getElementsByTag(NEWS_BODY_TABLE_TBODY_PARSE).first();
        String modifier = "";
        int counter = 1;

        for(Element childRows: body.children()){
            counter++;

            if(childRows.attr("class").equals("firstLine")) {
                modifier = "b";
            }else{
                modifier = "";
            }

            TableRow row = new TableRow(context);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            row.setMinimumHeight(20);

            for(Element childColumns : childRows.children()){
                View columnText = new TextView(context);
                ((TextView)columnText).setText(childColumns.text());
                ((TextView)columnText).setTextSize(11);
                if(modifier.equals("b"))
                    ((TextView)columnText).setTypeface(null, Typeface.BOLD);

                columnText.setPadding(0,0,15,0);

                row.addView(columnText);
            }

            if(counter%2 == 0)
                row.setBackgroundColor(Color.rgb(233,233,233));

            ((TableLayout)tableView).addView(row);
        }

        return tableView;
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

    public static Map<String, String> getWeekendData(Element weekendTable){
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

    public static String getVideoOnlineContainer(Document jsDoc){
        try {
            Element online_container = jsDoc.getElementsByClass(VIDEO_ONLINE_CONTAINER).first();

            return online_container.toString();
        }catch (Exception e){
            return "";
        }
    }
}
