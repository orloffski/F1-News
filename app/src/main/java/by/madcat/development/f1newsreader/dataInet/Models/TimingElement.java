package by.madcat.development.f1newsreader.dataInet.Models;

public class TimingElement {
    private String name;
    private int position;
    private String gap;
    private String bestLap;
    private String pits;
    private int lastLap;

    public TimingElement(String name, int position, String gap, String bestLap, String pits, int lastLap) {
        this.name = name;
        this.position = position;
        this.gap = gap;
        this.bestLap = bestLap;
        this.pits = pits;
        this.lastLap = lastLap;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public String getGap() {
        return gap;
    }

    public String getBestLap() {
        return bestLap;
    }

    public String getPits() {
        return pits;
    }

    public int getLastLap() {
        return lastLap;
    }
}
