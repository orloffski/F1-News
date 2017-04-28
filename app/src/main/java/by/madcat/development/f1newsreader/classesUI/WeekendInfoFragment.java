package by.madcat.development.f1newsreader.classesUI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Map;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.dataInet.LoadNewsTask;

public class WeekendInfoFragment extends Fragment {

    private ImageView weekendImage;
    private TextView weekendTitle;
    private TextView firstDayTitle;
    private TextView firstDayText;
    private TextView secondDayTitle;
    private TextView secondDayText;
    private TextView thirdDayTitle;
    private TextView thirdDayText;

    public WeekendInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_weekend_info, container, false);

        weekendImage = (ImageView) view.findViewById(R.id.weekendImage);
        weekendTitle = (TextView) view.findViewById(R.id.weekendTitle);
        firstDayTitle = (TextView) view.findViewById(R.id.first_day_title);
        firstDayText = (TextView) view.findViewById(R.id.first_day_text);
        secondDayTitle = (TextView) view.findViewById(R.id.second_day_title);
        secondDayText = (TextView) view.findViewById(R.id.second_day_text);
        thirdDayTitle = (TextView) view.findViewById(R.id.third_day_title);
        thirdDayText = (TextView) view.findViewById(R.id.third_day_text);

        loadWeekendTableData(getContext());

        return view;
    }

    private void loadWeekendTableData(Context context){
        String weekendImageName = SystemUtils.getWeekendImage(context);
        String fullPathImage = context.getFilesDir() + "/" + LoadNewsTask.IMAGE_PATH + "/" + weekendImageName;
        Glide.with(context).load(fullPathImage).into(weekendImage);

        String weekendTitleString = SystemUtils.getWeekendTitle(context);
        weekendTitle.setText(weekendTitleString);

        Map<String, String> weekendData = SystemUtils.getWeekendData(context);

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
