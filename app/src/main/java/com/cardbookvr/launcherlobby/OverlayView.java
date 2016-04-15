package com.cardbookvr.launcherlobby;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Schoen and Jonathan on 4/15/2016.
 */
public class OverlayView extends LinearLayout {

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TextView textView = new TextView(context, attrs);
        addView(textView);

        textView.setTextColor(Color.rgb(150, 255, 180));
        textView.setText("Hello Virtual World!");
        setVisibility(View.VISIBLE);
    }

}
