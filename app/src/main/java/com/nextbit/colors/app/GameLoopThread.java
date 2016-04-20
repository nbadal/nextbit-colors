package com.nextbit.colors.app;

import com.nextbit.colors.game.ColorsGame;
import com.nextbit.colors.game.Input;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;

public class GameLoopThread extends Thread {
    final private Object mPauseLock = new Object();
    private GameView view;
    private boolean mPaused;
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
                while (mPaused) {
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
            mPaused = true;
        }
    }

    public void onResume() {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }

    public void setSize(int w, int h) {
        game.setSize(w, h);
    }
}
