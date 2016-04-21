package com.nextbit.colors.game.graphics;

import com.nextbit.colors.game.GameColor;

import android.graphics.Canvas;

public abstract class Sprite {
    public abstract void render(Canvas canvas, GameColor color);
    public abstract double getHeightMeters();
}
