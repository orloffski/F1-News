package by.madcat.development.f1newsreader.classesUI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.dataInet.Models.RaceMode;

public class SessionStatusFragment extends Fragment {

    private ImageView flag;
    private TextView trackTempData;
    private TextView airTempData;
    private TextView lapsData;

    private RaceMode raceMode;


    public SessionStatusFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_status, container, false);

        flag = (ImageView) view.findViewById(R.id.flag);
        trackTempData = (TextView) view.findViewById(R.id.track_temp_data);
        airTempData = (TextView) view.findViewById(R.id.air_temp_data);
        lapsData = (TextView) view.findViewById(R.id.laps_data);

        return view;
    }

    public void updateRace(RaceMode raceMode){
        this.raceMode = raceMode;
        updateSessionDataView(raceMode);
    }

    private void updateSessionDataView(RaceMode raceMode){
        if(raceMode == null)
            return;

        Glide.with(getContext()).load(getFlagFromDrawable(raceMode.getFlag())).placeholder(R.drawable.flag_green).into(flag);
        trackTempData.setText(String.valueOf(raceMode.getTrackTemp()));
        airTempData.setText(String.valueOf(raceMode.getAirTemp()));
        lapsData.setText(String.valueOf(raceMode.getCurrentLap()) + "/" + String.valueOf(raceMode.getTotalLaps()));
    }

    private int getFlagFromDrawable(String flagColor){
        if(flagColor.equals("green"))
            return R.drawable.flag_green;
        if(flagColor.equals("red"))
            return R.drawable.flag_red;
        if(flagColor.equals("black"))
            return R.drawable.flag_black;

        return R.drawable.flag_green;
    }

}
