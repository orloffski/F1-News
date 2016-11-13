package by.madcat.development.f1newsreader.dataInet;

import java.util.HashMap;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class InternetDataRouting {
    public static final String NEWS_LINK_LIST_TAG = "article article_large";
    public static final String GROUP_TAG_NAME = "div";
    public static final String HTTP_PREFIX = "http://";
    public static final String HTTP_PREFIX_TO_ADD = "http://www.f1news.ru";
    public static final String LINK_TAG = "a";
    public static final String NEWS_TITLE_PARSE = "post_title";
    public static final String NEWS_BODY_PARSE = "post_body";
    public static final String NEWS_DATE_PARSE = "post_date";
    public static final String NEWS_IMAGE_DIV_PARSE = "post_thumbnail";
    public static final String NEWS_IMAGE_TAG_PARSE = "img";
    public static final String NEWS_IMAGE_LINK_ATTR_PARSE = "src";

    private static InternetDataRouting instance = null;

    private HashMap<NewsTypes, String> routingMap;

    private InternetDataRouting(){
        routingMap = createRoutesMap();

    }

    public static InternetDataRouting getInstance(){
        if(instance == null)
            instance = new InternetDataRouting();

        return instance;
    }

    private HashMap<NewsTypes, String> createRoutesMap(){
        HashMap<NewsTypes, String> tmpRoutingMap = new HashMap<>();

        tmpRoutingMap.put(NewsTypes.NEWS, "http://www.f1news.ru/news/");
        tmpRoutingMap.put(NewsTypes.MEMUAR, "http://www.f1news.ru/memuar/");
        tmpRoutingMap.put(NewsTypes.TECH, "http://www.f1news.ru/tech/");
        tmpRoutingMap.put(NewsTypes.HISTORY, "http://www.f1news.ru/history/");
        tmpRoutingMap.put(NewsTypes.COLUMNS, "http://www.f1news.ru/columns/");

        return tmpRoutingMap;
    }

    public HashMap<NewsTypes, String> getRoutingMap() {
        return routingMap;
    }
}
