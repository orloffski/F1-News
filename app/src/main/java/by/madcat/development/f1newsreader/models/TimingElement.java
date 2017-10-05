package by.madcat.development.f1newsreader.models;

public class TimingElement {
    private String name;
    private String position;
    private String gap;
    private String pits;
    private String lastLap;
    private String bestLap;

    public TimingElement(String name, String position, String gap, String pits, String bestLap, String lastLap) {
        this.name = name;
        this.position = position;
        this.gap = gap;
        this.pits = pits;
        this.bestLap = bestLap;
        this.lastLap = lastLap;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
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

    public String getBestLap() {
        return bestLap;
    }
}
