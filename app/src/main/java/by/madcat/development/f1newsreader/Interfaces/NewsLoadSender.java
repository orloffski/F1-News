package by.madcat.development.f1newsreader.Interfaces;

public interface NewsLoadSender {
    void checkNewsLoadCount();
    void sendNewsLoadCount(int count);
    void loadStart();
    void loadComplete();
    void loadCanceled();
}
