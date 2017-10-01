package by.madcat.development.f1newsreader.classesUI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Models.RaceMode;
import by.madcat.development.f1newsreader.styling.CustomViews.SpeedwayTextView;

public class SessionStatusFragment extends Fragment {

    private ImageView flag;
    private ImageView trackTempImage;
    private ImageView airTempImage;
    private TextView trackTempText;
    private TextView airTempText;
    private SpeedwayTextView raceModeSymbol;
    private TextView lapsText;

    private RaceMode raceMode;


    public SessionStatusFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_status, container, false);

        flag = (ImageView) view.findViewById(R.id.flag);
        trackTempImage = (ImageView) view.findViewById(R.id.trackTempImage);
        airTempImage = (ImageView) view.findViewById(R.id.airTempImage);
        trackTempText = (TextView) view.findViewById(R.id.trackTempText);
        airTempText = (TextView) view.findViewById(R.id.airTempText);
        raceModeSymbol = (SpeedwayTextView) view.findViewById(R.id.race_mode_symbol);
        lapsText = (TextView) view.findViewById(R.id.lapsText);

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

        Glide.with(getContext()).load(Uri.parse("file:///android_asset/sessionImages/track.png")).into(trackTempImage);
        trackTempText.setText(String.valueOf(raceMode.getTrackTemp()) + getString(R.string.temperature_celsium));

        Glide.with(getContext()).load(Uri.parse("file:///android_asset/sessionImages/cloud.png")).into(airTempImage);
        airTempText.setText(String.valueOf(raceMode.getAirTemp()) + getString(R.string.temperature_celsium));

        if(raceMode.getMode().equals("race")) {
            lapsText.setText(raceMode.getCurrentLap() + "/" + String.valueOf(raceMode.getTotalLaps()));
            raceModeSymbol.setText("R");
        }else {
            lapsText.setText("");
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
