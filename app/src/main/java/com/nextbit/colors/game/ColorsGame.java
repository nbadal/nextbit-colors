package com.nextbit.colors.game;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.nextbit.colors.game.systems.CameraFollowSystem;
import com.nextbit.colors.game.systems.GravitySystem;
import com.nextbit.colors.game.systems.MovementSystem;
import com.nextbit.colors.game.systems.ObstacleCollisionSystem;
import com.nextbit.colors.game.systems.PlayerFloorSystem;
import com.nextbit.colors.game.systems.PlayerJumpSystem;
import com.nextbit.colors.game.systems.RenderingSystem;

import android.content.Context;
import android.graphics.Canvas;

import java.util.Random;

public class ColorsGame {

    public static final Random random = new Random(System.currentTimeMillis());

    private WorldConfiguration config = new WorldConfigurationBuilder()
            .with(
                    new GravitySystem(),
                    new PlayerJumpSystem(),
                    new MovementSystem(),
                    new PlayerFloorSystem(),
                    new CameraFollowSystem(),
                    new ObstacleCollisionSystem(),
                    new RenderingSystem()
            )
            .build();
    private final World world = new World(config);

    private Context mContext;

    public ColorsGame(Context context) {
        mContext = context;

        Assets.load(context);

        int player = Entities.createPlayer(world);
        Entities.createCamera(world, player);

        Entities.createRing(world, 90, 0, 650, 200, 230);

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

        world.setDelta(deltaMillis / 1000f);
        world.process();
    }

    //

    public Context getContext() {
        return mContext;
    }
}
