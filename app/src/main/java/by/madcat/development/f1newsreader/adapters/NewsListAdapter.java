package by.madcat.development.f1newsreader.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.data.DatabaseDescription.*;
import by.madcat.development.f1newsreader.dataInet.LoadNewsTask;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder>{

    public interface ClickListener{
        void onClick(int positionID);
    }

    private Cursor cursor;
    private final ClickListener clickListener;
    private Context context;

    public NewsListAdapter(ClickListener clickListener, Context context){
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.setRowID(position);
        holder.title.setText(cursor.getString(cursor.getColumnIndex(News.COLUMN_TITLE)));

        String pathToImage = context.getFilesDir() +
                "/" + LoadNewsTask.IMAGE_PATH + "/" + cursor.getString(cursor.getColumnIndex(News.COLUMN_IMAGE));
        Glide.with(context).load(new File(pathToImage)).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView title;
        public final ImageView thumbnail;
        private long rowID;

        public ViewHolder(View itemView) {
            super(itemView);
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

    public void swapCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
