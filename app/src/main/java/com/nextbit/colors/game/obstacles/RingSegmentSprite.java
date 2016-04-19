package com.nextbit.colors.game.obstacles;

import com.nextbit.colors.game.ColoredSprite;
import com.nextbit.colors.game.GameColor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class RingSegmentSprite extends ColoredSprite {
    public RingSegment info;

    private static final RectF tempRect = new RectF();

    private Paint mPaint;

    public RingSegmentSprite() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void render(Canvas canvas, GameColor color) {
        mPaint.setStrokeWidth(info.outerRadius - info.innerRadius);
        mPaint.setColor(color.color);

        synchronized (tempRect) {
            tempRect.set(-info.outerRadius, -info.outerRadius, info.outerRadius, info.outerRadius);
            canvas.drawArc(tempRect, 0, (float) -Math.toDegrees(info.sweep), false, mPaint);
        }
    }
}
