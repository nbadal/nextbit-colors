package com.nextbit.colors.game.util;


public class GravityMath {
    /** Height in px */
    public static final float JUMP_HEIGHT = 2.0f;
    /** Time in s */
    public static final float JUMP_TIME = 0.45f;

    // Yay, Calculus!
    
    public static final float GRAVITY = (-8f * JUMP_HEIGHT) / (JUMP_TIME * JUMP_TIME);
    public static final float JUMP_VELOCITY= 4 * JUMP_HEIGHT / JUMP_TIME;
}
