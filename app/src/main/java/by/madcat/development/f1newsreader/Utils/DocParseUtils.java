package by.madcat.development.f1newsreader.Utils;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;

public final class DocParseUtils {
    public static org.jsoup.nodes.Document getJsDoc(String urlString) throws IOException {
        String line;
        StringBuilder doc = new StringBuilder();

        URL url = new URL(urlString);
        InputStreamReader isReader = new InputStreamReader(url.openConnection().getInputStream());
        BufferedReader reader = new BufferedReader(isReader);

        while((line = reader.readLine()) != null)
            doc.append(line);

        org.jsoup.nodes.Document jsDoc = Jsoup.parse(doc.toString(), "UTF-8");

        reader.close();
        isReader.close();

        return jsDoc;
    }

    public static String getNewsTitle(org.jsoup.nodes.Document jsDoc){
        return jsDoc.getElementsByClass(InternetDataRouting.NEWS_TITLE_PARSE).text();
    }

    public static String getNewsBody(org.jsoup.nodes.Document jsDoc){
        StringBuilder newsBody = new StringBuilder();
        Element news_content = jsDoc.getElementsByClass(InternetDataRouting.NEWS_BODY_PARSE).first();
        for(Element p : news_content.getElementsByTag(InternetDataRouting.NEWS_BODY_ELEMENTS_PARSE))
            newsBody.append(p.toString());
        return newsBody.toString();
    }

    public static String getNewsDate(org.jsoup.nodes.Document jsDoc){
        String dateTime = "";
        Elements news_date = jsDoc.getElementsByClass(InternetDataRouting.NEWS_DATE_PARSE);
        if(news_date.isEmpty()){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm");
            dateTime = simpleDateFormat.format(new Date());
        }else{
            dateTime = news_date.text();
        }

        return dateTime;
    }

    public static String getNewsImage(org.jsoup.nodes.Document jsDoc){
        String image = "";

        Elements news_image_div = jsDoc.getElementsByClass(InternetDataRouting.NEWS_IMAGE_DIV_PARSE);
        if(news_image_div.first() != null) {
            Elements linkDivs = news_image_div.first().getElementsByTag(InternetDataRouting.NEWS_IMAGE_TAG_PARSE);
            image = linkDivs.first().attr(InternetDataRouting.NEWS_IMAGE_LINK_ATTR_PARSE);
        }

        return image;
    }

    public static Map<String, NewsTypes> getNewsLinkList(org.jsoup.nodes.Document jsDoc){
        Map<String, NewsTypes> links = new HashMap<>();

        for(org.jsoup.nodes.Element guid : jsDoc.getElementsByTag(InternetDataRouting.LINK_TAG)){
            String link = guid.text();
            NewsTypes type = StringUtils.getNewsSection(link);
            if(type != null)
                links.put(link, type);
        }

        return links;
    }

    public static ArrayList<View> getViews(String htmlText, Context context){
        ArrayList<View> views = new ArrayList<>();


        org.jsoup.nodes.Document jsDoc = Jsoup.parse(htmlText.toString(), "UTF-8");

        for(Element p : jsDoc.getElementsByTag(InternetDataRouting.NEWS_BODY_ELEMENTS_PARSE)){
            String modifiedText = "";
            String modifierTag = "";
            int lengthOfChildrens = 0;

            if(p.children().size() != 0){
                for(Element children : p.children()){
                    modifiedText = children.text();
                    modifierTag = children.tagName();

                    // пока все ссылки выброшены - в дальнейшем есть планы сделать переход на новость по ссылке
                    if(modifierTag.equals("a"))
                        break;

                    // при повторении нескольких модификаторов между парой кусков текста есть пробел
                    if(lengthOfChildrens != 0)
                        lengthOfChildrens += 1;

                    lengthOfChildrens += modifiedText.length();

                    // переходы на новую строку в блоке <p> - имба PHP-прогеров портала... рисунки в новости пока не реализованы
                    if(!modifierTag.equals("br") && !modifierTag.equals("img")) {
                        View headerText = createView(modifiedText, modifierTag, "TextView", context);
                        views.add(headerText);
                    }
                }
            }

            // пустые абзацы выбрасываем - PHP-программисты такие программисты
            if(p.text().length() != 0) {
                View text = createView(p.text().toString().substring(lengthOfChildrens),
                        "", "TextView", context);
                views.add(text);
            }
        }

        // если новость была загружена до внедрения CustomView у нее нет тегов, грузим без оформления
        if(views.size() == 0){
            View text = createView(htmlText, "", "TextView", context);
            views.add(text);
        }

        return views;
    }

    private static View createView(String text, String modifier, String viewType, Context context){
        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = null;

        switch (viewType){
            case "TextView":
                view = new TextView(context);
                ((TextView)view).setText(text);
                view.setPadding(0, 5, 0, 0);
                view.setLayoutParams(textViewLayoutParams);
                break;
        }

        switch (modifier){
            case "b": case "span":  // на портале стиль жирного текста ради стиля - верстальщики как и PHP-прогеры)))
                ((TextView)view).setTypeface(null, Typeface.BOLD);
                break;
            case "i":
                ((TextView)view).setTypeface(null, Typeface.ITALIC);
        }

        return view;
    }
}
