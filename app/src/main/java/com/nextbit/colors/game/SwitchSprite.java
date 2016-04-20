package com.nextbit.colors.game;

import com.nextbit.colors.game.components.SwitchComponent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class SwitchSprite extends Sprite {
    private static final RectF BOUNDS = new RectF(-SwitchComponent.RADIUS, -SwitchComponent.RADIUS, SwitchComponent.RADIUS, SwitchComponent.RADIUS);
    private static final Paint PAINT = new Paint();

    @Override
    public void render(Canvas canvas, GameColor color) {
        for(int i = 0; i < 4; i++) {
            int angle = i * 90;
            PAINT.setColor(GameColor.values[i].color);
            canvas.drawArc(BOUNDS, angle, 90, true, PAINT);
        }
    }

    @Override
    public double getHeight() {
        return SwitchComponent.RADIUS;
    }
}
