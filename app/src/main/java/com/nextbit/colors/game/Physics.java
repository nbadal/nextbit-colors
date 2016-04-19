package com.nextbit.colors.game;

import com.nextbit.colors.game.util.GravityMath;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

public class Physics {
    public static final World world = new World();
    static {
        world.setGravity(new Vector2(0, GravityMath.GRAVITY));
    }
}
