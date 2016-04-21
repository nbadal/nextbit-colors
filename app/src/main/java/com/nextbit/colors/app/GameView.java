package com.nextbit.colors.app;

import com.nextbit.colors.game.Input;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
    private final SurfaceHolder holder;
    private final GameLoopThread gameLoopThread;
    private boolean mRunning;


    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(!mRunning) {
                    gameLoopThread.start();
                    mRunning = true;
                }
                gameLoopThread.onSurfaceCreated();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                gameLoopThread.onSurfaceDestroyed();
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        gameLoopThread.setSize(w, h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int act = event.getAction();
        Input.setTouched(act == MotionEvent.ACTION_DOWN || act == MotionEvent.ACTION_MOVE);
        return true;
    }

    public void onPause() {
        gameLoopThread.onPause();
    }

    public void onResume() {
        gameLoopThread.onResume();
    }
}
