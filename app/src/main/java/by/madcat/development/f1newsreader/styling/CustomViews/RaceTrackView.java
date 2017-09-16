package by.madcat.development.f1newsreader.styling.CustomViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.styling.drawingThread.RacerDrawOnLap;

public class RaceTrackView extends SurfaceView implements SurfaceHolder.Callback {

    private RacerDrawOnLap racerDrawOnLap;

    private int screenWidth;
    private int screenHeight;

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
        int[] timers = new int[]{60, 120, 240};

        racerDrawOnLap = new RacerDrawOnLap(getHolder(), screenWidth, screenHeight,
                BitmapFactory.decodeResource(getResources(), R.drawable.dot), timers);
        racerDrawOnLap.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

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
}