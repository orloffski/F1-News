package by.madcat.development.f1newsreader.classesUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.styling.CustomViews.RaceTrackView;

public class OnLapTranslationFragment extends Fragment{

    private RaceTrackView raceTrackView;

    public static OnLapTranslationFragment newInstance() {
        return new OnLapTranslationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_on_lap_translation, container, false);

        raceTrackView = (RaceTrackView)rootView.findViewById(R.id.map_view);

        return rootView;
    }
}