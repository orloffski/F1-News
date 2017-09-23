package by.madcat.development.f1newsreader.Models;

public enum TracksDataModel {
    MARINABAY("Гран При Сингапура"),
    SEPANG("Гран При Малайзии"),
    SUZUKA("Гран При Японии"),
    AUSTIN("Гран При США"),
    MEXICO("Гран При Мексики"),
    INTERLAGOS("Гран При Бразилии"),
    YASMARINA("Гран При Абу-Даби");

    private final String name;

    TracksDataModel(String name){
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

    public static TracksDataModel find(String other){
        for(TracksDataModel tracks: TracksDataModel.values())
            if(tracks.equalsName(other))
                return tracks;

        return null;
    }

    public static String getTrackName(String NameGP){
        String trackName = "";

        switch (NameGP){
            case "Гран При Сингапура":
                trackName = "marinabay";
                break;
            case "Гран При Малайзии":
                trackName = "sepang";
                break;
            case "Гран При Японии":
                trackName = "suzuka";
                break;
            case "Гран При США":
                trackName = "austin";
                break;
            case "Гран При Мексики":
                trackName = "mexico";
                break;
            case "Гран При Бразилии":
                trackName = "interlagos";
                break;
            case "Гран При Абу-Даби":
                trackName = "yasmarina";
                break;
        }

        return trackName;
    }
}
