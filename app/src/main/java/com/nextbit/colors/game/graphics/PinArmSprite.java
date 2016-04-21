package com.nextbit.colors.game.graphics;

import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.obstacles.PinArm;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class PinArmSprite extends Sprite {
    public PinArm info;

    private static final Path sTempPath = new Path();

    private Paint mPaint = new Paint();

    @Override
    public void render(Canvas canvas, GameColor color) {
        final float thicknessPx = (float) (info.thickness * Assets.metersToPx);
        final float halfThicknessPx = thicknessPx / 2f;
        final float lengthPx = (float) (info.length * Assets.metersToPx);

        mPaint.setColor(color.color);

        sTempPath.reset();
        sTempPath.lineTo(halfThicknessPx, halfThicknessPx);
        sTempPath.lineTo(halfThicknessPx, -halfThicknessPx);
        sTempPath.close();

        canvas.drawPath(sTempPath, mPaint);
        canvas.drawRect(halfThicknessPx, halfThicknessPx, lengthPx - halfThicknessPx,
                -halfThicknessPx, mPaint);
        canvas.drawCircle(lengthPx - halfThicknessPx, 0, halfThicknessPx, mPaint);
    }


    @Override
    public double getHeightMeters() {
        return info.length;
    }
}
