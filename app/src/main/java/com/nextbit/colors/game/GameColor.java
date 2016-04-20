package com.nextbit.colors.game;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

public enum GameColor {
    BLUE  (0xff0000ff),
    RED   (0xffff0000),
    PURPLE(0xff800080),
    GREEN (0xff00ff00),
    ;

    public final ColorFilter filter;
    public final int color;

    public static final GameColor[] values = {BLUE, RED, PURPLE, GREEN};

    GameColor(int color) {
        this.color = color;
        filter = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public static GameColor random() {
        return values[ColorsGame.random.nextInt(4)];
    }

    public static GameColor random(GameColor except) {
        final int newColorIdx = (except.ordinal() + 1 + ColorsGame.random.nextInt(3)) % 4;
        return GameColor.values[newColorIdx];
    }
}
