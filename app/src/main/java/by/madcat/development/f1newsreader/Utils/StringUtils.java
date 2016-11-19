package by.madcat.development.f1newsreader.Utils;

import by.madcat.development.f1newsreader.data.DatabaseDescription;

public final class StringUtils {
    private static final String NEWS_PREFIX = "f1news.ru/news/f1-";
    private static final String MEMUAR_PREFIX = "f1news.ru/memuar/";
    private static final String INTERVIEW_PREFIX = "f1news.ru/interview/";
    private static final String TECH_PREFIX = "f1news.ru/tech/";
    private static final String HISTORY_PREFIX = "f1news.ru/Championship/";
    private static final String COLUMNS_PREFIX = "f1news.ru/columns/";
    private static final String AUTOSPORT_PREFIX = "f1news.ru/news/autosport-";
    private static final String WEC_PREFIX = "f1news.ru/news/wec-";

    public static String getImageNameFromURL(String url){
        return url.split("/")[url.split("/").length - 1];
    }

    public static DatabaseDescription.NewsTypes getNewsSection(String link){
        if(link.contains(NEWS_PREFIX))
            return DatabaseDescription.NewsTypes.NEWS;
        else if(link.contains(MEMUAR_PREFIX))
            return DatabaseDescription.NewsTypes.MEMUAR;
        else if(link.contains(INTERVIEW_PREFIX))
            return DatabaseDescription.NewsTypes.INTERVIEW;
        else if(link.contains(TECH_PREFIX))
            return DatabaseDescription.NewsTypes.TECH;
        else if(link.contains(HISTORY_PREFIX))
            return DatabaseDescription.NewsTypes.HISTORY;
        else if(link.contains(COLUMNS_PREFIX))
            return DatabaseDescription.NewsTypes.COLUMNS;
        else if(link.contains(AUTOSPORT_PREFIX))
            return DatabaseDescription.NewsTypes.AUTOSPORT;
        else if(link.contains(WEC_PREFIX))
            return DatabaseDescription.NewsTypes.AUTOSPORT;

        return null;
    }
}
