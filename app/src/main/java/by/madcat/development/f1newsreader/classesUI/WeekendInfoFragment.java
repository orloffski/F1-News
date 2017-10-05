package by.madcat.development.f1newsreader.classesUI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Map;

import by.madcat.development.f1newsreader.models.TracksDataModel;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.utils.PreferencesUtils;
import by.madcat.development.f1newsreader.styling.customViews.PerfogramaTextView;
import by.madcat.development.f1newsreader.styling.customViews.RobotoRegularTextView;

public class WeekendInfoFragment extends Fragment {

    private RobotoRegularTextView weekendTitle;
    private PerfogramaTextView firstDayTitle;
    private RobotoRegularTextView firstDayText;
    private PerfogramaTextView secondDayTitle;
    private RobotoRegularTextView secondDayText;
    private PerfogramaTextView thirdDayTitle;
    private RobotoRegularTextView thirdDayText;

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

        weekendTitle = (RobotoRegularTextView) view.findViewById(R.id.weekend_title);
        firstDayTitle = (PerfogramaTextView) view.findViewById(R.id.first_day_title);
        firstDayText = (RobotoRegularTextView) view.findViewById(R.id.first_day_text);
        secondDayTitle = (PerfogramaTextView) view.findViewById(R.id.second_day_title);
        secondDayText = (RobotoRegularTextView) view.findViewById(R.id.second_day_text);
        thirdDayTitle = (PerfogramaTextView) view.findViewById(R.id.third_day_title);
        thirdDayText = (RobotoRegularTextView) view.findViewById(R.id.third_day_text);

        weekendTrack = (ImageView) view.findViewById(R.id.weekend_track);

        loadWeekendTableData(getContext());

        return view;
    }

    private void loadWeekendTableData(Context context){
        String weekendTitleString = PreferencesUtils.getWeekendTitle(context);
        weekendTitle.setText(weekendTitleString);

        String fullPathImage = "file:///android_asset/tracks_maps/" +
                TracksDataModel.getTrackName(weekendTitleString) +
                ".webp";
        Glide.with(context).load(fullPathImage).into(weekendTrack);

        Map<String, String> weekendData = PreferencesUtils.getWeekendData(context);

        int counter = 1;
        for(Map.Entry entry : weekendData.entrySet()){
            switch (counter){
                case 1:
                    firstDayTitle.setText(entry.getKey().toString());
                    firstDayText.setText(entry.getValue().toString());
                    break;
                case 2:
                    secondDayTitle.setText(entry.getKey().toString());
                    secondDayText.setText(entry.getValue().toString());
                    break;
                case 3:
                    thirdDayTitle.setText(entry.getKey().toString());
                    thirdDayText.setText(entry.getValue().toString());
                    break;
            }

            counter++;
        }
    }
}
