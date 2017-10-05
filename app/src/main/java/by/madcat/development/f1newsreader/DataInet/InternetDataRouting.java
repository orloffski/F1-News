package by.madcat.development.f1newsreader.DataInet;

public class InternetDataRouting {

    public final static String ROUTING_MAP = "https://www.f1news.ru/export/news.xml";
    public final static String MAIN_SITE_ADRESS = "https://www.f1news.ru";
    public final static String TEXT_ONLINE = "https://lc.f1news.ru/live/msg.json";

    private static InternetDataRouting instance = null;

    private String routingMap;
    private String mainSiteAdress;

    private InternetDataRouting(){
        routingMap = createRoutesMap();
        mainSiteAdress = createMainSiteAdress();

    }

    public static InternetDataRouting getInstance(){
        if(instance == null)
            instance = new InternetDataRouting();

        return instance;
    }

    private String createRoutesMap(){
        return ROUTING_MAP;
    }

    private String createMainSiteAdress(){
        return MAIN_SITE_ADRESS;
    }

    public String getRoutingMap() {
        return routingMap;
    }
    public String getMainSiteAdress() {
        return mainSiteAdress;
    }
}
