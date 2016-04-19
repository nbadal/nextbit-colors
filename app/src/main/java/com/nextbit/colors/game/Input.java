package com.nextbit.colors.game;

public class Input {
    /*
     * We keep a 1 frame buffer to avoid jitter-related false negatives
     */
    private static boolean actualState;
    private static boolean downThisFrame;
    private static boolean downPreviousFrame;
    private static boolean downPreviousPreviousFrame;

    public static synchronized void preUpdate() {
        downThisFrame = actualState;
    }

    public static synchronized boolean justTouched() {
        return !downPreviousPreviousFrame && downPreviousFrame;
    }

    public static synchronized void postUpdate() {
        downPreviousPreviousFrame = downPreviousFrame;
        downPreviousFrame = downThisFrame;
    }

    public static synchronized void setTouched(boolean touched) {
        actualState = touched;
        if(touched) {
            downThisFrame = true;
        }
    }
}
