package com.hencoder.plus.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.plus.R;
import com.hencoder.plus.Utils;

public class ImageTextView extends View {
    /**
     * 图片宽高
     */
    public static final float IMAGE_WIDTH = Utils.dp2px(100);
    /**
     * 图片 margin
     */
    public static final float MARGIN_TOP = Utils.dp2px(75);
    public static final float MARGIN_LEFT = Utils.dp2px(75);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    float[] cutWidth = new float[1];
    String text = "每一个类似虎嗅这样的平台去反思的、去继续探索的你发现了没？从内容到流量再到商业化每一个类似虎嗅这样的平台" +
            "去反思的、去继续探索的你发现了没？从内容到流量再到商业化每一个类似虎嗅这样的平台去反思的、去继续探索的你发现了没？" +
            "从内容到流量再到商业化你发现了没？从内容到流量再到商业化，总体“还是传统的内容配方，还是熟悉的味道”。现在大家都在摸" +
            "着石头过河，未来能不能换个内容配方？多种内容配方怎么去组合？新内容配方如何挖掘背后的价值？这些仍然是值得" +
            "每一个媒体人、每一个类似虎嗅这样的平台去反思的、去继续探索的你发现了没？从内容到流量再到商业化，总体“还是传" +
            "统的内容配方，还是熟悉的味道”。现在大家都在摸着石头过河，未来能不能换个内容配方？多种内容配方怎么去组合？新" +
            "内容配方如何挖掘背后的价值？这些仍然是值得每一个媒体人、每一个类似虎嗅这样的平台去反思的、去继续探索的";

    /**
     * 描述 Font 各个基线尺寸的度量类
     * baseline：字符基线
     * ascent：字符最高点到baseline的推荐距离
     * top：字符最高点到baseline的最大距离
     * descent：字符最低点到baseline的推荐距离
     * bottom：字符最低点到baseline的最大距离
     * leading：行间距，即前一行的descent与下一行的ascent之间的距离
     */
    Paint.FontMetrics fontMetrics = new Paint.FontMetrics();

    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        bitmap = getBitMapFormDrawable((int) IMAGE_WIDTH, (int) IMAGE_WIDTH, R.drawable.avatar_rengwuxian);
        paint.setTextSize(Utils.dp2px(14));
        paint.getFontMetrics(fontMetrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, MARGIN_LEFT, MARGIN_TOP, paint);
        int length = text.length();
        float verticalOffset = -fontMetrics.top;

        for (int start = 0; start < length; ) {
            int leftWidth;
            int rightWidth;
            float textTop = verticalOffset + fontMetrics.top;
            float textBottom = verticalOffset + fontMetrics.bottom;

            if (isTextWillNotOverIMG(textTop, textBottom)) {
                // 文字和图片不重合，直接写满整行
                leftWidth = getWidth();
                rightWidth = 0;
            } else {
                // 文字和图片重合，文字需要调整
                leftWidth = (int) (MARGIN_LEFT);
                rightWidth = getWidth() - (int) (leftWidth + IMAGE_WIDTH);
            }
            // 取到每行的 text count
            // public int breakText(String text, boolean measureForwards, float maxWidth, float[] measuredWidth)
            // 测量指定长度（maxWidth）字符串所包含的字符个数并返回
            int leftCount = paint.breakText(
                    // 被测量字符
                    text,
                    // 起始位置
                    start,
                    // 结束位置
                    length,
                    // 测量方向：从左到右
                    true,
                    // The maximum width to accumulate
                    leftWidth,
                    // 切片单位
                    cutWidth);

            // 左侧最后一个字符的下标
            int leftEnd = start + leftCount;
            canvas.drawText(text, start, leftEnd, 0, verticalOffset, paint);

            int rightCount = 0;
            if (rightWidth != 0) {
                // 右侧字符个数
                rightCount = paint.breakText(
                        // 被测量字符
                        text,
                        // 起始位置
                        leftEnd,
                        // 结束位置
                        length,
                        // 测量方向：从左到右
                        true,
                        // The maximum width to accumulate
                        rightWidth,
                        // 切片单位
                        cutWidth);
                canvas.drawText(
                        // 需要绘制的字符
                        text,
                        // 起始下标
                        leftEnd,
                        // 结束下标
                        leftEnd + rightCount,
                        // 相对于 View 的 x 坐标
                        MARGIN_LEFT+IMAGE_WIDTH,
                        // 相对于 View 的 y 坐标
                        verticalOffset, paint);
            }
            // 更新最新下标位置
            start += (leftCount + rightCount);
            verticalOffset += paint.getFontSpacing();
        }
    }

    private boolean isTextWillNotOverIMG(float textTop, float textBottom) {
        boolean isTopText = textBottom < MARGIN_TOP;
        boolean isBottomText = textTop > (MARGIN_TOP + IMAGE_WIDTH);
        return isTopText || isBottomText;
    }

    Bitmap getBitMapFormDrawable(int width, int height, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;

        return BitmapFactory.decodeResource(getResources(), resId, options);
    }
}