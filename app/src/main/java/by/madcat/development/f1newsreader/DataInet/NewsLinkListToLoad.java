package by.madcat.development.f1newsreader.DataInet;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.Interfaces.NewsLinkListObservable;
import by.madcat.development.f1newsreader.Interfaces.NewsLoadSender;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.SystemUtils;

public class NewsLinkListToLoad implements NewsLinkListObservable{
    private static NewsLinkListToLoad ourInstance;
    private ArrayList<LoadNewsTask> newsLinkList;
    private int newsCount;
    private NewsLoadSender sender;
    private boolean lock;
    private Context context;

    public static NewsLinkListToLoad getInstance(NewsLoadSender sender, Context context) {
        if(ourInstance == null)
            ourInstance = new NewsLinkListToLoad(sender, context);
        else if(ourInstance.newsLinkList.isEmpty())
            updateSender(sender);

        return ourInstance;
    }

    private NewsLinkListToLoad(NewsLoadSender sender, Context context) {
        this.sender = sender;
        newsLinkList = new ArrayList<>();
        this.context = context;
    }

    private static void updateSender(NewsLoadSender newSender){
        ourInstance.sender = newSender;
    }

    @Override
    public void addLoadNewsTask(LoadNewsTask task) {
        if(!lock)
            lock = true;

        newsLinkList.add(task);
    }

    @Override
    public void removeLoadNewsTask(LoadNewsTask task) {
        synchronized (newsLinkList) {
            newsLinkList.remove(task);
        }

        if(newsLinkList.isEmpty())
            completeLoadNews();
    }

    @Override
    public void runLoadNews() {
        if(!newsLinkList.isEmpty()) {
            newsCount = newsLinkList.size();
            for (LoadNewsTask task : newsLinkList)
                task.execute();
        }else
            cancelLoadNews();
    }

    @Override
    public void completeLoadNews() {
        sender.sendNotification(newsCount);
        newsCount = 0;
        lock = false;

        SystemUtils.addServiceToAlarmManager(
                context,
                PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getBoolean("refresh_interval_on", false),
                Integer.parseInt(PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getString("refresh_interval", context.getString(R.string.intervals_default_value)))
        );
    }

    @Override
    public void cancelLoadNews() {
        sender.sendNotification(0);
        newsCount = 0;
        lock = false;

        SystemUtils.addServiceToAlarmManager(
                context,
                PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getBoolean("refresh_interval_on", false),
                Integer.parseInt(PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getString("refresh_interval", context.getString(R.string.intervals_default_value)))
        );
    }

    @Override
    public int tasksToLoad() {
        if(newsLinkList == null)
            return 0;
        else
            return newsLinkList.size();
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
        ourInstance = null;
    }
}
