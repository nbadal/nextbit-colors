package com.nextbit.colors.game.components;

import com.artemis.Component;
import com.nextbit.colors.game.GameColor;

public class PlayerComponent extends Component {
    public static final float COLLISION_RADIUS = 45;

    public GameColor color;
    public int jumpCount;

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
