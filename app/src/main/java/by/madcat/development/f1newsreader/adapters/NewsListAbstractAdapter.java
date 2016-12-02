package by.madcat.development.f1newsreader.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

public abstract class NewsListAbstractAdapter extends RecyclerView.Adapter<ViewHolder>{
    public interface ClickListener{
        void onClick(int positionID);
    }

    public abstract void swapCursor(Cursor cursor);
}
