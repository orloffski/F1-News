package by.madcat.development.f1newsreader.dataInet;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import by.madcat.development.f1newsreader.Interfaces.NewsLoadSender;
import by.madcat.development.f1newsreader.Utils.DBUtils;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes;

public class LoadLinkListTask extends AsyncTask<Void, Void, Map<String, Map<NewsTypes, Date>>> {

    private String routeMap;
    private String mainSiteAdress;
    private Map<String, Map<NewsTypes, Date>> linksMap = null;
    private Context context;
    private NewsLoadSender sender;

    NewsLinkListToLoad links;

    public LoadLinkListTask(String routeMap, String mainSiteAdress, Context context, NewsLoadSender sender){

        links = NewsLinkListToLoad.getInstance(sender, context);
        links.setLock(true);

        this.routeMap = routeMap;
        this.mainSiteAdress = mainSiteAdress;
        this.context = context;
        this.sender = sender;
    }

    @Override
    protected Map<String, Map<NewsTypes, Date>> doInBackground(Void... voids) {
        linksMap = new HashMap<>();

        try {
            DocParseUtils.loadTimersData(mainSiteAdress, context);
            DocParseUtils.loadNextGranPrixWeekend(mainSiteAdress, context);
            loadNewsLinks(routeMap);
            SystemUtils.updateReminder(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        linksMap = checkLinksMap(linksMap);

        return linksMap;
    }

    @Override
    protected void onPostExecute(Map<String, Map<NewsTypes, Date>> strings) {
        super.onPostExecute(strings);

        for(Map.Entry entry : strings.entrySet()){
            ArrayList<String> dataLink = new ArrayList<>();
            dataLink.add(entry.getKey().toString());
            for(Map.Entry tmp : ((Map<NewsTypes, Date>) entry.getValue()).entrySet()) {
                dataLink.add(tmp.getKey().toString());
                dataLink.add(tmp.getValue().toString());
            }

            links.addLoadNewsTask(new LoadNewsTask(dataLink, context, links));
        }

        links.runLoadNews();
    }

    private void loadNewsLinks(String urlString) throws IOException{
        org.jsoup.nodes.Document jsDoc = DocParseUtils.getJsDoc(urlString);
        linksMap.putAll(DocParseUtils.getNewsLinkList(jsDoc));
    }

    private Map<String, Map<NewsTypes, Date>> checkLinksMap(Map<String, Map<NewsTypes, Date>> links){
        HashMap<String, Map<NewsTypes, Date>> checkedLinks = new HashMap<>();
        for(Map.Entry entry : links.entrySet()){
            if(DBUtils.checkIssetNewsLinkInDB(context, entry.getKey().toString()))
                checkedLinks.put(entry.getKey().toString(), (Map<NewsTypes, Date>)entry.getValue());
        }
        return checkedLinks;
    }
}
