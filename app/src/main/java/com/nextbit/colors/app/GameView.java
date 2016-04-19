package com.nextbit.colors.app;

import com.nextbit.colors.game.ColorsGame;
import com.nextbit.colors.game.Input;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class GameView extends SurfaceView {
    private final SurfaceHolder holder;
    private final GameLoopThread gameLoopThread;
    private final ColorsGame game;

    private long mLastUpdate;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        gameLoopThread = new GameLoopThread(this);
        game = new ColorsGame(context);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while(retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                gameLoopThread.setRunning(true);
                if(!gameLoopThread.isAlive()) {
                    gameLoopThread.start();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                gameLoopThread.setRunning(false);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Input.preUpdate();
        canvas.drawColor(Color.BLACK);

        long now = System.currentTimeMillis();

        game.update(canvas, now - mLastUpdate);
        mLastUpdate = now;
        Input.postUpdate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        game.setSize(w, h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int act = event.getAction();
        Input.setTouched(act == MotionEvent.ACTION_DOWN || act == MotionEvent.ACTION_MOVE);
        return true;
    }
}
