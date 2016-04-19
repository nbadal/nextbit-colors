package com.nextbit.colors.game.obstacles;

import com.nextbit.colors.game.ColoredSprite;
import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.Obstacle;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class RingSegment extends ColoredSprite implements Obstacle {
    public float outerRadius;
    public float innerRadius;
    public float sweep;

    private static final RectF tempRect = new RectF();

    private Paint mPaint;

    public RingSegment() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void render(Canvas canvas, GameColor color) {
        mPaint.setStrokeWidth(outerRadius - innerRadius);
        mPaint.setColor(color.color);

        synchronized (tempRect) {
            tempRect.set(-outerRadius, -outerRadius, outerRadius, outerRadius);
            canvas.drawArc(tempRect, 0, -sweep, false, mPaint);
        }
    }
}
