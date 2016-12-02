package by.madcat.development.f1newsreader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import by.madcat.development.f1newsreader.R;

public class ViewHolder extends RecyclerView.ViewHolder{
    public final TextView title;
    public final ImageView thumbnail;
    private long rowID;
    private NewsListAbstractAdapter.ClickListener clickListener;

    public ViewHolder(View itemView, final NewsListAbstractAdapter.ClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;

        title = (TextView) itemView.findViewById(R.id.title);
        thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick((int)rowID);
            }
        });

    }

    public void setRowID(long id){
        this.rowID = id;
    }
}
