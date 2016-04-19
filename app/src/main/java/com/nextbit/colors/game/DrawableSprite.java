package com.nextbit.colors.game;

import com.nextbit.colors.game.systems.RenderingSystem;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class DrawableSprite extends Sprite {
    private Drawable mDrawable;

    public DrawableSprite(Drawable drawable) {
        this(drawable, 0.5f, 0.5f);
    }

    public DrawableSprite(Drawable drawable, float centerX, float centerY) {
        super();
        mDrawable = drawable;
        int w = mDrawable.getIntrinsicWidth();
        int h = mDrawable.getIntrinsicHeight();
        int l = (int) (-w*centerX);
        int t = (int) (-h*centerY);

        mDrawable.setBounds(l, t, l+w, t+h);
    }

    @Override
    public void render(Canvas canvas) {
        canvas.save();
//        canvas.translate(0, mDrawable.getBounds().top);
        mDrawable.draw(canvas);
        canvas.restore();
    }
}
