package com.hencoder.plus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.plus.Utils;

public class Dashboard extends View {
    private static final int ANGLE = 120;
    private static final float RADIUS = Utils.dp2px(150);
    private static final float LENGTH = Utils.dp2px(100);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path dash = new Path();
    PathDashPathEffect effect;

    private int mCurrentSpeed = 0;

    public Dashboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Utils.dp2px(2));
        dash.addRect(0, 0, Utils.dp2px(2), Utils.dp2px(10), Path.Direction.CW);

        // 创建一个和表盘大小一致的路径
        Path arc = new Path();
        arc.addArc(getWidth() / 2 - RADIUS,
                getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS,
                getHeight() / 2 + RADIUS,
                90 + ANGLE / 2,
                360 - ANGLE);
        // 度量对象返回路径的长度，arc：被测量的路径 forceClosed：是否测量闭合路径长度
        PathMeasure pathMeasure = new PathMeasure(arc, false);

        // 通过使用指定的形状标记绘制的路径来对其进行划线。
        // 这仅适用于绘画样式为STROKE或STROKE_AND_FILL时的绘图。
        // 如果paint的样式为FILL，则忽略此效果。paint的strokeWidth不会影响结果。
        effect = new PathDashPathEffect(
                // 形状的路径
                dash,
                // 每个形状之间的间距
                (pathMeasure.getLength() - Utils.dp2px(2)) / 20,
                // 在第一个形状之前的偏移量
                0,
                // 如何在标记每个位置时转换形状
                PathDashPathEffect.Style.ROTATE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        L.i("onDraw===");

        // 画线
        canvas.drawArc(getWidth() / 2 - RADIUS,
                getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS,
                getHeight() / 2 + RADIUS,
                90 + ANGLE / 2,
                360 - ANGLE,
                false, paint);

        // 画刻度
        paint.setPathEffect(effect);
        canvas.drawArc(getWidth() / 2 - RADIUS,
                getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS,
                getHeight() / 2 + RADIUS,
                90 + ANGLE / 2,
                360 - ANGLE,
                false, paint);
        paint.setPathEffect(null);

        // 画指针
        canvas.drawLine(getWidth() / 2,
                getHeight() / 2,
                (float) Math.cos(Math.toRadians(getAngleFromSpeed(mCurrentSpeed))) * LENGTH + getWidth() / 2,
                (float) Math.sin(Math.toRadians(getAngleFromSpeed(mCurrentSpeed))) * LENGTH + getHeight() / 2,
                paint);

    }

    int getAngleFromMark(int mark) {
        return (int) (90 + (float) ANGLE / 2 + (360 - (float) ANGLE) / 20 * mark);
    }

    int getAngleFromSpeed(int speed) {
        return (int) (90 + (float) ANGLE / 2 + speed);
    }

    public void incrementSpeedBy(int by) {
        setSpeed(getSpeed() + by);
    }

    public int getSpeed() {
        return mCurrentSpeed;
    }

    public void setSpeed(int speed) {
        if (speed<0||speed>360-ANGLE){
            return;
        }
        this.mCurrentSpeed = speed;
//        invalidate();
        postInvalidate();
    }
}
