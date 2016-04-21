package com.nextbit.colors.game.obstacles;

import com.artemis.World;
import com.nextbit.colors.game.ColorsGame;
import com.nextbit.colors.game.Entities;
import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.util.GravityMath;

import java.util.HashSet;
import java.util.Set;

public enum ObstacleType {
    TOUCHING_CIRCLES,
    CIRCLE_EMPTY,
    CIRCLE_SWITCH,
    CIRCLE_PHONE,
    CIRCLE_2X,
    CIRCLE_2X_PHONE,
    ;

    // TODO: make these mean something
    public static final double DIFFICULTY_MEDIUM = 3.0;
    public static final double DIFFICULTY_HARD = 5.0;

    public static final double SPACING = GravityMath.JUMP_HEIGHT * 3.0;


    public static final double CIRCLE_SPEED = Math.PI / 2;
    public static final double TOUCHING_SPEED = CIRCLE_SPEED;

    public static final double RING_WIDTH = 0.3;
    public static final double RING_SPACING = 0.2;
    public static final double BIG_RADIUS = 4.0;
    public static final double SMALL_RADIUS = 3;

    public Set<Integer> create(World world, double y, double difficulty) {
        HashSet<Integer> ids = new HashSet<>();

        switch (this) {
            case CIRCLE_SWITCH:
                ids.addAll(CIRCLE_EMPTY.create(world, y, difficulty));
                ids.add(Entities.createSwitch(world, y + BIG_RADIUS));
                break;
            case CIRCLE_PHONE:
                ids.addAll(CIRCLE_EMPTY.create(world, y, difficulty));
                ids.add(Entities.createPhone(world, y + BIG_RADIUS));
                break;
            case CIRCLE_EMPTY:
                ids.addAll(Entities.createRing(world, CIRCLE_SPEED * difficulty,
                        0, y + BIG_RADIUS, BIG_RADIUS - RING_WIDTH, BIG_RADIUS));
                break;

            case CIRCLE_2X_PHONE:
                ids.add(Entities.createPhone(world, y + BIG_RADIUS));
                ids.addAll(CIRCLE_2X.create(world, y, difficulty));
                break;
            case CIRCLE_2X: {
                double radius = BIG_RADIUS;
                double speed = CIRCLE_SPEED * difficulty;
                final GameColor startColor = GameColor.random();
                final double circleY = y + BIG_RADIUS;

                for (int i = 0; i < 2; i++) {
                    ids.addAll(Entities.createRing(world, startColor, speed,
                            0, circleY, radius - RING_WIDTH, radius));
                    speed *= -1;
                    radius -= RING_WIDTH + RING_SPACING;
                }
                break;
            }
            case TOUCHING_CIRCLES: {
                final double radius = SMALL_RADIUS;
                final double speedLeft, speedRight;
                final GameColor colorLeft, colorRight;
                speedLeft = TOUCHING_SPEED;
                speedRight = -TOUCHING_SPEED;
                colorLeft = colorRight = GameColor.random();
//                // OTHER (IMPOSSIBLE?) MODES
//                // DOWN
//                speedLeft = -TOUCHING_SPEED;
//                speedRight = TOUCHING_SPEED;
//                colorLeft = colorRight = GameColor.random();
//                // BOTH
//                speedLeft = speedRight = TOUCHING_SPEED;
//                colorLeft = GameColor.random();
//                colorRight = colorLeft.opposite();
                final double circleY = y + SMALL_RADIUS;

                ids.addAll(Entities.createRing(world, colorLeft, speedLeft,
                        -SMALL_RADIUS, circleY, radius - RING_WIDTH, radius));
                ids.addAll(Entities.createRing(world, colorRight, speedRight,
                        SMALL_RADIUS, circleY, radius - RING_WIDTH, radius));
                break;
            }
        }

        return ids;
    }

    public double height() {
        switch(this) {
            case CIRCLE_2X:
            case CIRCLE_2X_PHONE:
            case CIRCLE_EMPTY:
            case CIRCLE_SWITCH:
            case CIRCLE_PHONE:
                return BIG_RADIUS * 2;
            case TOUCHING_CIRCLES:
                return SMALL_RADIUS * 2;
            default:
                throw new IllegalArgumentException("Missing heightMeters for "+this);
        }
    }
}
