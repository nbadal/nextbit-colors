package com.nextbit.colors.game.graphics;

import com.nextbit.colors.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;

public class Assets {
    public static final int TEXT_COLOR = 0xff222222;
    public static final int TEXT_SIZE_PX = 68;

    public static double pxToMeters;
    public static float metersToPx;

    public static Paint textPaint;
    public static PlayerSprite sheep;
    public static Sprite phone;

    public static void load(Context context) {
        final Resources res = context.getResources();
        sheep = new PlayerSprite(
                new DrawableSprite(res.getDrawable(R.drawable.sheep_body), 0.375, 0.45),
                new DrawableSprite(res.getDrawable(R.drawable.sheep_legs), 0.375, 0.45)
        );

        metersToPx = sheep.getHeightPx();
        pxToMeters = 1.0 / metersToPx;

        phone = new TwoSideSprite(new DrawableSprite(res.getDrawable(R.drawable.phone)),
                new DrawableSprite(res.getDrawable(R.drawable.phone_back)));

        textPaint = new Paint();
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(TEXT_SIZE_PX);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }
}
