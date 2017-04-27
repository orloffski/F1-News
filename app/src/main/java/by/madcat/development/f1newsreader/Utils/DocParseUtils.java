package by.madcat.development.f1newsreader.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

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
import java.util.Map;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes;
import by.madcat.development.f1newsreader.dataInet.LoadNewsTask;

public final class DocParseUtils {
    public static final String DOCUMENT_ENCODING = "UTF-8";
    public static final String LINK_ITEM = "item";
    public static final String LINK_TAG = "guid";
    public static final String LINK_DATE = "pubDate";
    public static final String NEWS_TITLE_PARSE = "post_title";
    public static final String NEWS_BODY_PARSE = "post_content";
    public static final String NEWS_BODY_TEXT_ELEMENTS_PARSE = "p";
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
    public static final String NEXT_WEEKEND_GP_TITLE_STREAM = "widget_title";
    public static final String NEXT_WEEKEND_GP_BODY_STREAM = "list_featured";
    public static final String WORDS_TO_CLEAR_1 = "Online ";
    public static final String WORDS_TO_CLEAR_2 = "Стартовое поле ";


    public static org.jsoup.nodes.Document getJsDoc(String urlString) throws IOException {
        String line;
        StringBuilder doc = new StringBuilder();

        URL url = new URL(urlString);
        InputStreamReader isReader = new InputStreamReader(url.openConnection().getInputStream());
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
        StringBuilder newsBody = new StringBuilder();
        Element news_content = jsDoc.getElementsByClass(NEWS_BODY_PARSE).first();
        for(Element element : news_content.children())
            newsBody.append(element.toString());
        return newsBody.toString();
    }

    public static ArrayList<String> getNewsBodyImageLinks(Document jsDoc){
        Element news_content = jsDoc.getElementsByClass(NEWS_BODY_PARSE).first();
        Elements news_body_images = news_content.getElementsByTag(NEWS_BODY_IMG_ELEMENT);

        if(news_body_images.size() == 0)
            return null;

        ArrayList<String> images_array = new ArrayList<>();

        for(Element imageLink : news_body_images){
            String link = imageLink.attr(NEWS_IMAGE_LINK_ATTR_PARSE);
            if(!link.contains("http"))
                link = "http:" + link;

            images_array.add(link);
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

    public static ArrayList<View> getViews(String htmlText, Context context){
        ArrayList<View> views = new ArrayList<>();


        Document jsDoc = Jsoup.parse(htmlText.toString(), DOCUMENT_ENCODING);
        Element body = jsDoc.getElementsByTag(NEWS_BODY_ROOT_ELEMENT).first();
        for(Element child : body.children()){

            if(child.tagName().equals(NEWS_BODY_TABLE_ELEMENTS_PARSE)){
                View table = createView(child.toString(), "", "TableView", context);
                views.add(table);
            }

            if(child.tagName().equals(NEWS_BODY_H3_ELEMENTS_PARSE)){
                View table = createView(child.text(), NEWS_BODY_H3_ELEMENTS_PARSE, "TextView", context);
                views.add(table);
            }

            if(child.tagName().equals(NEWS_BODY_TEXT_ELEMENTS_PARSE)) {
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
                            View bodyImage = createView(image, modifierTag, "ImageView", context);
                            views.add(bodyImage);
                        }

                        // при повторении нескольких модификаторов между парой кусков текста есть пробел
                        if (lengthOfChildrens != 0)
                            lengthOfChildrens += 1;

                        lengthOfChildrens += modifiedText.length();

                        // переходы на новую строку в блоке <p> и пустые абзацы не обрабатываются
                        if (!modifierTag.equals(NEWS_BODY_BR_ELEMENT) && modifiedText.length() != 0) {
                            View headerText = createView(modifiedText.trim(), modifierTag, "TextView", context);
                            views.add(headerText);
                        }
                    }
                }

                // пустые абзацы выбрасываем
                if (child.text().length() != 0) {
                    View text = createView(child.text().toString().substring(lengthOfChildrens).trim(),
                            "", "TextView", context);
                    views.add(text);
                }
            }
        }

        // если новость была загружена до внедрения CustomView у нее нет тегов, грузим без оформления
        if(views.size() == 0){
            View text = createView(htmlText, "", "TextView", context);
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

    private static View createView(String text, final String modifier, String viewType, final Context context){
        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
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
                view = new ImageView(context);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                final String pathToImage = context.getFilesDir() + "/" + LoadNewsTask.IMAGE_PATH + "/" + text;

                Glide.with(context)
                        .load(pathToImage)
                        .asBitmap()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.f1_logo)
                        .into(new BitmapImageViewTarget((ImageView)view) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                super.onResourceReady(bitmap, anim);
                                Glide.with(context).load(pathToImage).into(view);
                            }
                        });
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

        SystemUtils.saveTimersData(
                getNextGpTitle(jsDoc),
                getNextGpDate(jsDoc),
                Integer.parseInt(getNextGpTimestamp(jsDoc)),
                context);
    }

    public static void loadNextGranPriWeekend(String urlString, Context context) throws IOException{
        org.jsoup.nodes.Document jsDoc = DocParseUtils.getJsDoc(urlString);

        Element weekend = jsDoc.getElementsByClass(NEXT_WEEKEND_GP_HEAD_STREAM).first();
        String weekendTitle = weekend.getElementsByClass(NEXT_WEEKEND_GP_TITLE_STREAM).text();

        Element weekendTable = weekend.getElementsByClass(NEXT_WEEKEND_GP_BODY_STREAM).first();

        SystemUtils.saveWeekendData(weekendTitle, getWeekendData(weekendTable), context);
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
}
