package com.nextbit.colors.game.obstacles;

import com.artemis.World;
import com.nextbit.colors.game.ColorsGame;
import com.nextbit.colors.game.Entities;
import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.util.GravityMath;

import java.util.HashSet;
import java.util.Set;

public enum Obstacles {
    BIG_CIRCLE,
    ;

    public static final double SPACING = GravityMath.JUMP_HEIGHT * 3.0;
    public static final double CIRCLE_SPEED = Math.PI / 2;
    public static final double RING_WIDTH = 0.3;
    public static final double BIG_RADIUS = 4.0;

    public Set<Integer> create(World world, double y, double difficulty) {
        HashSet<Integer> ids = new HashSet<>();

        switch (this) {
            case BIG_CIRCLE:
                ids.addAll(Entities.createRing(world, CIRCLE_SPEED * difficulty,
                        0, y + BIG_RADIUS, BIG_RADIUS - RING_WIDTH, BIG_RADIUS));
                if(ColorsGame.random.nextBoolean()) {
                    ids.add(Entities.createSwitch(world, y + BIG_RADIUS));
                } else {
                    ids.add(Entities.createPhone(world, y + BIG_RADIUS));
                }
                break;
        }

        return ids;
    }

    public double height() {
        switch(this) {
            case BIG_CIRCLE:
                return BIG_RADIUS * 2;
            default:
                throw new IllegalArgumentException("Missing heightMeters for "+this);
        }
    }
}
