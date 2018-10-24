package com.hencoder.plus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.plus.Utils;

import java.util.Calendar;

public class ClockView extends View {
    private static final float RADIUS_DIAL = Utils.dp2px(150);
    private static final float HAND_HOUR = Utils.dp2px(70);
    private static final float HAND_MINUTE = Utils.dp2px(90);
    private static final float HAND_SECOND = Utils.dp2px(110);

    private static final double DIAL_PERIMETER = 2 * Math.PI * RADIUS_DIAL;

    private float mHourDegree;
    private float mMinuteDegree;
    private float mSecondDegree;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    PathDashPathEffect effect;
    PathDashPathEffect effectLong;
    /**
     * 短刻度形状
     */
    Path pathScale;
    /**
     * 长刻度形状
     */
    Path pathScaleLong;

    {
        // 设置画笔填充方式
        paint.setStyle(Paint.Style.STROKE);

        // 设置画笔粗细
        paint.setStrokeWidth(Utils.dp2px(2));

        // 初始化刻度形状
        pathScale = new Path();
        pathScale.addRect(0,
                0,
                Utils.dp2px(2),
                Utils.dp2px(8),
                Path.Direction.CW);

        pathScaleLong = new Path();
        pathScaleLong.addRect(0,
                0,
                Utils.dp2px(3),
                Utils.dp2px(16),
                Path.Direction.CW);

        // 初始化刻度 effect
        effect = new PathDashPathEffect(
                pathScale,
                (float) DIAL_PERIMETER / 60,
                0,
                PathDashPathEffect.Style.ROTATE
        );
        effectLong = new PathDashPathEffect(
                pathScaleLong,
                (float) DIAL_PERIMETER / 12,
                0,
                PathDashPathEffect.Style.ROTATE
        );
    }

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据当前系统时间得到时针、分针、秒针所对应的角度
        getTimeDegree();
        // 画表盘
        drawDial(canvas);
        // 画刻度
        drawScale(canvas);
        // 时针
        drawHourHand(canvas);
        // 分针
        drawMinuteHand(canvas);
        // 秒针
        drawSecondHand(canvas);
        // 使布局失效，重绘，会调用 onDraw
        invalidate();
    }

    private void drawDial(Canvas canvas) {
        paint.setStrokeWidth(Utils.dp2px(3));
        canvas.drawOval(getWidth() / 2 - RADIUS_DIAL,
                getHeight() / 2 - RADIUS_DIAL,
                getWidth() / 2 + RADIUS_DIAL,
                getHeight() / 2 + RADIUS_DIAL,
                paint);
    }

    private void drawScale(Canvas canvas) {
        // 给画笔设置 effect
        paint.setPathEffect(effect);
        canvas.drawOval(getWidth() / 2 - RADIUS_DIAL,
                getHeight() / 2 - RADIUS_DIAL,
                getWidth() / 2 + RADIUS_DIAL,
                getHeight() / 2 + RADIUS_DIAL,
                paint);
        paint.setPathEffect(effectLong);
        canvas.drawOval(getWidth() / 2 - RADIUS_DIAL,
                getHeight() / 2 - RADIUS_DIAL,
                getWidth() / 2 + RADIUS_DIAL,
                getHeight() / 2 + RADIUS_DIAL,
                paint);
        // 画完之后清除 effect
        paint.setPathEffect(null);
    }

    private void drawHourHand(Canvas canvas) {
        paint.setStrokeWidth(Utils.dp2px(5));
        canvas.drawLine(getWidth() / 2,
                getHeight() / 2,
                (float) Math.cos(Math.toRadians(mHourDegree)) * HAND_HOUR + getWidth() / 2,
                (float) Math.sin(Math.toRadians(mHourDegree)) * HAND_HOUR + getHeight() / 2,
                paint);
    }

    private void drawMinuteHand(Canvas canvas) {
        paint.setStrokeWidth(Utils.dp2px(3));
        canvas.drawLine(getWidth() / 2,
                getHeight() / 2,
                (float) Math.cos(Math.toRadians(mMinuteDegree)) * HAND_MINUTE + getWidth() / 2,
                (float) Math.sin(Math.toRadians(mMinuteDegree)) * HAND_MINUTE + getHeight() / 2,
                paint);
    }

    private void drawSecondHand(Canvas canvas) {
        paint.setStrokeWidth(Utils.dp2px(1));
        canvas.drawLine(getWidth() / 2,
                getHeight() / 2,
                (float) Math.cos(Math.toRadians(mSecondDegree)) * HAND_SECOND + getWidth() / 2,
                (float) Math.sin(Math.toRadians(mSecondDegree)) * HAND_SECOND + getHeight() / 2,
                paint);
    }


    /**
     * 根据当前系统时间得到时针、分针、秒针所对应的角度
     */
    void getTimeDegree() {
        Calendar calendar = Calendar.getInstance();
//        float second = calendar.get(Calendar.SECOND);
        // 更润滑的秒针
        float milliSecond = calendar.get(Calendar.MILLISECOND);
        float second = calendar.get(Calendar.SECOND) + milliSecond / 1000;
        float minute = calendar.get(Calendar.MINUTE) + second / 60;
        float hour = calendar.get(Calendar.HOUR) + minute / 60;

        // 右移 90°
        mSecondDegree = second / 60 * 360 - 90;
        mMinuteDegree = minute / 60 * 360 - 90;
        mHourDegree = hour / 12 * 360 - 90;
    }
}
