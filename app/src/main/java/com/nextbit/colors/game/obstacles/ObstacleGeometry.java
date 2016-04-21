package com.nextbit.colors.game.obstacles;

import com.nextbit.colors.game.systems.PhysicsSystem;
import com.nextbit.colors.game.util.EntityBody;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;

public class ObstacleGeometry {

    private static final int RING_SUBDIVS = 8;

    /** Create a ring made of polygonal segments */
    public static Body createRingSegment(int entityId, RingSegment info) {
        Body body = new EntityBody(entityId, RING_SUBDIVS);
        final double subDivAmt = info.sweep / RING_SUBDIVS;
        for(int i = 0; i < RING_SUBDIVS; i++) {
            double angle1 = i * subDivAmt;
            double angle2 = (i + 1) * subDivAmt;

            Vector2 inner1 = createPolarPoint(angle1, info.innerRadius);
            Vector2 inner2 = createPolarPoint(angle2, info.innerRadius);
            Vector2 outer1 = createPolarPoint(angle1, info.outerRadius);
            Vector2 outer2 = createPolarPoint(angle2, info.outerRadius);

            BodyFixture bf = body.addFixture(new Polygon(inner1, outer1, outer2, inner2));
            bf.setSensor(true);
            bf.setFilter(new CategoryFilter(PhysicsSystem.CAT_OBSTACLE, PhysicsSystem.CAT_PLAYER));
        }
        return body;
    }

    private static Vector2 createPolarPoint(double angle, double radius) {
        return new Vector2(radius * Math.cos(angle), radius * Math.sin(angle));
    }
}
