package by.madcat.development.f1newsreader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import by.madcat.development.f1newsreader.R;

public class OnlineViewHolder extends RecyclerView.ViewHolder{

    public final WebView post;
    public final TextView time;

    public OnlineViewHolder(View itemView) {
        super(itemView);

        time = (TextView) itemView.findViewById(R.id.online_post_time);
        post = (WebView) itemView.findViewById(R.id.online_post_web_view);
    }
}
