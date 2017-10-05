package by.madcat.development.f1newsreader.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.adapters.viewHolders.WeekendTimeLineViewHolder;
import by.madcat.development.f1newsreader.models.WeekendItemStatus;
import by.madcat.development.f1newsreader.models.WeekendTimeLineModel;
import by.madcat.development.f1newsreader.utils.VectorDrawableUtils;

public class WeekendTimeLineAdapter extends RecyclerView.Adapter<WeekendTimeLineViewHolder> {

    private List<WeekendTimeLineModel> mFeedList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public WeekendTimeLineAdapter(List<WeekendTimeLineModel> feedList) {
        mFeedList = feedList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public WeekendTimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

        view = mLayoutInflater.inflate(R.layout.weekend_item, parent, false);

        return new WeekendTimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(WeekendTimeLineViewHolder holder, int position) {

        WeekendTimeLineModel timeLineModel = mFeedList.get(position);

        if(timeLineModel.getStatus() == WeekendItemStatus.INACTIVE) {
            holder.itemMarker.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.getStatus() == WeekendItemStatus.ACTIVE) {
            holder.itemMarker.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else {
            holder.itemMarker.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
        }

        if(!timeLineModel.getDate().isEmpty()) {
            holder.itemTextDate.setVisibility(View.VISIBLE);
            holder.itemTextDate.setText(timeLineModel.getDate());
        }
        else
            holder.itemTextDate.setVisibility(View.GONE);

        holder.itemTextTitle.setText(timeLineModel.getMessage());
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

}