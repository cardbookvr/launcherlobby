package com.cardbookvr.launcherlobby;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.vr.sdk.base.GvrView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Schoen and Jonathan on 4/15/2016.
 */
public class OverlayView extends LinearLayout {
    private final OverlayEye leftEye;
    private final OverlayEye rightEye;

    private int virtualWidth;
    private float pixelsPerRadian;

    private int headOffset;

    private List<Shortcut> shortcuts = new ArrayList<Shortcut>();
    private final int maxShortcuts = 24;

    private int shortcutWidth;

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

    public void calcVirtualWidth(GvrView cardboard) {
        int screenWidth = cardboard.getHeadMountedDisplay().getScreenParams().getWidth() / 2;
        float fov = cardboard.getGvrViewerParams().getLeftEyeMaxFov().getLeft();
        float pixelsPerDegree = screenWidth / fov;
        pixelsPerRadian = (float) (pixelsPerDegree * 180.0 / Math.PI);
        virtualWidth = (int) (pixelsPerDegree * 360.0);
        shortcutWidth = virtualWidth / maxShortcuts;
    }

    public void setHeadYaw(float angle) {
        headOffset = (int)( angle * pixelsPerRadian );
        leftEye.setHeadOffset(headOffset);
        rightEye.setHeadOffset(headOffset);
    }

    public void addShortcut(Shortcut shortcut){
        shortcuts.add(shortcut);
        leftEye.addShortcut(shortcut);
        rightEye.addShortcut(shortcut);
    }

    public int getSlot() {
        int slotOffset = shortcutWidth/2 - headOffset;
        slotOffset /= shortcutWidth;
        if(slotOffset < 0)
            slotOffset = 0;
        if(slotOffset >= shortcuts.size())
            slotOffset = shortcuts.size() - 1;
        return slotOffset;
    }

    public void onTrigger() {
        shortcuts.get( getSlot() ).launch();
    }


    private class OverlayEye extends ViewGroup {
        private Context context;
        private AttributeSet attrs;
        private int textColor;
        private int depthOffset;
        private int viewWidth;

        private final List<TextView> textViews = new ArrayList<TextView>();
        private final List<ImageView> imageViews = new ArrayList<ImageView>();


        public OverlayEye(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.context = context;
            this.attrs = attrs;
        }

        public void setColor(int color) {
            this.textColor = color;
        }

        public void addShortcut(Shortcut shortcut) {
            TextView textView = new TextView(context, attrs);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0f);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(textColor);
            textView.setText(shortcut.name);
            addView(textView);
            textViews.add(textView);

            ImageView imageView = new ImageView(context, attrs);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setAdjustViewBounds(true); // preserve aspect ratio
            imageView.setImageDrawable(shortcut.icon);
            addView(imageView);
            imageViews.add(imageView);
        }

        public void setHeadOffset(int headOffset) {
            int currentSlot = getSlot();
            int slot = 0;
            for(TextView textView : textViews) {
                textView.setX(headOffset + depthOffset + (shortcutWidth * slot));
                if (slot==currentSlot) {
                    textView.setTextColor(Color.WHITE);
                } else {
                    textView.setTextColor(textColor);
                }
                slot++;
            }
            slot = 0;
            for(ImageView imageView : imageViews) {
                imageView.setX(headOffset + depthOffset + (shortcutWidth * slot));
                slot++;
            }
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            final int width = right - left;
            final int height = bottom - top;

            final float verticalTextPos = 0.52f;

            float topMargin = height * verticalTextPos;
            for(TextView textView : textViews) {
                textView.layout(0, (int) topMargin, width, bottom);
            }
            viewWidth = width;

            final float imageSize = 0.1f;
            final float verticalImageOffset = -0.07f;
            float imageMargin = (1.0f - imageSize) / 2.0f;
            topMargin = (height * (imageMargin + verticalImageOffset));
            float botMargin =  topMargin + (height * imageSize);
            for(ImageView imageView : imageViews) {
                imageView.layout(0, (int) topMargin, width, (int) botMargin);
            }
        }

        public void setDepthFactor(float factor) {
            this.depthOffset = (int)(factor * viewWidth);
        }
    }

}
