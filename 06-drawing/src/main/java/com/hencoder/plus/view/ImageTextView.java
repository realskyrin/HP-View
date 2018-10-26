package com.hencoder.plus.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;

import com.hencoder.plus.R;
import com.hencoder.plus.Utils;

public class ImageTextView extends View {
    public static final float IMAGE_WIDTH = Utils.dp2px(100);
    public static final float IMAGE_OFFSET = Utils.dp2px(80);
    public static final float MARGIN = Utils.dp2px(8);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    float[] cutWidth = new float[1];

    /**
     *  baseline：字符基线
     *  ascent：字符最高点到baseline的推荐距离
     *  top：字符最高点到baseline的最大距离
     *  descent：字符最低点到baseline的推荐距离
     *  bottom：字符最低点到baseline的最大距离
     *  leading：行间距，即前一行的descent与下一行的ascent之间的距离
     */
    String text = "";

    /**
     *  描述 Font 各个基线尺寸的度量类
     */
    Paint.FontMetrics fontMetrics = new Paint.FontMetrics();

    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        bitmap = getBitMapFormDrawable((int)IMAGE_WIDTH,(int)IMAGE_WIDTH, R.drawable.avatar_rengwuxian);
        paint.setTextSize(Utils.dp2px(14));
        paint.getFontMetrics(fontMetrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap,MARGIN,MARGIN,paint);
        int length = text.length();
        float verticalOffset = - fontMetrics.top;
        for (int start = 0;start<length;){
            int maxWidth;
            float textTop = verticalOffset+fontMetrics.top;
            float textBottom = verticalOffset+fontMetrics.bottom;

            if (textTop>IMAGE_OFFSET){
                // 文字和图片重合，文字需要换行
                maxWidth = (int) (getWidth()-IMAGE_WIDTH);

            }else {
                // 文字和图片不重合
                maxWidth = getWidth();
            }
            // 取到每行的 text count
            // public int breakText(String text, boolean measureForwards, float maxWidth, float[] measuredWidth)
            int count = paint.breakText(text,start,length,true,maxWidth,cutWidth);
        }
    }

    Bitmap getBitMapFormDrawable(int width, int height, int resId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),resId,options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;

        return BitmapFactory.decodeResource(getResources(),resId,options);
    }
}