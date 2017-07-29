package by.madcat.development.f1newsreader.adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.adapters.NewsListAbstractAdapter;

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public final TextView title;
    public final TextView date;
    public final ImageView thumbnail;
    private long rowID;
    private NewsListAbstractAdapter.ClickListener clickListener;

    public NewsViewHolder(View itemView, final NewsListAbstractAdapter.ClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;

        date = (TextView) itemView.findViewById(R.id.newsDate);
        title = (TextView) itemView.findViewById(R.id.title);
        thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

        thumbnail.setOnClickListener(this);
        itemView.setOnClickListener(this);

    }

    public void setRowID(long id){
        this.rowID = id;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick((int)rowID);
    }
}
