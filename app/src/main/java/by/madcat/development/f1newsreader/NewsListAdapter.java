package by.madcat.development.f1newsreader;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

/**
 * Created by orlof on 04.11.2016.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder>{

    public interface ClickListener{
        void onClick(int sectionID, int positionID);
    }

    private Cursor cursor;
    private final ClickListener clickListener;
    private int sectionID;

    public NewsListAdapter(ClickListener clickListener, int sectionID){
        this.clickListener = clickListener;
        this.sectionID = sectionID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        switch (sectionID){
            case R.id.nav_news:
                holder.setRowID(cursor.getLong(cursor.getColumnIndex(News._ID)));
                holder.textView.setText(cursor.getString(cursor.getColumnIndex(News.COLUMN_DATE)));
                break;
            case R.id.nav_memuar:
                holder.setRowID(cursor.getLong(cursor.getColumnIndex(Memuar._ID)));
                holder.textView.setText(cursor.getString(cursor.getColumnIndex(Memuar.COLUMN_DATE)));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView textView;
        private long rowID;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (sectionID){
                        case R.id.nav_news:
                            clickListener.onClick(sectionID, (int)rowID);
                            break;
                        case R.id.nav_memuar:
                            clickListener.onClick(sectionID, (int)rowID);
                            break;
                    }
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
