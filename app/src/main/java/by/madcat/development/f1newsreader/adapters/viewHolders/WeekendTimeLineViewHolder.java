package by.madcat.development.f1newsreader.adapters.viewHolders;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.vipulasri.timelineview.TimelineView;

import by.madcat.development.f1newsreader.R;

public class WeekendTimeLineViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView itemTextDate;
    public AppCompatTextView itemTextTitle;
    public TimelineView itemMarker;

    public WeekendTimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

        itemTextDate = (AppCompatTextView) itemView.findViewById(R.id.item_text_date);
        itemTextTitle = (AppCompatTextView) itemView.findViewById(R.id.item_text_title);
        itemMarker = (TimelineView) itemView.findViewById(R.id.item_marker);

        itemMarker.initLine(viewType);
    }
}