package com.dannie.p2p.other.shadowview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.dannie.p2p.R;

public class RoundLinerLayoutNormal extends LinearLayout {
    public RoundLinerLayoutNormal(Context context) {
        super(context);
        initBackground();
    }

    public RoundLinerLayoutNormal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBackground();
    }

    public RoundLinerLayoutNormal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackground();
    }

    private void initBackground() {
//        setBackground(ViewUtils.generateBackgroundWithShadow(this, R.color.white,
//                R.dimen.round_radius, R.color.shadowColor, R.dimen.elevation, Gravity.BOTTOM));
    }
}