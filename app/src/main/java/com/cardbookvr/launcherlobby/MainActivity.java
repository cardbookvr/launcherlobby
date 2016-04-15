package com.cardbookvr.launcherlobby;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public class MainActivity extends CardboardActivity implements CardboardView.StereoRenderer {
    private OverlayView overlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(this);
        setCardboardView(cardboardView);

        overlayView = (OverlayView) findViewById(R.id.overlay);
        overlayView.calcVirtualWidth(cardboardView);
        Drawable icon = getResources().getDrawable(R.drawable.android_robot, null);
        overlayView.addContent("Hello Virtual World!", icon);
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        final float[] angles = new float[3];
        headTransform.getEulerAngles(angles, 0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                overlayView.setHeadYaw(angles[1]);
            }
        });
    }


    @Override
    public void onDrawEye(Eye eye) {

    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {

    }

    @Override
    public void onRendererShutdown() {

    }
}
