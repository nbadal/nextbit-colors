package com.nextbit.colors.app;

import com.nextbit.colors.game.ColorsGame;
import com.nextbit.colors.game.Input;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class GameLoopThread extends Thread {
    final private Object mPauseLock = new Object();
    private GameView view;

    private boolean mActivityPaused;
    private boolean mHasSurface;

    private ColorsGame game;
    private long mLastUpdate;

    public GameLoopThread(GameView view) {
        this.view = view;
        game = new ColorsGame(view.getContext());
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        while (true) {
            Canvas canvas = null;
            try {
                canvas = view.getHolder().lockCanvas();
                if(canvas != null) {
                    synchronized (view.getHolder()) {

                        Input.preUpdate();
                        canvas.drawColor(Color.WHITE);

                        long now = System.currentTimeMillis();

                        game.update(canvas, now - mLastUpdate);
                        mLastUpdate = now;
                        Input.postUpdate();
                    }
                }
            } finally {
                if (canvas != null) {
                    view.getHolder().unlockCanvasAndPost(canvas);
                }
            }

            synchronized (mPauseLock) {
                while (mActivityPaused || !mHasSurface) {
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void onPause() {
        synchronized (mPauseLock) {
            mActivityPaused = true;
        }
    }

    public void onSurfaceDestroyed() {
        synchronized (mPauseLock) {
            mHasSurface = false;
        }
    }

    public void onSurfaceCreated() {
        synchronized (mPauseLock) {
            mHasSurface = true;
            if(!mActivityPaused) {
                unpause();
            }
        }
    }

    public void onResume() {
        synchronized (mPauseLock) {
            mActivityPaused = false;
            if(mHasSurface) {
                unpause();
            }
        }
    }

    private void unpause() {
        mPauseLock.notifyAll();
    }

    public void setSize(int w, int h) {
        game.setSize(w, h);
    }
}
