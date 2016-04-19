package com.nextbit.colors.game.util;

public class Vector2 {
    static public final float PI = 3.1415927f;
    static public final float RADIANS_TO_DEGREES = 180f / PI;
    static public final float DEGREES_TO_RADIANS = PI / 180;

    public float x;
    public float y;

    public Vector2() {
    }

    public Vector2(float x, float y) {
        set(x, y);
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 set(Vector2 other) {
        x = other.x;
        y = other.y;
        return this;
    }

    public Vector2 scl(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public Vector2 add(Vector2 other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public float angle () {
        float angle = (float)Math.atan2(y, x) * RADIANS_TO_DEGREES;
        if (angle < 0) angle += 360;
        return angle;
    }

    public float dst (Vector2 v) {
        final float x_d = v.x - x;
        final float y_d = v.y - y;
        return (float)Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public Vector2 sub(Vector2 other) {
        x -= other.x;
        y -= other.y;
        return this;
    }
}
