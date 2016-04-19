package com.nextbit.colors.game.obstacles;

import com.nextbit.colors.game.ColoredSprite;
import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.Obstacle;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class RingSegment extends ColoredSprite implements Obstacle {
    float outerRadius;
    float innerRadius;
    int degrees;

    private static final RectF tempRect = new RectF();

    private Paint mPaint = new Paint();

    @Override
    public void render(Canvas canvas, GameColor color) {
        updatePaint();
        synchronized (tempRect) {
            tempRect.set(-outerRadius, -outerRadius, outerRadius, outerRadius);
            canvas.drawArc(tempRect, 0, degrees, false, mPaint);
        }
    }

    private void updatePaint() {
        mPaint.setStrokeWidth(outerRadius - innerRadius);
    }
}
