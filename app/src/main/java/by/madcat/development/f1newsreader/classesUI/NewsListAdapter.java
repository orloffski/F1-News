package by.madcat.development.f1newsreader.classesUI;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.madcat.development.f1newsreader.data.DatabaseDescription.*;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder>{

    public interface ClickListener{
        void onClick(int positionID);
    }

    private Cursor cursor;
    private final ClickListener clickListener;

    public NewsListAdapter(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.setRowID(position);
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(News.COLUMN_TITLE)));
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
