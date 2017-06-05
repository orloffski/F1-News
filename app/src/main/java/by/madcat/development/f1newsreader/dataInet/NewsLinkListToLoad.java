package by.madcat.development.f1newsreader.dataInet;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.AnalyticsTrackers.AnalyticsTrackers;
import by.madcat.development.f1newsreader.Interfaces.NewsLinkListObservable;
import by.madcat.development.f1newsreader.Interfaces.NewsLoadSender;

public class NewsLinkListToLoad implements NewsLinkListObservable{
    private static NewsLinkListToLoad ourInstance;
    private ArrayList<LoadNewsTask> newsLinkList;
    private int newsCount;
    private NewsLoadSender sender;
    private boolean lock;
    private AnalyticsTrackers analyticsTrackers;

    public static NewsLinkListToLoad getInstance(NewsLoadSender sender, AnalyticsTrackers analyticsTrackers) {
        if(ourInstance == null)
            ourInstance = new NewsLinkListToLoad(sender, analyticsTrackers);
        else if(ourInstance.newsLinkList.isEmpty())
            updateSender(sender);

        return ourInstance;
    }

    private NewsLinkListToLoad(NewsLoadSender sender, AnalyticsTrackers analyticsTrackers) {
        this.sender = sender;
        this.analyticsTrackers = analyticsTrackers;
        newsLinkList = new ArrayList<>();
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
            analyticsTrackers.trackEvent("Load news", "load run", "news count: " + newsLinkList.size());
            newsCount = newsLinkList.size();
            for (LoadNewsTask task : newsLinkList)
                task.execute();
        }else
            cancelLoadNews();
    }

    @Override
    public void completeLoadNews() {
        analyticsTrackers.trackEvent("Load news", "load complete", "loaded news count: " + newsCount);
        sender.sendNotification(newsCount);
        newsCount = 0;
        lock = false;
    }

    @Override
    public void cancelLoadNews() {
        analyticsTrackers.trackEvent("Load news", "load canceled", "loaded news count: " + 0);
        sender.sendNotification(0);
        newsCount = 0;
        lock = false;
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
