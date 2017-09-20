package by.madcat.development.f1newsreader.Models;

public enum Racers {
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

    Racers(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

    public static Racers find(String other){
        for(Racers racers: Racers.values())
            if(racers.equalsName(other))
                return racers;

        return null;
    }
}
