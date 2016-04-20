package com.nextbit.colors.game.obstacles;

import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.Sprite;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class RingSegmentSprite extends Sprite {
    private static final float SAFETY = 5;
    public RingSegment info;

    private static final RectF tempRect = new RectF();

    private Paint mPaint;

    public RingSegmentSprite() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void render(Canvas canvas, GameColor color) {
        final float strokeWidth = info.outerRadius - info.innerRadius;
        final float halfStroke = strokeWidth / 2;

        mPaint.setStrokeWidth(strokeWidth + SAFETY);
        mPaint.setColor(color.color);

        synchronized (tempRect) {
            final float size = info.outerRadius - halfStroke;
            tempRect.set(-size, -size, size, size);
            canvas.drawArc(tempRect, 0, (float) -Math.toDegrees(info.sweep), false, mPaint);
        }
    }
}
