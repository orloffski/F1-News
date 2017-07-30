package by.madcat.development.f1newsreader.dataInet.Models;

import java.util.HashMap;

public class HelmetsDataModel {

    private HashMap<String, String> helmets;
    private static HelmetsDataModel instance;

    private HelmetsDataModel() {
        this.helmets = createHelmetsData();
    }

    public static HelmetsDataModel getInstance(){
        if(instance == null)
            instance = new HelmetsDataModel();

        return instance;
    }

    public HashMap<String, String> getHelmets() {
        return helmets;
    }

    private HashMap<String, String> createHelmetsData(){
        HashMap<String, String> helmets = new HashMap<>();

        helmets.put("Алонсо", "helmets_alonso.jpg");
        helmets.put("Боттас", "helmets_bottas.jpg");
        helmets.put("Эриксон", "helmets_ericsson.jpg");
        helmets.put("Джовинацци", "helmets_giovinazzi.jpg");
        helmets.put("Грожан", "helmets_grojean.jpg");
        helmets.put("Хэмилтон", "helmets_hamilton.jpg");
        helmets.put("Хюлкенберг", "helmets_hulkenberg.jpg");
        helmets.put("Магнуссен", "helmets_magnussen.jpg");
        helmets.put("Масса", "helmets_massa.jpg");
        helmets.put("Окон", "helmets_ocon.jpg");
        helmets.put("Палмер", "helmets_palmer.jpg");
        helmets.put("Перес", "helmets_perez.jpg");
        helmets.put("Риккардо", "helmets_ricciardo.jpg");
        helmets.put("Сайнс", "helmets_sainz.jpg");
        helmets.put("Сироткин", "helmets_sirotkin.jpg");
        helmets.put("Стролл", "helmets_stroll.jpg");
        helmets.put("Вандорн", "helmets_vandorne.jpg");
        helmets.put("Ферстаппен", "helmets_verstappen.jpg");
        helmets.put("Феттель", "helmets_vellet.jpg");
        helmets.put("Верляйн", "helmets_wehrlein.jpg");

        return helmets;
    }
}
