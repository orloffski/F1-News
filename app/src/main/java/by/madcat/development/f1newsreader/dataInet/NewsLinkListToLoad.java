package by.madcat.development.f1newsreader.dataInet;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.Interfaces.NewsLinkListObservable;
import by.madcat.development.f1newsreader.Interfaces.NewsLoadSender;

public class NewsLinkListToLoad implements NewsLinkListObservable{
    private static NewsLinkListToLoad ourInstance;
    private ArrayList<LoadNewsTask> newsLinkList;
    private int newsCount;
    private NewsLoadSender sender;
    private boolean lock;

    public static NewsLinkListToLoad getInstance(NewsLoadSender sender) {
        if(ourInstance == null)
            ourInstance = new NewsLinkListToLoad(sender);
        else if(ourInstance.newsLinkList.isEmpty())
            updateSender(sender);

        return ourInstance;
    }

    private NewsLinkListToLoad(NewsLoadSender sender) {
        this.sender = sender;
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
        newsCount++;
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
        if(!newsLinkList.isEmpty())
            for(LoadNewsTask task: newsLinkList)
                task.execute();
        else
            cancelLoadNews();
    }

    @Override
    public void completeLoadNews() {
        sender.sendNotification(newsCount);
        newsCount = 0;
        lock = false;
    }

    @Override
    public void cancelLoadNews() {
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
    }
}
