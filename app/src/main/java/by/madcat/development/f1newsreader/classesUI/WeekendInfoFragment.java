package by.madcat.development.f1newsreader.classesUI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import by.madcat.development.f1newsreader.adapters.WeekendTimeLineAdapter;
import by.madcat.development.f1newsreader.models.TracksDataModel;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.models.WeekendItemStatus;
import by.madcat.development.f1newsreader.models.WeekendTimeLineModel;
import by.madcat.development.f1newsreader.utils.PreferencesUtils;
import by.madcat.development.f1newsreader.styling.customViews.PerfogramaTextView;
import by.madcat.development.f1newsreader.styling.customViews.RobotoRegularTextView;

public class WeekendInfoFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private WeekendTimeLineAdapter mTimeLineAdapter;
    private List<WeekendTimeLineModel> mDataList = new ArrayList<>();

    private ImageView weekendTrack;

    public static WeekendInfoFragment newInstance() {
        return new WeekendInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_weekend_info, container, false);

        weekendTrack = (ImageView) view.findViewById(R.id.weekend_track);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.weekend_items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        loadWeekendTableData(getContext());

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        mDataList.clear();
    }

    private void loadWeekendTableData(Context context){
        String weekendTitleString = PreferencesUtils.getWeekendTitle(context);

        String fullPathImage = "file:///android_asset/tracks_maps/" +
                TracksDataModel.getTrackName(weekendTitleString) +
                ".webp";
        Glide.with(context).load(fullPathImage).into(weekendTrack);

        Map<String, String> weekendData = PreferencesUtils.getWeekendData(context);

        for(Map.Entry entry : weekendData.entrySet()){
            mDataList.add(
                    new WeekendTimeLineModel(
                            entry.getValue().toString(),
                            entry.getKey().toString(),
                            WeekendItemStatus.COMPLETED
                    )
            );
        }

        mTimeLineAdapter = new WeekendTimeLineAdapter(mDataList);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }
}
