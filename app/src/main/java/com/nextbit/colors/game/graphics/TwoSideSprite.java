package com.nextbit.colors.game.graphics;

import com.nextbit.colors.game.GameColor;

import android.graphics.Canvas;

public class TwoSideSprite extends Sprite {
    private final Sprite mFront;
    private final Sprite mBack;
    private boolean mShowFront = true;

    public TwoSideSprite(Sprite front, Sprite back) {
        mFront = front;
        mBack = back;
    }

    public void setFront(boolean front) {
        mShowFront = front;
    }

    @Override
    public void render(Canvas canvas, GameColor color) {
        if(mShowFront) {
            mFront.render(canvas, color);
        } else {
            mBack.render(canvas, color);
        }
    }

    @Override
    public double getHeightMeters() {
        if(mShowFront) {
            return mFront.getHeightMeters();
        } else {
            return mBack.getHeightMeters();
        }
    }
}
