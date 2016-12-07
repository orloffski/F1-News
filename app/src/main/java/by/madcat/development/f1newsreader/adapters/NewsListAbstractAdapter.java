package by.madcat.development.f1newsreader.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;

import by.madcat.development.f1newsreader.Utils.DateUtils;
import by.madcat.development.f1newsreader.data.DatabaseDescription;
import by.madcat.development.f1newsreader.dataInet.LoadNewsTask;

public abstract class NewsListAbstractAdapter extends RecyclerView.Adapter<ViewHolder>{
    public interface ClickListener{
        void onClick(int positionID);
    }

    protected Cursor cursor;
    protected Context context;

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.setRowID(position);
        holder.title.setText(cursor.getString(cursor.getColumnIndex(DatabaseDescription.News.COLUMN_TITLE)));
        holder.date.setText(DateUtils.untransformDateTime(cursor.getString(cursor.getColumnIndex(DatabaseDescription.News.COLUMN_DATE))));

        String pathToImage = context.getFilesDir() +
                "/" + LoadNewsTask.IMAGE_PATH + "/" + cursor.getString(cursor.getColumnIndex(DatabaseDescription.News.COLUMN_IMAGE));
        Glide.with(context).load(new File(pathToImage)).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
