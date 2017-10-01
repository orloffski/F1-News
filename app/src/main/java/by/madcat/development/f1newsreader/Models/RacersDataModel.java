package by.madcat.development.f1newsreader.Models;

public enum RacersDataModel {
    VET ("Феттель"),
    VER ("Ферстаппен"),
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
    WEH ("Верляйн"),
    ERI ("Эриксон"),
    GAS("Гасли");

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
            case "Гасли":
                racerAbr = "GAS";
                break;
        }

        return racerAbr;
    }

    public static String getHelmetFromAssets(String driverName){
        String helmet = "file:///android_asset/helmets/";

        switch (driverName){
            case "Алонсо":
                helmet += "alonso.webp";
                break;
            case "Боттас":
                helmet += "bottas.webp";
                break;
            case"Эриксон":
                helmet += "ericsson.webp";
                break;
            case "Джовинацци":
                helmet += "giovinazzi.webp";
                break;
            case "Грожан":
                helmet += "grojean.webp";
                break;
            case "Хэмилтон":
                helmet += "hamilton.webp";
                break;
            case "Хюлкенберг":
                helmet += "hulkenberg.webp";
                break;
            case "Квят":
                helmet += "kvyat.webp";
                break;
            case "Магнуссен":
                helmet += "magnussen.webp";
                break;
            case "Масса":
                helmet += "massa.webp";
                break;
            case "Окон":
                helmet += "ocon.webp";
                break;
            case "Палмер":
                helmet += "palmer.webp";
                break;
            case "Перес":
                helmet += "perez.webp";
                break;
            case "Райкконен":
                helmet += "raikkonen.webp";
                break;
            case "Риккардо":
                helmet += "ricciardo.webp";
                break;
            case "Сайнс":
                helmet += "sainz.webp";
                break;
            case "Сироткин":
                helmet += "sirotkin.webp";
                break;
            case "Стролл":
                helmet += "stroll.webp";
                break;
            case "Вандорн":
                helmet += "vandorne.webp";
                break;
            case "Ферстаппен":
                helmet += "verstappen.webp";
                break;
            case "Феттель":
                helmet += "vettel.webp";
                break;
            case "Верляйн":
                helmet += "wehrlein.webp";
                break;
            case "ди Реста":
                helmet += "diResta.webp";
                break;
            case "Гасли":
                helmet += "gasly.webp";
                break;
            default:
                helmet += "helmets/default.webp";
        }

        return helmet;
    }
}
