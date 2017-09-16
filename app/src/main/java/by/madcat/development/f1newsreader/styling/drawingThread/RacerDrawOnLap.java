package by.madcat.development.f1newsreader.styling.drawingThread;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

public class RacerDrawOnLap extends Thread {

    private SurfaceHolder surfaceHolder;

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

    private static Bitmap bmSprite;

    public RacerDrawOnLap(SurfaceHolder surfaceHolder, int width, int height, Bitmap bitmap, int[] timers) {

        time = timers;
        segmentLenght = new float[timers.length];
        nowTime = new int[timers.length];

        if (bmSprite == null)
            bmSprite = bitmap;

        screenWidth = width;
        screenHeight = height;

        this.surfaceHolder = surfaceHolder;

        pointsMap = generatePointsMap();

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

    @Override
    public void run() {
        Canvas canvas = null;
        Matrix mxTransform = new Matrix();

        try {
            synchronized (surfaceHolder) {

                while(true){
                    canvas = surfaceHolder.lockCanvas(null);

                    canvas.drawColor(Color.WHITE);
                    canvas.drawPath(ptCurve, paint);

                    for(int i = 0; i < time.length; i++){
                        if(nowTime[i] < time[i]) {
                            pm.getMatrix(segmentLenght[i] * nowTime[i], mxTransform,
                                    PathMeasure.POSITION_MATRIX_FLAG + PathMeasure.TANGENT_MATRIX_FLAG);
                            mxTransform.preTranslate(-bmSprite.getWidth(), -bmSprite.getHeight());
                            canvas.drawBitmap(bmSprite, mxTransform, null);

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

    private float[][] generatePointsMap(){

        return new float[][]{{307,250},{160,236},{153,224},{128,235},{82,231},{70,225},{43,199},{33,176},
                {31,130},{32,96},{20,87},{23,73},{2,31},{7,10},{73,3},{106,76},{194,168},{213,169},{225,174},
                {237,189},{424,203},{436,219},{429,242},{406,255},{307,250},{300,249}};
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
