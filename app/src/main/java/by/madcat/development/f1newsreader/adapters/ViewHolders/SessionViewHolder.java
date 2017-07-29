package by.madcat.development.f1newsreader.adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import by.madcat.development.f1newsreader.R;

public class SessionViewHolder extends RecyclerView.ViewHolder{

    public final TextView name;
    public final TextView position;
    public final TextView gap;
    public final TextView bestLap;
    public final TextView pits;
    public final TextView lastLap;

    public SessionViewHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.name);
        position = (TextView) itemView.findViewById(R.id.position);
        gap = (TextView) itemView.findViewById(R.id.gap);
        bestLap = (TextView) itemView.findViewById(R.id.best_lap);
        pits = (TextView) itemView.findViewById(R.id.pits);
        lastLap = (TextView) itemView.findViewById(R.id.last_lap);
    }
}
