package by.madcat.development.f1newsreader.dataInet.Models;

public class SessionStatus {
    private int airTemp;
    private int trackTemp;
    private int currentLap;
    private int totalLaps;
    private boolean safetyCar;
    private String flag;

    public SessionStatus(int airTemp, int trackTemp, int currentLap, int totalLaps, boolean safetyCar, String flag) {
        this.airTemp = airTemp;
        this.trackTemp = trackTemp;
        this.currentLap = currentLap;
        this.totalLaps = totalLaps;
        this.safetyCar = safetyCar;
        this.flag = flag;
    }

    public int getAirTemp() {
        return airTemp;
    }

    public int getTrackTemp() {
        return trackTemp;
    }

    public int getCurrentLap() {
        return currentLap;
    }

    public int getTotalLaps() {
        return totalLaps;
    }

    public boolean isSafetyCar() {
        return safetyCar;
    }

    public String getFlag() {
        return flag;
    }
}
