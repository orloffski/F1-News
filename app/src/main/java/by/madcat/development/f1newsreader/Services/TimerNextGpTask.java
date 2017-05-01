package by.madcat.development.f1newsreader.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import by.madcat.development.f1newsreader.Utils.DateUtils;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;

public class TimerNextGpTask extends AsyncTask<TextView, String, Void>{

    private Context context;

    private TextView timerText;
    private TextView timer;

    public TimerNextGpTask(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(TextView... views) {
        timerText = views[0];
        timer = views[1];

        String toNextGP;
        String text;

        while(true){
            int timestamp = SystemUtils.getNextGpTime(context);

            if(timestamp != 0 && timestamp > System.currentTimeMillis()/1000) {
                toNextGP = DateUtils.getNextGpString(timestamp);
                text = SystemUtils.getNextGpData(context);

                publishProgress(text, toNextGP);
            }
            else if(timestamp != 0 && timestamp < System.currentTimeMillis()/1000){
                try {
                    DocParseUtils.loadTimersData(InternetDataRouting.getInstance().getMainSiteAdress(), context);
                    
                    text = SystemUtils.getNextGpData(context);
                    toNextGP = DateUtils.getNextGpString(timestamp);

                    publishProgress(text, toNextGP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                publishProgress("", "");
            }



            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(String... timers) {
        super.onProgressUpdate(timers);

        timerText.setText(timers[0]);
        timer.setText(timers[1]);
    }
}
