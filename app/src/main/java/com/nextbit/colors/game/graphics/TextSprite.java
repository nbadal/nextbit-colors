package com.nextbit.colors.game.graphics;

import com.nextbit.colors.game.GameColor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class TextSprite extends Sprite {
    public String text;

    private static final int PADDING = 10;
    private static final Rect sTempRect = new Rect();
    private static final RectF sTempRectF = new RectF();
    private static final Paint sBgPaint = new Paint();
    static {
        sBgPaint.setColor(Color.WHITE);
    }

    public TextSprite(String text) {
        this.text = text;
    }

    @Override
    public void render(Canvas canvas, GameColor color) {
        Assets.textPaint.getTextBounds(text, 0, text.length(), sTempRect);

        canvas.save();
        canvas.translate(-sTempRect.width() / 2, 0);

        sTempRectF.set(sTempRect);
        sTempRectF.inset(-PADDING, -PADDING);
        sTempRectF.offset(0, Assets.TEXT_SIZE_PX/2);

        canvas.drawRoundRect(sTempRectF, PADDING, PADDING, sBgPaint);

        canvas.drawText(text, 0, Assets.TEXT_SIZE_PX/2, Assets.textPaint);
        canvas.restore();
    }

    @Override
    public double getHeightMeters() {
        return Assets.TEXT_SIZE_PX * Assets.pxToMeters;
    }
}
