package com.example.pendulum;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class GraphView extends View {
    private Paint paint;
    private Path path;
    private ArrayList<FloatPoint> arrPoints = new ArrayList<>();

    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4f);

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //drawAxisGrid(canvas);

        drawFuncGraph(canvas);


    }

    private void drawAxisGrid(Canvas canvas) {


        // сетка
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(3f);

        // Вертикальные линии сетки
        for (float x = 120; x < getWidth(); x += 80) {
            canvas.drawLine(x, 0, x, getHeight(), paint);
        }
//        for (float x = originX; x > 0; x -= scaleX) {
//            canvas.drawLine(x, 0, x, getHeight(), paint);
//        }

        // Горизонтальные линии сетки
        for (float y = getHeight()/2; y < getHeight(); y += 80) {
            canvas.drawLine(40, y, getWidth(), y, paint);
        }
        for (float y = getHeight()/2; y > 0; y -= 80) {
            canvas.drawLine(40, y, getWidth(), y, paint);
        }

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4f);

        // Ось X
        canvas.drawLine( 40, getHeight()/2, getWidth(), getHeight()/2, paint);
        // Ось Y
        canvas.drawLine(40, 0, 40, getHeight(), paint);

    }
    private void drawFuncGraph(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(20f);

        path.reset();
        boolean firstPoint = true;
        for (int i = 0; i < arrPoints.size(); i++) {
            float divX = (getWidth()-40)/13;
            float divY = (getHeight())/9;
            float x = 40+arrPoints.get(i).x*10;
            float y = 50+arrPoints.get(i).y;
            canvas.drawCircle(x,y,6,paint);
//            if (firstPoint) {
//
//                path.moveTo(x, y);
//            } else {
//                firstPoint = false;
//                path.lineTo(x, y);
//            }
            //canvas.drawPath(path, paint);
        }
        //Log.d("массив", String.valueOf(arrPoints.size()));



    }
    public void setPoint(FloatPoint point) {
        arrPoints.add(point);
        invalidate();
    }

}