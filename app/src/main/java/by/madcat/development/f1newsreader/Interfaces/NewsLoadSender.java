package by.madcat.development.f1newsreader.Interfaces;

public interface NewsLoadSender {
    void checkNewsLoadCount(boolean loaded);
    void sendNewsLoadCount(int count);
    void cancelLinkLoad();
    void loadStart();
    void loadComplete();
    void loadCanceled();
}
