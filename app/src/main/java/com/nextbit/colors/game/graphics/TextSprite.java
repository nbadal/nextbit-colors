package com.nextbit.colors.game.graphics;

import com.nextbit.colors.game.GameColor;

import android.graphics.Canvas;

public class TextSprite extends Sprite {
    public String text;

    public TextSprite(String text) {
        this.text = text;
    }

    @Override
    public void render(Canvas canvas, GameColor color) {
        canvas.drawText(text, 0, Assets.TEXT_SIZE_PX/2, Assets.textPaint);
    }

    @Override
    public double getHeightMeters() {
        return Assets.TEXT_SIZE_PX * Assets.pxToMeters;
    }
}
