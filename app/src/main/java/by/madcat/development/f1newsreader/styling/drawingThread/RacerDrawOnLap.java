package by.madcat.development.f1newsreader.styling.drawingThread;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import by.madcat.development.f1newsreader.Models.RaceDataModel;
import by.madcat.development.f1newsreader.Models.RacersDataModel;
import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.StringUtils;
import by.madcat.development.f1newsreader.dataInet.Models.TimingElement;

public class RacerDrawOnLap extends Thread {

    private int fps = 15;

    private boolean runFlag = false;

    private SurfaceHolder surfaceHolder;
    private Context context;

    private Paint paint;
    private Path ptCurve = new Path(); //curve

    private int screenWidth;
    private int screenHeight;
    private float scalingFactor;

    private float[][] pointsMap;
    List<PointF> aPoints;

    private PathMeasure pm;
    private float pathLenght;

    private HashMap<RacersDataModel, RaceDataModel> raceData, tmpRaceData;
    private RacersDataModel[] racers = RacersDataModel.values();

    public RacerDrawOnLap(SurfaceHolder surfaceHolder, Context context, int width, int height, float[][] mapTrack) {
        pointsMap = mapTrack;

        screenWidth = width;
        screenHeight = height;

        this.surfaceHolder = surfaceHolder;
        this.context = context;

        getScalingFactor(getMaxX(), getMaxY());

        aPoints = new ArrayList<>();

        getPoints(aPoints);

        initCurve();
        getPaint();

        pm = new PathMeasure(ptCurve, false);
        pathLenght = pm.getLength();
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        Matrix mxTransform = new Matrix();

        canvas = surfaceHolder.lockCanvas(null);
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(ptCurve, paint);
        if (canvas != null) {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

        try {
            synchronized (surfaceHolder) {

                while(runFlag){
                    long timestamp = System.nanoTime();

                    canvas = surfaceHolder.lockCanvas(null);

                    if(canvas != null) {
                        canvas.drawColor(Color.WHITE);
                        canvas.drawPath(ptCurve, paint);
                    }else {
                        Thread.sleep(10);
                        continue;
                    }

                    for(int i = 0; i < racers.length; i++) {
                        if (raceData == null) {
                            Thread.sleep(10);
                            continue;
                        }

                            if (raceData.get(racers[i]) != null) {
                                RaceDataModel tmpModel = raceData.get(racers[i]);

                                if (tmpModel.getTimer() < tmpModel.getSeconds()) {
                                    View racerView = getRacerView();
                                    setRacerName(racerView, String.valueOf(racers[i]));

                                    canvas.save();
                                    pm.getMatrix(tmpModel.getSegmentLenght() * tmpModel.getTimer(), mxTransform,
                                            PathMeasure.POSITION_MATRIX_FLAG);
                                    mxTransform.preTranslate(-racerView.getWidth(), -racerView.getHeight());
                                    canvas.setMatrix(mxTransform);
                                    racerView.draw(canvas);
                                    canvas.restore();

                                    tmpModel.setTimer(tmpModel.getTimer() + 1);
                                } else {
                                    timestamp = System.nanoTime();
                                    tmpModel.setTimer(0);

                                    if (raceData.get(racers[i]).isToDelete()) {
                                        tmpModel.setSeconds(0);
                                        tmpModel.setSegmentLenght(0);
                                    } else {
                                        tmpModel.setSeconds(tmpRaceData.get(racers[i]).getSeconds());
                                        tmpModel.setSegmentLenght(pathLenght / tmpModel.getSeconds());
                                    }
                                }

                                raceData.put(racers[i], tmpModel);
                            }
                    }
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }

                    long interval = System.nanoTime() - timestamp;
                    if((1000/fps)*1000000 > interval){
                        interval = (1000/fps)*1000000 - interval;
                        long millis = interval / 999999;
                        int nanos = (int)(interval - millis * 999999);

                        Thread.sleep(millis, nanos);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private View getRacerView(){
        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.racer_view, null);
            v.measure(View.MeasureSpec.getSize(v.getMeasuredWidth()), View.MeasureSpec.getSize(v.getMeasuredHeight()));
        v.layout(10, 10, 10, 10);
        v.setRotation(180);

        return v;
    }

    private void setRacerName(View view, String racerName){
        TextView name = (TextView)view.findViewById(R.id.racerName);
        name.setText(RacersDataModel.getRacerAbr(racerName));
    }

    private float getMaxX(){
        float tmpMax = 0f;

        for (float[] floats : pointsMap) {
            if (floats[0] > tmpMax)
                tmpMax = floats[0];
        }

        return tmpMax;
    }

    private float getMaxY(){
        float tmpMax = 0f;

        for (float[] floats : pointsMap) {
            if (floats[1] > tmpMax)
                tmpMax = floats[1];
        }

        return tmpMax;
    }

    private void getPoints(List<PointF> points){
        float horizontalPadding = (screenWidth - getMaxX() * scalingFactor)/2;
        int topPaddingMap = 50;

        for (float[] aPointsMap : pointsMap) {
            points.add(new PointF(
                    aPointsMap[0] * scalingFactor + horizontalPadding,
                    aPointsMap[1] * scalingFactor + topPaddingMap
            ));
        }
    }

    private void getScalingFactor(float maxX, float maxY){
        scalingFactor = (((float)screenWidth - 50)/maxX > ((float)screenHeight - 300)/maxY)
                ? ((float)screenHeight - 300)/maxY
                : ((float)screenWidth - 50)/maxX;
    }

    private void getPaint(){
        //init paint object
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(13);
        paint.setColor(Color.rgb(0, 148, 255));
    }

    private void initCurve(){
        //init smooth curve
        PointF point = aPoints.get(0);
        ptCurve.moveTo(point.x, point.y);
        for(int i = 0; i < aPoints.size() - 1; i++){
            point = aPoints.get(i);
            PointF next = aPoints.get(i+1);
            ptCurve.quadTo(point.x, point.y, (next.x + point.x) / 2, (point.y + next.y) / 2);
        }
    }

    public void setRaceData(LinkedList<TimingElement> timings) {
        if(timings == null){
            runFlag = false;
            return;
        }

        if (raceData == null) {
            raceData = new LinkedHashMap<>();
            float interval = 0;

            synchronized (raceData) {
                for (TimingElement element : timings) {
                    interval += element.getBestLap().equals("") ? 0 : Float.parseFloat(element.getBestLap());
                    raceData.put(RacersDataModel.find(element.getName()), StringUtils.getRaceDataModel(element, pathLenght, fps, interval));
                }
            }
        } else {
            if(tmpRaceData == null)
                tmpRaceData = new LinkedHashMap<>();

            synchronized (tmpRaceData) {
                for (TimingElement element : timings) {
                    tmpRaceData.put(RacersDataModel.find(element.getName()), StringUtils.getRaceDataModel(element, pathLenght, fps, 0));
                }
            }
        }

    }
}
