package com.nextbit.colors.app;

import com.nextbit.colors.game.ColorsGame;
import com.nextbit.colors.game.Input;
import com.nextbit.colors.game.ScoreListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class GameLoopThread extends Thread implements ScoreListener {
    private static final String TAG = GameLoopThread.class.getSimpleName();
    private static final String PREFS = "ColorsGame";
    private static final String KEY_HIGHSCORE = "highscore";
    final private Object mPauseLock = new Object();
    private GameView view;

    private boolean mActivityPaused;
    private boolean mHasSurface;

    private ColorsGame game;
    private long mLastUpdate;

    public GameLoopThread(GameView view) {
        this.view = view;
        game = new ColorsGame(view.getContext());
        game.setScoreListener(this);
        ColorsGame.highScore = getHighScore();
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
                        canvas.drawColor(Color.MAGENTA);

                        long now = System.currentTimeMillis();

                        game.update(canvas, now - mLastUpdate);
                        mLastUpdate = now;
                        Input.postUpdate();
                    }
                }
            } finally {
                if (canvas != null) {
                    try {
                        view.getHolder().unlockCanvasAndPost(canvas);
                    } catch (IllegalStateException ise) {
                        Log.w(TAG, "Illegal State", ise);
                    }
                }
            }

            synchronized (mPauseLock) {
                if(isGamePaused()) {
                    Log.d(TAG, "Pausing.");
                }
                while (isGamePaused()) {
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private boolean isGamePaused() {
        return mActivityPaused || !mHasSurface;
    }

    public void onPause() {
        Log.d(TAG, "onPause");
        synchronized (mPauseLock) {
            mActivityPaused = true;
        }
    }

    public void onSurfaceDestroyed() {
        Log.d(TAG, "onSurfaceDestroyed");
        synchronized (mPauseLock) {
            mHasSurface = false;
        }
    }

    public void onSurfaceCreated() {
        Log.d(TAG, "onSurfaceCreated");
        synchronized (mPauseLock) {
            mHasSurface = true;
            if(!mActivityPaused) {
                unpause();
            }
        }
    }

    public void onResume() {
        Log.d(TAG, "onResume");
        synchronized (mPauseLock) {
            mActivityPaused = false;
            if(mHasSurface) {
                unpause();
            }
        }
    }

    private void unpause() {
        Log.d(TAG, "Un-pausing.");
        mPauseLock.notifyAll();
    }

    public void setSize(int w, int h) {
        game.setSize(w, h);
    }

    @Override
    public void onScoreChanged(int score) {
        int previousHigh = getHighScore();
        if(score > previousHigh) {
            setHighScore(score);
        }
    }

    private int getHighScore() {
        final SharedPreferences sp = view.getContext().getSharedPreferences(PREFS, Context
                .MODE_PRIVATE);
        return sp.getInt(KEY_HIGHSCORE, 0);
    }

    private void setHighScore(int highScore) {
        final SharedPreferences sp = view.getContext().getSharedPreferences(PREFS, Context
                .MODE_PRIVATE);
        sp.edit()
                .putInt(KEY_HIGHSCORE, highScore)
                .apply();

        ColorsGame.highScore = highScore;
    }
}
