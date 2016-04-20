package com.nextbit.colors.game;

import com.nextbit.colors.game.components.PlayerComponent;

import android.graphics.Canvas;
import android.graphics.Paint;

public class PlayerSprite extends Sprite {
    private static final Paint PAINT = new Paint();

    @Override
    public void render(Canvas canvas, GameColor color) {
        PAINT.setColor(color.color);
        // TODO: Sheepify
        canvas.drawCircle(0, 0, PlayerComponent.SIZE, PAINT);
    }
}
