package by.madcat.development.f1newsreader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.adapters.ViewHolders.SessionViewHolder;
import by.madcat.development.f1newsreader.dataInet.Models.TimingElement;

public class OnlineSessionAdapter extends RecyclerView.Adapter<SessionViewHolder>{

    private LinkedList<TimingElement> timings;

    public OnlineSessionAdapter(LinkedList<TimingElement> timings) {
        this.timings = timings;
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
        holder.gap.setText(element.getGap().equals("-")?element.getGap():"+" + element.getGap());
        holder.bestLap.setText(element.getBestLap());
        holder.pits.setText(element.getPits());
        holder.lastLap.setText(String.valueOf(""));
    }

    @Override
    public int getItemCount() {
        return timings.size();
    }
}
