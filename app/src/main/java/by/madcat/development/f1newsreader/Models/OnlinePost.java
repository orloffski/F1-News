package by.madcat.development.f1newsreader.Models;

public class OnlinePost {
    private String onlinePostText;
    private String onlinePostTime;

    public OnlinePost(String onlinePostText, String onlinePostTime) {
        this.onlinePostText = onlinePostText;
        this.onlinePostTime = onlinePostTime;
    }

    public String getOnlinePostText() {
        return onlinePostText;
    }

    public String getOnlinePostTime() {
        return onlinePostTime;
    }
}
