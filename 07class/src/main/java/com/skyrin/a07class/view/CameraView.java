package com.skyrin.a07class.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.skyrin.a07class.Utils;

public class CameraView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Camera camera = new Camera();
    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context,@Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        camera.rotateX(45);
        camera.setLocation(0,0,Utils.getZForCamera());
    }

}
