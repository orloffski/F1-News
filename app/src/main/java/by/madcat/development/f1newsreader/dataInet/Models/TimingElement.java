package by.madcat.development.f1newsreader.dataInet.Models;

public class TimingElement {
    private String name;
    private int position;
    private String gap;
    private String pits;
    private String lastLap;

    public TimingElement(String name, int position, String gap, String pits, String lastLap) {
        this.name = name;
        this.position = position;
        this.gap = gap;
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

    public String getPits() {
        return pits;
    }

    public String getLastLap() {
        return lastLap;
    }
}
