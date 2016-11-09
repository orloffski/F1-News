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
import java.util.Map;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class LoadLinkListTask extends AsyncTask<Void, Void, Map<String, NewsTypes>>{
    private static final String SPLIT_ELEMENT = "\"";

    private HashMap<NewsTypes, String> routeMap;
    private Map<String, NewsTypes> links = null;

    public LoadLinkListTask(HashMap<NewsTypes, String> routeMap){
        this.routeMap = routeMap;
    }

    @Override
    protected Map<String, NewsTypes> doInBackground(Void... voids) {
        links = new HashMap<>();

        try {
            loadNewsLinks(routeMap.get(NewsTypes.NEWS), NewsTypes.NEWS);
            loadNewsLinks(routeMap.get(NewsTypes.MEMUAR), NewsTypes.MEMUAR);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return links;
    }

    @Override
    protected void onPostExecute(Map<String, NewsTypes> strings) {
        super.onPostExecute(strings);

        for(Map.Entry entry : strings.entrySet()){
            ArrayList<String> dataLink = new ArrayList<>();
            dataLink.add(entry.getKey().toString());
            dataLink.add(entry.getValue().toString());

            new LoadNewsTask(dataLink).execute();
        }
    }

    private void loadNewsLinks(String urlString, NewsTypes type) throws IOException{

        String line;
        StringBuilder doc = new StringBuilder();

        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

        while((line = reader.readLine()) != null)
            doc.append(line);

        org.jsoup.nodes.Document jsDoc = Jsoup.parse(doc.toString(), "UTF-8");

        for(org.jsoup.nodes.Element mainDiv : jsDoc.getElementsByClass(InternetDataRouting.NEWS_LINK_LIST_TAG)){
            Elements linkDivs = mainDiv.getElementsByTag(InternetDataRouting.GROUP_TAG_NAME);
            org.jsoup.nodes.Element linkDiv = linkDivs.first();
            Elements linkElements = linkDiv.getElementsByTag(InternetDataRouting.LINK_TAG);
            String linkAttr = linkElements.first().attributes().toString();
            String link = linkAttr.split(SPLIT_ELEMENT)[1];
            if(!link.contains(InternetDataRouting.HTTP_PREFIX))
                link = InternetDataRouting.HTTP_PREFIX_TO_ADD + link;

            links.put(link, type);
        }
    }
}
