package by.madcat.development.f1newsreader.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.File;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.data.DatabaseDescription.News;
import by.madcat.development.f1newsreader.dataInet.LoadNewsTask;

public class NewsCardsAdapter extends NewsListAbstractAdapter {

    private Cursor cursor;
    private final NewsListAbstractAdapter.ClickListener clickListener;
    private Context context;

    public NewsCardsAdapter(ClickListener clickListener, Context context) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cards_card, parent, false);

        return new ViewHolder(view, clickListener);
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

    public void swapCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
