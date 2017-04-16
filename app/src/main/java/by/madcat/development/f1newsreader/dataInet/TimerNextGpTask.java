package by.madcat.development.f1newsreader.dataInet;

import android.os.AsyncTask;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import by.madcat.development.f1newsreader.Utils.DateUtils;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;

public class TimerNextGpTask extends AsyncTask<TextView, String, Void>{

    private Document jsDoc;

    private TextView timerText;
    private TextView timer;

    @Override
    protected Void doInBackground(TextView... views) {

        timerText = views[0];
        timer = views[1];

        String timerText = "";
        int timestamp = 0;
        String toNextGP = "";

        try {
            jsDoc = DocParseUtils.getJsDoc(InternetDataRouting.getInstance().getMainSiteAdress());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(jsDoc != null){
            timerText = DocParseUtils.getNextGpTitle(jsDoc);
            timerText += "\n" + DocParseUtils.getNextGpDate(jsDoc);

            timestamp = Integer.parseInt(DocParseUtils.getNextGpTimestamp(jsDoc)) ;
        }

        while(true){
            publishProgress(timerText, DateUtils.getNextGpString(timestamp));

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //return null;
    }

    @Override
    protected void onProgressUpdate(String... timers) {
        super.onProgressUpdate(timers);

        timerText.setText(timers[0]);
        timer.setText(timers[1]);
    }
}
