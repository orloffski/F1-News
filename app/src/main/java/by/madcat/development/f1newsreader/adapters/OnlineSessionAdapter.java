package by.madcat.development.f1newsreader.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.adapters.ViewHolders.SessionViewHolder;
import by.madcat.development.f1newsreader.dataInet.Models.RaceMode;
import by.madcat.development.f1newsreader.dataInet.Models.TimingElement;

public class OnlineSessionAdapter extends RecyclerView.Adapter<SessionViewHolder>{

    private LinkedList<TimingElement> timings;
    private Context context;

    public OnlineSessionAdapter(LinkedList<TimingElement> timings, Context context) {
        this.timings = timings;
        this.context = context;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_timing_element, parent, false);

        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        TimingElement element = timings.get(position);

        holder.name.setText(element.getName());
        holder.position.setText(String.valueOf(element.getPosition()));

        RaceMode mode = RaceMode.getInstance();

        // leader gap
        if(mode.getMode().equals("race")){
            holder.bestLap_gap.setText(element.getGap().equals("")?element.getGap():"+" + element.getGap());
            holder.interval_pits.setText("pits: " + element.getPits());
        }else if(mode.getMode().equals("practice")){
            holder.bestLap_gap.setText(element.getLastLap());
            holder.interval_pits.setText(element.getGap().equals("-")?"":"+" + element.getGap());
        }

        Glide.with(context)
                .load(Uri.parse(getHelmetFromAssets(element.getName())))
                .placeholder(R.drawable.helmet_default)
                .into(holder.helmet);
    }

    @Override
    public int getItemCount() {
        return timings.size();
    }

    private String getHelmetFromAssets(String driverName){
        String helmet = "file:///android_asset/";

        switch (driverName){
            case "Алонсо":
                helmet += "helmets/alonso.webp";
                break;
            case "Боттас":
                helmet += "helmets/bottas.webp";
                break;
            case"Эриксон":
                helmet += "helmets/ericsson.webp";
                break;
            case "Джовинацци":
                helmet += "helmets/giovinazzi.webp";
                break;
            case "Грожан":
                helmet += "helmets/grojean.webp";
                break;
            case "Хэмилтон":
                helmet += "helmets/hamilton.webp";
                break;
            case "Хюлкенберг":
                helmet += "helmets/hulkenberg.webp";
                break;
            case "Квят":
                helmet += "helmets/kvyat.webp";
                break;
            case "Магнуссен":
                helmet += "helmets/magnussen.webp";
                break;
            case "Масса":
                helmet += "helmets/massa.webp";
                break;
            case "Окон":
                helmet += "helmets/ocon.webp";
                break;
            case "Палмер":
                helmet += "helmets/palmer.webp";
                break;
            case "Перес":
                helmet += "helmets/perez.webp";
                break;
            case "Райкконен":
                helmet += "helmets/raikkonen.webp";
                break;
            case "Риккардо":
                helmet += "helmets/ricciardo.webp";
                break;
            case "Сайнс":
                helmet += "helmets/sainz.webp";
                break;
            case "Сироткин":
                helmet += "helmets/sirotkin.webp";
                break;
            case "Стролл":
                helmet += "helmets/stroll.webp";
                break;
            case "Вандорн":
                helmet += "helmets/vandorne.webp";
                break;
            case "Ферстаппен":
                helmet += "helmets/verstappen.webp";
                break;
            case "Феттель":
                helmet += "helmets/vettel.webp";
                break;
            case "Верляйн":
                helmet += "helmets/wehrlein.webp";
                break;
            case "ди Реста":
                helmet += "helmets/diResta.webp";
                break;
            default:
                helmet += "helmets/default.webp";
        }

        return helmet;
    }
}
