package by.madcat.development.f1newsreader.adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import by.madcat.development.f1newsreader.R;

public class SessionViewHolder extends RecyclerView.ViewHolder{

    public final TextView name;
    public final TextView position;
    public final TextView gapText;
    public final TextView pitsText;
    public final TextView bestLapText;
    public final TextView lastLapText;
    public final TextView gap;
    public final TextView pits;
    public final TextView bestLap;
    public final TextView lastLap;
    public final ImageView helmet;

    public SessionViewHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.name);
        position = (TextView) itemView.findViewById(R.id.position);
        gap = (TextView) itemView.findViewById(R.id.gap);
        pits = (TextView) itemView.findViewById(R.id.pits);
        bestLap = (TextView) itemView.findViewById(R.id.best_lap);
        lastLap = (TextView) itemView.findViewById(R.id.last_lap);
        gapText = (TextView) itemView.findViewById(R.id.gap_text);
        pitsText = (TextView) itemView.findViewById(R.id.pits_text);
        bestLapText = (TextView) itemView.findViewById(R.id.best_lap_text);
        lastLapText = (TextView) itemView.findViewById(R.id.last_lap_text);
        helmet = (ImageView) itemView.findViewById(R.id.helmet);
    }
}
