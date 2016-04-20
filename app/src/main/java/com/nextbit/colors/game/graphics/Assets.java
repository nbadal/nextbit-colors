package com.nextbit.colors.game.graphics;

import com.nextbit.colors.R;

import android.content.Context;
import android.content.res.Resources;

public class Assets {
    public static double pxToMeters;
    public static float metersToPx;

    public static DrawableSprite sheep;

    public static void load(Context context) {
        final Resources res = context.getResources();
        sheep = new DrawableSprite(res.getDrawable(R.drawable.sheep), 0.6, 0.45);

        metersToPx = sheep.getHeightPx();
        pxToMeters = 1.0 / metersToPx;
    }
}
