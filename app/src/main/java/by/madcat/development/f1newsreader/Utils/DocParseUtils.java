package by.madcat.development.f1newsreader.Utils;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;
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
        return jsDoc.getElementsByClass(InternetDataRouting.NEWS_BODY_PARSE).text();
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
}
