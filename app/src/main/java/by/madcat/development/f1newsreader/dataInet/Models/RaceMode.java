package by.madcat.development.f1newsreader.dataInet.Models;

public class RaceMode {
    private String mode;
    private String flag;
    private int safetyCar;
    private String currentLap;
    private String totalLaps;
    private int trackTemp;
    private int airTemp;
    private String status;

    private static RaceMode instance;

    private RaceMode(String mode, String flag, int safetyCar, String currentLap, String totalLaps, int trackTemp, int airTemp, String status) {
        this.mode = mode;
        this.flag = flag;
        this.safetyCar = safetyCar;
        this.currentLap = currentLap;
        this.totalLaps = totalLaps;
        this.trackTemp = trackTemp;
        this.airTemp = airTemp;
        this.status = status;
    }

    public static RaceMode getInstance(){
        return instance;
    }

    public static RaceMode updateInstance(String mode, String flag, int safetyCar, String currentLap, String totalLaps, int trackTemp, int airTemp, String status){
        instance = new RaceMode(mode, flag, safetyCar, currentLap, totalLaps, trackTemp, airTemp, status);
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

    public String getStatus() {
        return status;
    }
}
