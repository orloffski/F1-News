package by.madcat.development.f1newsreader.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.DateUtils;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;
import by.madcat.development.f1newsreader.Utils.PreferencesUtils;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.dataInet.InternetDataRouting;

public class TimerNextGpTask extends AsyncTask<View, String, Void>{

    private Context context;

    private TextView timerText;
    private TextView timer;

    private boolean isOnline;

    public TimerNextGpTask(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(View... views) {
        timerText = (TextView) views[0];
        timer = (TextView) views[1];

        String toNextGP;
        String text;

        while(true){
            int timestamp = PreferencesUtils.getNextGpTime(context);

            if(timestamp != 0 && timestamp > System.currentTimeMillis()/1000) {
                toNextGP = DateUtils.getNextGpString(timestamp);
                text = SystemUtils.getNextGpData(context);

                // даем начать просмотр за 15 минут до старта Гран-При
                if(timestamp - System.currentTimeMillis()/1000 <= 900)
                    isOnline = true;
                else
                    isOnline = true;
//                    isOnline = false;

                publishProgress(text, toNextGP);
            }else if(timestamp != 0 && timestamp < System.currentTimeMillis()/1000){
                try {
                    DocParseUtils.loadTimersData(InternetDataRouting.getInstance().getMainSiteAdress(), context);

                    // перезагружаем данные о следующем гран-при
                    timestamp = PreferencesUtils.getNextGpTime(context);

                    // вдруг гонка онлайн и данные о текущем - фикс отсчета таймера в минус
                    if(timestamp > System.currentTimeMillis()/1000) {
                        text = SystemUtils.getNextGpData(context);
                        toNextGP = DateUtils.getNextGpString(timestamp);
                    }else{
                        text = SystemUtils.getNextGpData(context);
                        toNextGP = context.getResources().getString(R.string.gp_online);
                    }

                    isOnline = true;
                    publishProgress(text, toNextGP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                isOnline = false;
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

        timerText.setTextSize((float)PreferencesUtils.getWeekendTitleFontSize(context));
        timer.setTextSize((float)PreferencesUtils.getWeekendTimerFontSize(context));

//        if(isOnline) {
//            onlineLinksLayout.setVisibility(View.VISIBLE);
//        }else{
//            onlineLinksLayout.setVisibility(View.GONE);
//        }
    }
}
