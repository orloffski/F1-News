package by.madcat.development.f1newsreader.dataInet;

import java.util.ArrayList;
import java.util.HashMap;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class InternetDataRouting {
    private static InternetDataRouting instance = null;

    private HashMap<NewsTypes, ArrayList<String>> routingMap;

    private InternetDataRouting(){
        routingMap = createRoutesMap();

    }

    public static InternetDataRouting getInstance(){
        if(instance == null)
            instance = new InternetDataRouting();

        return instance;
    }

    private HashMap<NewsTypes, ArrayList<String>> createRoutesMap(){
        HashMap<NewsTypes, ArrayList<String>> tmpRoutingMap = new HashMap<>();

        tmpRoutingMap.put(NewsTypes.NEWS, createRoute("http://www.f1news.ru/", "list list_striped", "li"));
        tmpRoutingMap.put(NewsTypes.MEMUAR, createRoute("http://www.f1news.ru/memuar/", "article article_large", "div"));

        return tmpRoutingMap;
    }

    private ArrayList<String> createRoute(String newsPageLink, String newsLinkListTag, String groupTagName){
        ArrayList<String> route = new ArrayList<>();

        route.add(newsPageLink);
        route.add(newsLinkListTag);
        route.add(groupTagName);

        return route;
    }

    public HashMap<NewsTypes, ArrayList<String>> getRoutingMap() {
        return routingMap;
    }
}
