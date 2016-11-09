package by.madcat.development.f1newsreader.dataInet;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class LoadLinkListTask extends AsyncTask<Void, Void, ArrayList<String>>{

    private static final String HTTP_PREFIX = "http://";
    private static final String HTTP_PREFIX_TO_ADD = "http://www.f1news.ru";
    private static final String SPLIT_ELEMENT = "\"";

    private HashMap<NewsTypes, ArrayList<String>> routeMap;
    private ArrayList<String> links = null;

    public LoadLinkListTask(HashMap<NewsTypes, ArrayList<String>> routeMap){
        this.routeMap = routeMap;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // update UI - run load links
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        links = new ArrayList<>();

        try {
            links.addAll(loadNewsLinks(routeMap.get(NewsTypes.NEWS)));
            links.addAll(loadMemuarsLinks(routeMap.get(NewsTypes.MEMUAR)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return links;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);

        // update UI - finish load links, start load data from links
        // run load data
    }

    private ArrayList<String> loadNewsLinks(ArrayList<String> parseData) throws IOException {
        ArrayList<String> links = new ArrayList<>();

        String urlString = parseData.get(0);
        String newsLinkListTag = parseData.get(1);
        String groupTagName = parseData.get(2);

        String line;
        StringBuilder doc = new StringBuilder();

        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

        while((line = reader.readLine()) != null)
            doc.append(line);

        org.jsoup.nodes.Document jsDoc = Jsoup.parse(doc.toString(), "UTF-8");

        Elements ulElements = jsDoc.getElementsByClass(newsLinkListTag);
        org.jsoup.nodes.Element ulElement = ulElements.first();
        for(org.jsoup.nodes.Element liElement : ulElement.getElementsByTag(groupTagName)){
            Elements linkElements = liElement.getElementsByTag("a");
            String linkAttr = linkElements.first().attributes().toString();
            String link = linkAttr.split(SPLIT_ELEMENT)[1];
            if(!link.contains(HTTP_PREFIX))
                link = HTTP_PREFIX_TO_ADD + link;

            links.add(link);
        }

        return links;
    }

    private ArrayList<String> loadMemuarsLinks(ArrayList<String> parseData) throws IOException{
        ArrayList<String> links = new ArrayList<>();

        String urlString = parseData.get(0);
        String newsLinkListTag = parseData.get(1);
        String groupTagName = parseData.get(2);

        String line;
        StringBuilder doc = new StringBuilder();

        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

        while((line = reader.readLine()) != null)
            doc.append(line);

        org.jsoup.nodes.Document jsDoc = Jsoup.parse(doc.toString(), "UTF-8");

        for(org.jsoup.nodes.Element mainDiv : jsDoc.getElementsByClass(newsLinkListTag)){
            Elements linkDivs = mainDiv.getElementsByTag(groupTagName);
            org.jsoup.nodes.Element linkDiv = linkDivs.first();
            Elements linkElements = linkDiv.getElementsByTag("a");
            String linkAttr = linkElements.first().attributes().toString();
            String link = linkAttr.split(SPLIT_ELEMENT)[1];
            if(!link.contains(HTTP_PREFIX))
                link = HTTP_PREFIX_TO_ADD + link;

            links.add(link);
        }

        return links;
    }
}
