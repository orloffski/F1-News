package by.madcat.development.f1newsreader.Models;

public class RaceDataModel {
    private float seconds;
    private float segmentLenght;
    private int timer;
    private boolean toDelete;

    public RaceDataModel(float seconds, float segmentLenght, int timer) {
        this.seconds = seconds;
        this.segmentLenght = segmentLenght;
        this.timer = timer;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public float getSeconds() {
        return seconds;
    }

    public void setSeconds(float seconds) {
        this.seconds = seconds;
    }

    public float getSegmentLenght() {
        return segmentLenght;
    }

    public void setSegmentLenght(float segmentLenght) {
        this.segmentLenght = segmentLenght;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
