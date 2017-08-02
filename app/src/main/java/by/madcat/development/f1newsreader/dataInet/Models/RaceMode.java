package by.madcat.development.f1newsreader.dataInet.Models;

public class RaceMode {
    private String mode;
    private String flag;
    private int safetyCar;
    private String currentLap;
    private String totalLaps;
    private int trackTemp;
    private int airTemp;

    private static RaceMode instance;

    private RaceMode(String mode, String flag, int safetyCar, String currentLap, String totalLaps, int trackTemp, int airTemp) {
        this.mode = mode;
        this.flag = flag;
        this.safetyCar = safetyCar;
        this.currentLap = currentLap;
        this.totalLaps = totalLaps;
        this.trackTemp = trackTemp;
        this.airTemp = airTemp;
    }

    public static RaceMode getInstance(){
        return instance;
    }

    public static RaceMode updateInstance(String mode, String flag, int safetyCar, String currentLap, String totalLaps, int trackTemp, int airTemp){
        instance = new RaceMode(mode, flag, safetyCar, currentLap, totalLaps, trackTemp, airTemp);
        return instance;
    }

    public String getMode() {
        return mode;
    }

    public String getFlag() {
        return flag;
    }

    public int getSafetyCar() {
        return safetyCar;
    }

    public String getCurrentLap() {
        return currentLap;
    }

    public String getTotalLaps() {
        return totalLaps;
    }

    public int getTrackTemp() {
        return trackTemp;
    }

    public int getAirTemp() {
        return airTemp;
    }
}
