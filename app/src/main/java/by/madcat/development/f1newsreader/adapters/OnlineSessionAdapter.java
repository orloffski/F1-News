package by.madcat.development.f1newsreader.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.IOException;
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
        holder.gapText.setText(context.getString(R.string.leader_gap_text));
        holder.gap.setText(element.getGap().equals("-")?element.getGap():"+" + element.getGap());
        if(mode.getMode().equals("race")){
            // gap to prev driver
            holder.bestLapText.setText(R.string.gap_text);
            holder.bestLap.setText(element.getBestLap());
            // pits
            holder.pitsText.setText(R.string.pits_text);
            holder.pits.setText(element.getPits());
            // last lap time
            holder.lastLapText.setText(R.string.last_lap_text);
            holder.lastLap.setText(element.getLastLap());
        }else if(mode.getMode().equals("practice")){
            // laps count
            holder.lastLapText.setText(R.string.last_laps);
            holder.lastLap.setText(element.getBestLap());
            // best lap time
            holder.bestLapText.setText(R.string.best_lap_text);
            holder.bestLap.setText(element.getLastLap());
            // pits
            holder.pitsText.setText(R.string.pits_text);
            holder.pits.setText(element.getPits());
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
                helmet += "helmets/alonso.jpg";
                break;
            case "Боттас":
                helmet += "helmets/bottas.jpg";
                break;
            case"Эриксон":
                helmet += "helmets/ericsson.jpg";
                break;
            case "Джовинацци":
                helmet += "helmets/giovinazzi.jpg";
                break;
            case "Грожан":
                helmet += "helmets/grojean.jpg";
                break;
            case "Хэмилтон":
                helmet += "helmets/hamilton.jpg";
                break;
            case "Хюлкенберг":
                helmet += "helmets/hulkenberg.jpg";
                break;
            case "Квят":
                helmet += "helmets/kvyat.jpg";
                break;
            case "Магнуссен":
                helmet += "helmets/magnussen.jpg";
                break;
            case "Масса":
                helmet += "helmets/massa.jpg";
                break;
            case "Окон":
                helmet += "helmets/ocon.jpg";
                break;
            case "Палмер":
                helmet += "helmets/palmer.jpg";
                break;
            case "Перес":
                helmet += "helmets/perez.jpg";
                break;
            case "Райкконен":
                helmet += "helmets/raikkonen.jpg";
                break;
            case "Риккардо":
                helmet += "helmets/ricciardo.jpg";
                break;
            case "Сайнс":
                helmet += "helmets/sainz.jpg";
                break;
            case "Сироткин":
                helmet += "helmets/sirotkin.jpg";
                break;
            case "Стролл":
                helmet += "helmets/stroll.jpg";
                break;
            case "Вандорн":
                helmet += "helmets/vandorne.jpg";
                break;
            case "Ферстаппен":
                helmet += "helmets/verstappen.jpg";
                break;
            case "Феттель":
                helmet += "helmets/vettel.jpg";
                break;
            case "Верляйн":
                helmet += "helmets/wehrlein.jpg";
                break;
            default:
                helmet += "helmets/default.jpg";
        }

        return helmet;
    }
}
