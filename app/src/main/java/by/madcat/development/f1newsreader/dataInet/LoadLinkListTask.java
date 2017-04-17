package by.madcat.development.f1newsreader.dataInet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.madcat.development.f1newsreader.Interfaces.NewsLoadSender;
import by.madcat.development.f1newsreader.Utils.DBUtils;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.data.DatabaseDescription.NewsTypes;

public class LoadLinkListTask extends AsyncTask<Void, Void, Map<String, NewsTypes>> {

    private String routeMap;
    private String mainSiteAdress;
    private Map<String, NewsTypes> links = null;
    private Context context;
    private NewsLoadSender sender;

    public LoadLinkListTask(String routeMap, String mainSiteAdress, Context context, NewsLoadSender sender){
        this.routeMap = routeMap;
        this.mainSiteAdress = mainSiteAdress;
        this.context = context;
        this.sender = sender;
    }

    @Override
    protected Map<String, NewsTypes> doInBackground(Void... voids) {
        links = new HashMap<>();

        try {
            loadTimersData(mainSiteAdress);
            loadNewsLinks(routeMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        links = checkLinksMap(links);

        return links;
    }

    @Override
    protected void onPostExecute(Map<String, NewsTypes> strings) {
        super.onPostExecute(strings);

        sender.sendNewsLoadCount(0);

        if(strings.size() == 0){
            sender.loadCanceled();
            return;
        }

        sender.sendNewsLoadCount(strings.size());
        sender.loadStart();

        for(Map.Entry entry : strings.entrySet()){
            ArrayList<String> dataLink = new ArrayList<>();
            dataLink.add(entry.getKey().toString());
            dataLink.add(entry.getValue().toString());

            new LoadNewsTask(dataLink, context, sender).execute();
        }
    }

    private void loadNewsLinks(String urlString) throws IOException{
        org.jsoup.nodes.Document jsDoc = DocParseUtils.getJsDoc(urlString);
        links.putAll(DocParseUtils.getNewsLinkList(jsDoc));
    }

    private Map<String, NewsTypes> checkLinksMap(Map<String, NewsTypes> links){
        HashMap<String, NewsTypes> checkedLinks = new HashMap<>();
        for(Map.Entry entry : links.entrySet()){
            if(DBUtils.checkIssetNewsLinkInDB(context, entry.getKey().toString()))
                checkedLinks.put(entry.getKey().toString(), NewsTypes.valueOf(entry.getValue().toString()));
        }
        return checkedLinks;
    }

    private void loadTimersData(String urlString) throws IOException{
        org.jsoup.nodes.Document jsDoc = DocParseUtils.getJsDoc(urlString);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("gp_country", DocParseUtils.getNextGpTitle(jsDoc));
        editor.putString("gp_date", DocParseUtils.getNextGpDate(jsDoc));
        editor.putInt("gp_timestamp", Integer.parseInt(DocParseUtils.getNextGpTimestamp(jsDoc)));
        editor.commit();
    }
}
