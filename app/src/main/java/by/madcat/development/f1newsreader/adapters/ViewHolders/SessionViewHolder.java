package by.madcat.development.f1newsreader.adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import by.madcat.development.f1newsreader.R;

public class SessionViewHolder extends RecyclerView.ViewHolder{

    public final TextView name;
    public final TextView position;
    public final TextView bestLap_gap;
    public final TextView interval_pits;
    public final ImageView helmet;

    public SessionViewHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.name);
        position = (TextView) itemView.findViewById(R.id.position);
        bestLap_gap = (TextView) itemView.findViewById(R.id.bestLap_gap);
        interval_pits = (TextView) itemView.findViewById(R.id.interval_pits);
        helmet = (ImageView) itemView.findViewById(R.id.helmet);
    }
}
