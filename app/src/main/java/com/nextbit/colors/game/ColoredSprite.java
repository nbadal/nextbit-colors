package com.nextbit.colors.game;

import android.graphics.Canvas;

public abstract class ColoredSprite extends Sprite {
    public GameColor color;

    @Override
    public final void render(Canvas canvas) {
        render(canvas, color);
    }

    public abstract void render(Canvas canvas, GameColor color);
}
