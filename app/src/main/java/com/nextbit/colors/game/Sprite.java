package com.nextbit.colors.game;

import com.nextbit.colors.game.components.PhysicsComponent;

import android.graphics.Canvas;

public abstract class Sprite {
    public abstract void render(Canvas canvas, GameColor color);
    public abstract double getHeight();
}
