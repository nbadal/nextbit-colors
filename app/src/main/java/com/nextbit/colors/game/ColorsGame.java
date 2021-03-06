package com.nextbit.colors.game;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.nextbit.colors.game.graphics.Assets;
import com.nextbit.colors.game.systems.BackgroundRenderSystem;
import com.nextbit.colors.game.systems.CameraFollowSystem;
import com.nextbit.colors.game.systems.CullingSystem;
import com.nextbit.colors.game.systems.EntityRenderSystem;
import com.nextbit.colors.game.systems.LavaSystem;
import com.nextbit.colors.game.systems.LevelGeneratorSystem;
import com.nextbit.colors.game.systems.PhysicsSystem;
import com.nextbit.colors.game.systems.PlayerAnimationSystem;
import com.nextbit.colors.game.systems.PlayerDeathSystem;
import com.nextbit.colors.game.systems.PlayerFloorSystem;
import com.nextbit.colors.game.systems.PlayerRespawnSystem;
import com.nextbit.colors.game.systems.PlayerScoreSystem;
import com.nextbit.colors.game.systems.PlayerTapSystem;
import com.nextbit.colors.game.util.GravityMath;

import android.content.Context;
import android.graphics.Canvas;

import java.util.Random;

public class ColorsGame implements ScoreListener {

    public static int highScore;

    public static final Random random = new Random(System.currentTimeMillis());

    private WorldConfiguration config = new WorldConfigurationBuilder()
            .with(
                    new PlayerTapSystem(),
                    new PlayerAnimationSystem(),
                    new PhysicsSystem(),
                    new PlayerScoreSystem(this),
                    new CullingSystem(),
                    new PlayerFloorSystem(),
                    new CameraFollowSystem(),
                    new LevelGeneratorSystem(),
                    new PlayerRespawnSystem(),
                    new LavaSystem(),
                    new PlayerDeathSystem(),
                    new BackgroundRenderSystem(),
                    new EntityRenderSystem()
            )
            .build();
    private final World world = new World(config);

    private Context mContext;
    private ScoreListener mScoreListener;

    public ColorsGame(Context context) {
        mContext = context;

        Assets.load(context);

        int player = Entities.createPlayer(world);
        Entities.createCamera(world, player);
        Entities.createScore(world);
        Entities.createSwitch(world, PlayerFloorSystem.FLOOR_POS + GravityMath.JUMP_HEIGHT * 2);
    }

    public void setScoreListener(ScoreListener listener) {
        mScoreListener = listener;
    }

    @Override
    public void onScoreChanged(int score) {
        if(mScoreListener != null) {
            mScoreListener.onScoreChanged(score);
        }
    }

    public void setSize(int widthPx, int heightPx) {
        final double widthMeters = widthPx * Assets.pxToMeters;
        final double heightMeters = heightPx * Assets.pxToMeters;

        if(widthMeters != Camera.widthMeters || heightMeters != Camera.heightMeters) {
            Camera.widthMeters = widthMeters ;
            Camera.heightMeters = heightMeters;

            Camera.x = -widthMeters/2;
        }
    }

    public void update(Canvas canvas, long deltaMillis) {
        Camera.canvas = canvas;

        world.setDelta(deltaMillis / 1000f);
        world.process();
    }

    //

    public Context getContext() {
        return mContext;
    }

}
