package com.cardbookvr.launcherlobby;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.vrtoolkit.cardboard.CardboardView;

/**
 * Created by Schoen and Jonathan on 4/15/2016.
 */
public class OverlayView extends LinearLayout {
    private final OverlayEye leftEye;
    private final OverlayEye rightEye;

    private int virtualWidth;
    private float pixelsPerRadian;

    private int headOffset;

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, 1.0f);
        params.setMargins(0, 0, 0, 0);

        leftEye = new OverlayEye(context, attrs);
        leftEye.setLayoutParams(params);
        addView(leftEye);

        rightEye = new OverlayEye(context, attrs);
        rightEye.setLayoutParams(params);
        addView(rightEye);

        setDepthFactor(0.01f);
        setColor(Color.rgb(150, 255, 180));
        setVisibility(View.VISIBLE);
    }

    public void setDepthFactor(float factor) {
        leftEye.setDepthFactor(factor);
        rightEye.setDepthFactor(-factor);
    }

    public void setColor(int color) {
        leftEye.setColor(color);
        rightEye.setColor(color);
    }

    public void addContent(String text, Drawable icon) {
        leftEye.addContent(text, icon);
        rightEye.addContent(text, icon);
    }

    public void calcVirtualWidth(CardboardView cardboard) {
        int screenWidth = cardboard.getHeadMountedDisplay().getScreenParams().getWidth() / 2;
        float fov = cardboard.getCardboardDeviceParams().getLeftEyeMaxFov().getLeft();
        float pixelsPerDegree = screenWidth / fov;
        pixelsPerRadian = (float) (pixelsPerDegree * 180.0 / Math.PI);
        virtualWidth = (int) (pixelsPerDegree * 360.0);
    }

    public void setHeadYaw(float angle) {
        headOffset = (int)( angle * pixelsPerRadian );
        leftEye.setHeadOffset(headOffset);
        rightEye.setHeadOffset(headOffset);
    }

    private class OverlayEye extends ViewGroup {
        private Context context;
        private AttributeSet attrs;
        private TextView textView;
        private int textColor;
        private int depthOffset;
        private int viewWidth;

        private ImageView imageView;

        public OverlayEye(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.context = context;
            this.attrs = attrs;
        }

        public void setColor(int color) {
            this.textColor = color;
        }

        public void addContent(String text, Drawable icon) {
            textView = new TextView(context, attrs);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(textColor);
            textView.setText(text);
            textView.setX(depthOffset);
            addView(textView);

            imageView = new ImageView(context, attrs);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setAdjustViewBounds(true);
            // preserve aspect ratio
            imageView.setImageDrawable(icon);
            addView(imageView);
        }

        public void setHeadOffset(int headOffset) {
            textView.setX( headOffset + depthOffset );
            imageView.setX( headOffset + depthOffset );
        }

        @Override
        protected void onLayout(boolean changed, int left, int
                top, int right, int bottom) {
            final int width = right - left;
            final int height = bottom - top;

            final float verticalTextPos = 0.52f;

            float topMargin = height * verticalTextPos;
            textView.layout(0, (int) topMargin, width, bottom);
            viewWidth = width;

            final float imageSize = 0.1f;
            final float verticalImageOffset = -0.07f;
            float imageMargin = (1.0f - imageSize) / 2.0f;
            topMargin = (height * (imageMargin + verticalImageOffset));
            float botMargin =  topMargin + (height * imageSize);
            imageView.layout(0, (int) topMargin, width, (int) botMargin);
        }

        public void setDepthFactor(float factor) {
            this.depthOffset = (int)(factor * viewWidth);
        }
    }

}
