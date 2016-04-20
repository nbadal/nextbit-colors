package com.nextbit.colors.game.graphics;

import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.components.SwitchComponent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class SwitchSprite extends Sprite {
    private final float SIZE_PX = (float) SwitchComponent.RADIUS * Assets.metersToPx;
    private final RectF BOUNDS_PX = new RectF(-SIZE_PX, -SIZE_PX, SIZE_PX, SIZE_PX);
    private static final Paint PAINT = new Paint();

    @Override
    public void render(Canvas canvas, GameColor color) {
        for(int i = 0; i < 4; i++) {
            int angle = i * 90;
            PAINT.setColor(GameColor.values[i].color);
            canvas.drawArc(BOUNDS_PX, angle, 90, true, PAINT);
        }
    }

    @Override
    public double getHeightMeters() {
        return SIZE_PX * 2;
    }
}
