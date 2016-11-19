package by.madcat.development.f1newsreader.dataInet;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.madcat.development.f1newsreader.Utils.DBUtils;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.classesUI.NewsLoadSender;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class LoadLinkListTask extends AsyncTask<Void, Void, Map<String, NewsTypes>> {

    private String routeMap;
    private Map<String, NewsTypes> links = null;
    private Context context;
    private NewsLoadSender sender;

    public LoadLinkListTask(String routeMap, Context context, NewsLoadSender sender){
        this.routeMap = routeMap;
        this.context = context;
        this.sender = sender;
    }

    @Override
    protected Map<String, NewsTypes> doInBackground(Void... voids) {
        links = new HashMap<>();

        try {
            loadNewsLinks(routeMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return links;
    }

    @Override
    protected void onPostExecute(Map<String, NewsTypes> strings) {
        super.onPostExecute(strings);

        strings = checkLinksMap(strings);

        sender.sendNewsCountToAdapter(0);

        if(strings.size() == 0){
            sender.loadComplete();
            return;
        }

        sender.sendNewsCountToAdapter(strings.size());

        for(Map.Entry entry : strings.entrySet()){
            ArrayList<String> dataLink = new ArrayList<>();
            dataLink.add(entry.getKey().toString());
            dataLink.add(entry.getValue().toString());

            new LoadNewsTask(dataLink, context, sender).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
}
