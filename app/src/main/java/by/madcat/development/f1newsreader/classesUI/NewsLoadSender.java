package by.madcat.development.f1newsreader.classesUI;

public interface NewsLoadSender {
    public void sendNewsCountToAdapter(int count);
    public void sendNewsLoadToAdapter();
    public void loadComplete();
    public void loadCanceled();
}
