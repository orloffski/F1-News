package by.madcat.development.f1newsreader.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.Models.RacersDataModel;
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
                .load(Uri.parse(RacersDataModel.getHelmetFromAssets(element.getName())))
                .placeholder(R.drawable.helmet_default)
                .into(holder.helmet);
    }

    @Override
    public int getItemCount() {
        return timings.size();
    }
}
