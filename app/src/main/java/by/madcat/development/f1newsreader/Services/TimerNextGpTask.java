package by.madcat.development.f1newsreader.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import by.madcat.development.f1newsreader.Utils.DateUtils;

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

        String text = PreferenceManager.getDefaultSharedPreferences(context).getString("gp_country", "")
                + "\n" + PreferenceManager.getDefaultSharedPreferences(context).getString("gp_date", "");
        int timestamp = PreferenceManager.getDefaultSharedPreferences(context).getInt("gp_timestamp", 0);
        String toNextGP = "";



        while(true){
            if(timestamp != 0)
                toNextGP = DateUtils.getNextGpString(timestamp);

            publishProgress(text, toNextGP);

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
