package com.nextbit.colors.game;

import com.badlogic.ashley.core.PooledEngine;
import com.nextbit.colors.game.systems.GravitySystem;
import com.nextbit.colors.game.systems.MovementSystem;
import com.nextbit.colors.game.systems.ObstacleCollisionSystem;
import com.nextbit.colors.game.systems.PlayerFloorSystem;
import com.nextbit.colors.game.systems.PlayerJumpSystem;
import com.nextbit.colors.game.systems.RenderingSystem;

import android.content.Context;
import android.graphics.Canvas;

public class ColorsGame {

    private final PooledEngine engine = new PooledEngine();
    private final World world = new World(engine);

    private Context mContext;

    public ColorsGame(Context context) {
        mContext = context;

        Assets.load(context);

        engine.addSystem(new GravitySystem());
        engine.addSystem(new PlayerJumpSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new PlayerFloorSystem());
//        engine.addSystem(new CameraFollowSystem());
        engine.addSystem(new ObstacleCollisionSystem());
        engine.addSystem(new RenderingSystem());

        world.create();
    }

    public void setSize(int width, int height) {
        if(width != Camera.width || height != Camera.height) {
            Camera.width = width;
            Camera.height = height;

            Camera.x = -width/2;
        }
    }

    public void update(Canvas canvas, long deltaMillis) {
        if (deltaMillis > 100) deltaMillis = 100;

        Camera.canvas = canvas;

        engine.update(deltaMillis / 1000f);
    }

    //

    public Context getContext() {
        return mContext;
    }
}
