package by.madcat.development.f1newsreader.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;

import by.madcat.development.f1newsreader.Models.RaceDataModel;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.data.DatabaseDescription;
import by.madcat.development.f1newsreader.dataInet.Models.TimingElement;

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

    public static String replaceImagesInWebView(String htmlText){
        Document doc = Jsoup.parse(htmlText);

        Elements imgs = doc.getElementsByTag("img");

        for(Element img : imgs){
            String imgName = StringUtils.getImageNameFromURL(img.attr("src"));
            img.attr("src", "file:///android_asset/tableImages/" + imgName);
        }

        return doc.toString();
    }

    public static RaceDataModel getRaceDataModel(TimingElement element, float pathLenght, int fps, float interval){
        float seconds = getSeconds(element.getLastLap());

        seconds += interval;

        if(seconds != 0)
            return new RaceDataModel(seconds * fps, pathLenght / (seconds * fps), 0);
        else
            return new RaceDataModel(0, 0f, 0);
    }

    private static float getSeconds(String secondsString){
        float tmpSeconds = 0;

        if(!secondsString.equals("STOP")) {
            tmpSeconds += Integer.valueOf(secondsString.split(":")[0]) * 60;
            tmpSeconds += Float.valueOf(secondsString.split(":")[1]);
        }

        return tmpSeconds;
    }

//    private static float getGap(String gapString){
//        float tmpGap = 0;
//
////        if(gapString.contains(":")) {
////            tmpGap += Integer.valueOf(gapString.split(":")[0]) * 60;
////            tmpGap += Float.valueOf(gapString.split(":")[1]);
////        }else
//        if(gapString.length() != 0 && !gapString.contains("L")){
//            tmpGap += Float.valueOf(gapString);
//        }
//
//        return tmpGap;
//    }
}
