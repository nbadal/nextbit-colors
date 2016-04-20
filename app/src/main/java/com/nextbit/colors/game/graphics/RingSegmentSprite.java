package com.nextbit.colors.game.graphics;

import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.obstacles.RingSegment;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class RingSegmentSprite extends Sprite {
    private static final float SAFETY_PX = 5;
    public RingSegment info;

    private static final RectF tempRectPx = new RectF();

    private Paint mPaint;

    public RingSegmentSprite() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void render(Canvas canvas, GameColor color) {
        final float strokeWidthPx =
                (float) ((info.outerRadius - info.innerRadius) * Assets.metersToPx);
        final float halfStrokePx = strokeWidthPx / 2;

        mPaint.setStrokeWidth(strokeWidthPx + SAFETY_PX);
        mPaint.setColor(color.color);

        synchronized (tempRectPx) {
            final float sizePx = (float) (info.outerRadius * Assets.metersToPx - halfStrokePx) ;
            tempRectPx.set(-sizePx, -sizePx, sizePx, sizePx);
            canvas.drawArc(tempRectPx, 0, (float) -Math.toDegrees(info.sweep), false, mPaint);
        }
    }

    @Override
    public double getHeightMeters() {
        return info.outerRadius;
    }
}
