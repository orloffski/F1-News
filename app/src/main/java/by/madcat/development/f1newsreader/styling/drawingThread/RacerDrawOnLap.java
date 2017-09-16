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
import java.util.List;

import by.madcat.development.f1newsreader.R;

public class RacerDrawOnLap extends Thread {

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

    int[] time;  // test time to lap
    int[] nowTime;  // test timer

    private PathMeasure pm;
    private float pathLenght;
    private float segmentLenght[];

    public RacerDrawOnLap(SurfaceHolder surfaceHolder, Context context, int width, int height, int[] timers, float[][] mapTrack) {

        time = timers;
        segmentLenght = new float[timers.length];
        nowTime = new int[timers.length];

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
        for(int i = 0; i < time.length; i++) {
            segmentLenght[i] = pathLenght / time[i];
            nowTime[i] = 0;
        }
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        Matrix mxTransform = new Matrix();

        try {
            synchronized (surfaceHolder) {

                while(runFlag){

                    canvas = surfaceHolder.lockCanvas(null);

                    canvas.drawColor(Color.WHITE);
                    canvas.drawPath(ptCurve, paint);

                    for(int i = 0; i < time.length; i++){
                        if(nowTime[i] < time[i]) {
                            View racerView = getRacerView();
                            setRacerName(racerView, String.valueOf(i));

                            canvas.save();
                            pm.getMatrix(segmentLenght[i] * nowTime[i], mxTransform,
                                    PathMeasure.POSITION_MATRIX_FLAG);
                            mxTransform.preTranslate(-racerView.getWidth(), -racerView.getHeight());
                            canvas.setMatrix(mxTransform);
                            racerView.draw(canvas);
                            canvas.restore();

                            nowTime[i]++; //advance to the next step
                        }else{
                            nowTime[i] = 0;
                        }
                    }
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }

                    Thread.sleep(100);
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
        name.setText(racerName);
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
}
