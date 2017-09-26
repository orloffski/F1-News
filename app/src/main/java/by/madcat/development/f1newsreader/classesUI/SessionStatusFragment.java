package by.madcat.development.f1newsreader.classesUI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.dataInet.Models.RaceMode;
import by.madcat.development.f1newsreader.styling.CustomViews.RobotoRegularTextView;
import by.madcat.development.f1newsreader.styling.CustomViews.SpeedwayTextView;

public class SessionStatusFragment extends Fragment {

    private ImageView flag;
    private RobotoRegularTextView trackTempData;
    private RobotoRegularTextView airTempData;
    private RobotoRegularTextView lapsData;
    private RobotoRegularTextView laps;
    private SpeedwayTextView raceModeSymbol;

    private RaceMode raceMode;


    public SessionStatusFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_status, container, false);

        flag = (ImageView) view.findViewById(R.id.flag);
        trackTempData = (RobotoRegularTextView) view.findViewById(R.id.track_temp_data);
        airTempData = (RobotoRegularTextView) view.findViewById(R.id.air_temp_data);
        lapsData = (RobotoRegularTextView) view.findViewById(R.id.laps_data);
        laps = (RobotoRegularTextView) view.findViewById(R.id.laps);
        raceModeSymbol = (SpeedwayTextView) view.findViewById(R.id.race_mode_symbol);

        return view;
    }

    public void updateRace(RaceMode raceMode){
        this.raceMode = raceMode;
        updateSessionDataView(raceMode);
    }

    private void updateSessionDataView(RaceMode raceMode){
        if(raceMode == null)
            return;

        Glide.with(getContext()).load(Uri.parse(getFlagFromAssets(raceMode.getFlag()))).into(flag);
        trackTempData.setText(String.valueOf(raceMode.getTrackTemp()));
        airTempData.setText(String.valueOf(raceMode.getAirTemp()));
        if(raceMode.getMode().equals("race")) {
            lapsData.setText(String.valueOf(raceMode.getCurrentLap()) + "/" + String.valueOf(raceMode.getTotalLaps()));
            raceModeSymbol.setText("R");
        }else {
            lapsData.setText("");
            laps.setText("");
            raceModeSymbol.setText("Q");
        }

    }

    private String getFlagFromAssets(String flagColor){
        String flagPath = "file:///android_asset/sessionImages/";

        switch (flagColor){
            case "green":
                flagPath += "flag_green.webp";
                break;
            case "red":
                flagPath += "flag_red.webp";
                break;
            case "black":
                flagPath += "flag_black.webp";
                break;
            default:
                flagPath += "flag_green.webp";
        }

        return flagPath;
    }

}
