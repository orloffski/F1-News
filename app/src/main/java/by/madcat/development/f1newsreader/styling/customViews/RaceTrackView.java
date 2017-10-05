package by.madcat.development.f1newsreader.styling.customViews;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.models.TracksPoints;
import by.madcat.development.f1newsreader.utils.PreferencesUtils;
import by.madcat.development.f1newsreader.models.TimingElement;
import by.madcat.development.f1newsreader.styling.drawingThread.RacerDrawOnLap;

public class RaceTrackView extends SurfaceView implements SurfaceHolder.Callback {

    private RacerDrawOnLap racerDrawOnLap;

    private int screenWidth;
    private int screenHeight;

    private float[][] mapTrack;

    public RaceTrackView(Context context) {
        super(context);

        init();
    }

    public RaceTrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public RaceTrackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mapTrack = TracksPoints.getTrackPoints(PreferencesUtils.getNextGpCountry(getContext()));

        racerDrawOnLap = new RacerDrawOnLap(getHolder(), getContext(), screenWidth, screenHeight, mapTrack);
        racerDrawOnLap.setRunning(true);
        racerDrawOnLap.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        // завершаем работу потока
        racerDrawOnLap.setRunning(false);
        while (retry) {
            try {
                racerDrawOnLap.join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
    }

    private void init(){
        getHolder().addCallback(this);
        getWindowSize(getContext());
    }


    private void getWindowSize(Context context){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
    }

    public void updateRaceData(LinkedList<TimingElement> timings){
        racerDrawOnLap.setRaceData(timings);
    }
}