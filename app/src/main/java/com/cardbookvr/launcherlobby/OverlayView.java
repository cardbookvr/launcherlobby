package com.cardbookvr.launcherlobby;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Schoen and Jonathan on 4/15/2016.
 */
public class OverlayView extends LinearLayout {

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, 1.0f);
        params.setMargins(0, 0, 0, 0);

        OverlayEye eye = new OverlayEye(context, attrs);
        eye.setLayoutParams(params);
        addView(eye);

        eye.setColor(Color.rgb(150, 255, 180));
        eye.addContent("Hello Virtual World!");
        setVisibility(View.VISIBLE);
    }

    private class OverlayEye extends ViewGroup {
        private Context context;
        private AttributeSet attrs;
        private TextView textView;
        private int textColor;

        public OverlayEye(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.context = context;
            this.attrs = attrs;
        }

        public void setColor(int color) {
            this.textColor = color;
        }

        public void addContent(String text) {
            textView = new TextView(context, attrs);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(textColor);
            textView.setText(text);
            addView(textView);
        }

        @Override
        protected void onLayout(boolean changed, int left, int
                top, int right, int bottom) {
            final int width = right - left;
            final int height = bottom - top;

            final float verticalTextPos = 0.52f;

            float topMargin = height * verticalTextPos;
            textView.layout(0, (int) topMargin, width, bottom);
        }

    }

}
