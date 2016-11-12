package by.madcat.development.f1newsreader.dataInet;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class LoadLinkListTask extends AsyncTask<Void, Void, Map<String, NewsTypes>>{

    private HashMap<NewsTypes, String> routeMap;
    private Map<String, NewsTypes> links = null;
    private Context context;

    public LoadLinkListTask(HashMap<NewsTypes, String> routeMap, Context context){
        this.routeMap = routeMap;
        this.context = context;
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

            new LoadNewsTask(dataLink, context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void loadNewsLinks(String urlString, NewsTypes type) throws IOException{

        org.jsoup.nodes.Document jsDoc = DocParseUtils.getJsDoc(urlString);
        links.putAll(DocParseUtils.getNewsLinkList(jsDoc, type));
    }
}
