package com.nextbit.colors.game.obstacles;

import com.artemis.World;
import com.nextbit.colors.game.Entities;

import java.util.HashSet;
import java.util.Set;

public enum Obstacles {
    BIG_CIRCLE,
    ;

    public static final double SPACING = 500;
    public static final double CIRCLE_SPEED = Math.PI / 2;
    public static final double RING_WIDTH = 40;
    public static final double BIG_RADIUS = 320;

    public Set<Integer> create(World world, double y, double difficulty) {
        HashSet<Integer> ids = new HashSet<>();

        switch (this) {
            case BIG_CIRCLE:
                ids.addAll(Entities.createRing(world, CIRCLE_SPEED * difficulty,
                        0, y + BIG_RADIUS, BIG_RADIUS - RING_WIDTH, BIG_RADIUS));
                break;
        }

        return ids;
    }

    public double height() {
        switch(this) {
            case BIG_CIRCLE:
                return BIG_RADIUS * 2;
            default:
                throw new IllegalArgumentException("Missing height for "+this);
        }
    }
}
