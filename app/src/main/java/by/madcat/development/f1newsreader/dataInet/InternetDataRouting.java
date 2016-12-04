package by.madcat.development.f1newsreader.dataInet;

public class InternetDataRouting {
    public static final String LINK_TAG = "guid";
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
