package by.madcat.development.f1newsreader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.models.RacersDataModel;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.viewHolders.OnLapTimingsHolder;
import by.madcat.development.f1newsreader.models.TimingElement;

public class OnLapTimingsAdapter extends RecyclerView.Adapter<OnLapTimingsHolder>{

    private LinkedList<TimingElement> timings;

    public OnLapTimingsAdapter(LinkedList<TimingElement> timings) {
        this.timings = timings;
    }

    @Override
    public OnLapTimingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_timing_card, parent, false);

        return new OnLapTimingsHolder(view);
    }

    @Override
    public void onBindViewHolder(OnLapTimingsHolder holder, int position) {
        TimingElement element = timings.get(position);

        holder.position.setText(element.getPosition());
        holder.abr.setText(RacersDataModel.getRacerAbr(element.getName()));
        holder.interval.setText(
                element.getBestLap().equals("")
                        ?
                        ""
                        :
                        ("+" + element.getBestLap())
        );
    }

    @Override
    public int getItemCount() {
        return timings.size();
    }
}
