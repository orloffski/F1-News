package by.madcat.development.f1newsreader.Interfaces;

import by.madcat.development.f1newsreader.dataInet.LoadNewsTask;

public interface NewsLinkListObservable {
    void addLoadNewsTask(LoadNewsTask task);
    void removeLoadNewsTask(LoadNewsTask task);
    void runLoadNews();
    void completeLoadNews();
    void cancelLoadNews();
    int tasksToLoad();
}
