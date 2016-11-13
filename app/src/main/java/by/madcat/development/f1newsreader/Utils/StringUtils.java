package by.madcat.development.f1newsreader.Utils;

import by.madcat.development.f1newsreader.data.DatabaseDescription;

public final class StringUtils {
    private static final String NEWS_PREFIX = "/news/";
    private static final String MEMUAR_PREFIX = "/memuar/";
    private static final String INTERVIEW_PREFIX = "/interview/";
    private static final String TECH_PREFIX = "/tech/";
    private static final String HISTORY_PREFIX = "/Championship/";
    private static final String COLUMNS_PREFIX = "/columns/";
    private static final String AUTOSPORT_PREFIX = "/autosport-";

    public static String getImageNameFromURL(String url){
        return url.split("/")[url.split("/").length - 1];
    }

    public static boolean checkNewsLinkInSection(String link, DatabaseDescription.NewsTypes type){

        // check link to correct news section (delete news from other sections)
        String prefix = "";

        switch (type){
            case NEWS:
                prefix = NEWS_PREFIX;
                break;
            case MEMUAR:
                prefix = MEMUAR_PREFIX;
                break;
            case TECH:
                prefix = TECH_PREFIX;
                break;
            case HISTORY:
                prefix = HISTORY_PREFIX;
                break;
            case COLUMNS:
                prefix = COLUMNS_PREFIX;
                break;
            case AUTOSPORT:
                prefix = AUTOSPORT_PREFIX;
                break;
            case INTERVIEW:
                prefix = INTERVIEW_PREFIX;
                break;

        }

        if(!link.contains(prefix))
            return false;

        return true;
    }
}
