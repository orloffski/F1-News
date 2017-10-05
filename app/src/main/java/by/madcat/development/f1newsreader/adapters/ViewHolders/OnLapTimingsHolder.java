package by.madcat.development.f1newsreader.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import by.madcat.development.f1newsreader.R;

public class OnLapTimingsHolder extends RecyclerView.ViewHolder{

    public TextView position;
    public TextView abr;
    public TextView interval;

    public OnLapTimingsHolder(View itemView) {
        super(itemView);

        position = (TextView) itemView.findViewById(R.id.onlap_position);
        abr = (TextView) itemView.findViewById(R.id.onlap_abr);
        interval = (TextView) itemView.findViewById(R.id.onlap_interval);
    }
}
