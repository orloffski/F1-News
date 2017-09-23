package by.madcat.development.f1newsreader.Models;

public enum RacersDataModel {
    VET ("Феттель"),
    VES ("Ферстаппен"),
    RIC ("Риккардо"),
    RAI ("Райкконен"),
    HAM ("Хэмилтон"),
    BOT ("Боттас"),
    HUL ("Хюлкенберг"),
    ALO ("Алонсо"),
    VAN ("Вандорн"),
    SAI ("Сайнс"),
    PAL ("Палмер"),
    PER ("Перес"),
    KVY ("Квят"),
    OCO ("Окон"),
    GRO ("Грожан"),
    MAG ("Магнуссен"),
    MAS ("Масса"),
    STR ("Стролл"),
    VEH ("Верляйн"),
    ERI ("Эриксон");

    private final String name;

    RacersDataModel(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

    public static RacersDataModel find(String other){
        for(RacersDataModel racersDataModel : RacersDataModel.values())
            if(racersDataModel.equalsName(other))
                return racersDataModel;

        return null;
    }

    public static String getRacerAbr(String racerName){
        String racerAbr = "";

        switch (racerName){
            case "Феттель":
                racerAbr = "VET";
                break;
            case "Ферстаппен":
                racerAbr = "VES";
                break;
            case "Риккардо":
                racerAbr = "RIC";
                break;
            case "Райкконен":
                racerAbr = "RAI";
                break;
            case "Хэмилтон":
                racerAbr = "HAM";
                break;
            case "Боттас":
                racerAbr = "BOT";
                break;
            case "Хюлкенберг":
                racerAbr = "HUL";
                break;
            case "Алонсо":
                racerAbr = "ALO";
                break;
            case "Вандорн":
                racerAbr = "VAN";
                break;
            case "Сайнс":
                racerAbr = "SAI";
                break;
            case "Палмер":
                racerAbr = "PAL";
                break;
            case "Перес":
                racerAbr = "PER";
                break;
            case "Квят":
                racerAbr = "KVY";
                break;
            case "Окон":
                racerAbr = "OCO";
                break;
            case "Грожан":
                racerAbr = "GRO";
                break;
            case "Магнуссен":
                racerAbr = "MAG";
                break;
            case "Масса":
                racerAbr = "MAS";
                break;
            case "Стролл":
                racerAbr = "STR";
                break;
            case "Верляйн":
                racerAbr = "VEH";
                break;
            case "Эриксон":
                racerAbr = "ERI";
                break;
        }

        return racerAbr;
    }
}
