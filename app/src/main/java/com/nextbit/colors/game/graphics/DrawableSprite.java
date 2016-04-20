package com.nextbit.colors.game.graphics;

import com.nextbit.colors.game.GameColor;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class DrawableSprite extends Sprite {
    private Drawable mDrawable;
    private int mWidthPx;
    private int mHeightPx;
    private double mCenterX;
    private double mCenterY;

    public DrawableSprite(Drawable drawable) {
        this(drawable, 0.5, 0.5);
    }

    public DrawableSprite(Drawable drawable, double centerX, double centerY) {
        super();
        mDrawable = drawable;
        mWidthPx = mDrawable.getIntrinsicWidth();
        mHeightPx = mDrawable.getIntrinsicHeight();
        mCenterX = centerX;
        mCenterY = centerY;
    }

    @Override
    public void render(Canvas canvas, GameColor color) {
        int w = mWidthPx;
        int h = mHeightPx;
        int l = (int) (-w*mCenterX);
        int t = (int) (-h*mCenterY);

        canvas.save();
        mDrawable.setBounds(l, t, l+w, t+h);
        if(color != null) {
            mDrawable.setColorFilter(color.filter);
        }
        mDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public double getHeightMeters() {
        return mHeightPx * Assets.pxToMeters;
    }

    public int getHeightPx() {
        return mDrawable.getIntrinsicHeight();
    }
}
