package com.cardbookvr.launcherlobby;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

public class MainActivity extends GvrActivity implements GvrView.StereoRenderer {
    public static MainActivity instance;
    private OverlayView overlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_main);

        GvrView gvrView = (GvrView) findViewById(R.id.gvr_view);
        gvrView.setRenderer(this);
        setGvrView(gvrView);

        overlayView = (OverlayView) findViewById(R.id.overlay);
        overlayView.calcVirtualWidth(gvrView);
        Drawable icon = getResources().getDrawable(R.drawable.android_robot, null);

        getAppList();
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

    @Override
    public void onCardboardTrigger(){
        overlayView.onTrigger();
    }

    private void getAppList() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory("com.google.intent.category.CARDBOARD");
        mainIntent.addFlags(PackageManager.GET_INTENT_FILTERS);

        final List<ResolveInfo> pkgAppsList = getPackageManager().queryIntentActivities( mainIntent, PackageManager.GET_INTENT_FILTERS);

        int count = 0;
        for (ResolveInfo info : pkgAppsList) {
            overlayView.addShortcut( new Shortcut(info, getPackageManager()));
            if (++count == 24)
                break;
        }
    }

}
