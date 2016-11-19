package by.madcat.development.f1newsreader.dataInet;

public class InternetDataRouting {
    public static final String LINK_TAG = "guid";
    public static final String NEWS_TITLE_PARSE = "post_title";
    public static final String NEWS_BODY_PARSE = "post_body";
    public static final String NEWS_DATE_PARSE = "post_date";
    public static final String NEWS_IMAGE_DIV_PARSE = "post_thumbnail";
    public static final String NEWS_IMAGE_TAG_PARSE = "img";
    public static final String NEWS_IMAGE_LINK_ATTR_PARSE = "src";

    private static InternetDataRouting instance = null;

    private String routingMap;

    private InternetDataRouting(){
        routingMap = createRoutesMap();

    }

    public static InternetDataRouting getInstance(){
        if(instance == null)
            instance = new InternetDataRouting();

        return instance;
    }

    private String createRoutesMap(){
        String tmpRoutingMap = "http://www.f1news.ru/export/news.xml";
        return tmpRoutingMap;
    }

    public String getRoutingMap() {
        return routingMap;
    }
}
