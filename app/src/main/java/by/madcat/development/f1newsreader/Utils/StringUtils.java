package by.madcat.development.f1newsreader.Utils;

import java.util.Calendar;

import by.madcat.development.f1newsreader.R;
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

    public static String getVideoIdFromURL(String url){
        String idTemp = url.split("/")[url.split("/").length - 1];
        idTemp = idTemp.split("\\?")[0];

        return idTemp;
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
        else if(link.contains(HISTORY_PREFIX)) {
            if(link.contains(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))))
                return DatabaseDescription.NewsTypes.NEWS;
            else
                return DatabaseDescription.NewsTypes.HISTORY;}
        else if(link.contains(COLUMNS_PREFIX))
            return DatabaseDescription.NewsTypes.COLUMNS;
        else if(link.contains(AUTOSPORT_PREFIX))
            return DatabaseDescription.NewsTypes.AUTOSPORT;
        else if(link.contains(WEC_PREFIX))
            return DatabaseDescription.NewsTypes.AUTOSPORT;

        return null;
    }

    public static String addNewLineCharInLongLine(String line){
        StringBuilder tmpLine = new StringBuilder();
        boolean needNewLine = false;

        for(int i = 0; i < line.length(); i++){
            if(needNewLine && line.charAt(i) != ' '){
                tmpLine.append("\n");
                tmpLine.append(line.charAt(i));
                needNewLine = false;
            }else {
                if (Character.isDigit(line.charAt(i))) {
                    if (i != line.length() - 1 && line.charAt(i + 1) == ' ') {
                        tmpLine.append(line.charAt(i));
                        needNewLine = true;
                    } else {
                        tmpLine.append(line.charAt(i));
                    }
                } else {
                    tmpLine.append(line.charAt(i));
                }
            }
        }

        return tmpLine.toString();
    }

    public static int getImageByTitle(String title){
        switch (title){
            case "Новости":
                return R.drawable.drawerimage_news;
            case "Статьи":
                return R.drawable.drawerimage_memuar;
            case "Интервью":
                return R.drawable.drawerimage_interview;
            case "Техника":
                return R.drawable.drawerimage_tech;
            case "История":
                return R.drawable.drawerimage_history;
            case "Авторские колонки":
                return R.drawable.drawerimage_columns;
            case "Автоспорт":
                return R.drawable.drawerimage_autosport;
            default:
                return R.drawable.drawerimage_news;
        }
    }
}
