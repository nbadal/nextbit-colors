package com.nextbit.colors.game.components;

import com.artemis.Component;
import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.util.GravityMath;

public class PlayerComponent extends Component {
    public static final double SIZE = 1;
    public static final double JUMP_ANIM_LENGTH = (GravityMath.JUMP_TIME * 1000) / 2;
    public static final double JUMP_WIGGLE_RANGE = Math.toRadians(10);

    public int jumpCount;
    public long jumpTime;


    public boolean isAlive = true;
    public boolean deathHandled;
    public boolean respawnHandled;

    public void kill() {
        isAlive = false;

        deathHandled = false;
        respawnHandled = false;
    }

    public void respawn() {
        isAlive = true;

        respawnHandled = false;


        jumpCount = 0;
    }
}
