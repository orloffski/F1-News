package by.madcat.development.f1newsreader.dataInet;

public class InternetDataRouting {

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
