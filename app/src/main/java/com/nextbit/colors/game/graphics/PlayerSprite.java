package com.nextbit.colors.game.graphics;

import com.nextbit.colors.game.GameColor;

import android.graphics.Canvas;

public class PlayerSprite extends Sprite {
    private final DrawableSprite mBody;
    private final DrawableSprite mLegs;
    public float bodyOffset;

    public PlayerSprite(DrawableSprite body, DrawableSprite legs) {
        mBody = body;
        mLegs = legs;
    }

    @Override
    public void render(Canvas canvas, GameColor color) {
        mLegs.render(canvas, color);

        canvas.save();
        canvas.translate(0, bodyOffset);
        mBody.render(canvas, color);
        canvas.restore();
    }

    @Override
    public double getHeightMeters() {
        return Math.max(mBody.getHeightMeters(), mLegs.getHeightMeters());
    }

    public float getHeightPx() {
        return Math.max(mBody.getHeightPx(), mLegs.getHeightPx());
    }
}
