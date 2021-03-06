package com.nextbit.colors.game;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

public enum GameColor {
    RED   (0xffec4331),
    YELLOW(0xfff6d70a),
    GREEN (0xff52c272),
    BLUE  (0xff596eb4),
    ;

    public final ColorFilter filter;
    public final int color;

    public static final GameColor[] values = {RED, YELLOW, GREEN, BLUE};

    GameColor(int color) {
        this.color = color;
        filter = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public static GameColor random() {
        return values[ColorsGame.random.nextInt(4)];
    }

    public static GameColor random(GameColor except) {
        if(except == null) {
            return random();
        }
        final int newColorIdx = (except.ordinal() + 1 + ColorsGame.random.nextInt(3)) % 4;
        return GameColor.values[newColorIdx];
    }

    public GameColor opposite() {
        return offset(2);
    }

    public GameColor offset(int offset) {
        while(offset < 0) {
            offset += 4;
        }
        return GameColor.values[(ordinal() + offset) % 4];
    }
}
