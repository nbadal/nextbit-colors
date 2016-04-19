package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.nextbit.colors.game.Obstacle;
import com.nextbit.colors.game.components.ObstacleComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.TransformComponent;
import com.nextbit.colors.game.obstacles.Rectangle;
import com.nextbit.colors.game.obstacles.RingSegment;
import com.nextbit.colors.game.util.Vector2;

import android.util.Log;

public class ObstacleCollisionSystem extends IteratingSystem {
    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<ObstacleComponent> obstacleM;
    private ComponentMapper<TransformComponent> transformM;

    private static final Vector2 tmp = new Vector2();

    public ObstacleCollisionSystem(/* TODO: pass listener in here? */) {
        super(Aspect.all(PlayerComponent.class, TransformComponent.class));
    }

    @Override
    protected void process(int player) {
        PlayerComponent pc = playerM.get(player);
        TransformComponent playerTransform = transformM.get(player);

        IntBag entities = getWorld().getAspectSubscriptionManager().get(
                Aspect.all(ObstacleComponent.class, TransformComponent.class)).getEntities();
        for (int i = 0; i < entities.size(); i++) {
            int obstacle = entities.get(i);

            ObstacleComponent oc = obstacleM.get(obstacle);
            TransformComponent obstacleTransform = transformM.get(obstacle);

            if(checkCollision(pc, playerTransform, oc, obstacleTransform)) {
                // TODO: notify listener
                Log.d("Collision", "COLLIDED");
                return;
            }
        }
    }

    private boolean checkCollision(PlayerComponent pc, TransformComponent playerTransform,
                                   ObstacleComponent oc, TransformComponent obstacleTransform) {
        if(oc.color == pc.color) {
            return false;
        }

        Obstacle obstacle = oc.obstacle;
        if(obstacle instanceof Rectangle) {
            return checkRectangle(pc, playerTransform, (Rectangle) obstacle, obstacleTransform);
        } else if(obstacle instanceof RingSegment) {
            return checkRingSegment(pc, playerTransform, (RingSegment) obstacle, obstacleTransform);
        } else {
            throw new RuntimeException("Missing obstacle collision check for "+obstacle);
        }
    }

    private boolean checkRectangle(PlayerComponent pc, TransformComponent playerTransform,
                                   Rectangle rectangle, TransformComponent rectTransform) {
        final Vector2 ot = rectTransform.position;

        final float left = ot.x - rectangle.width/2;
        final float right = ot.x + rectangle.width/2;
        final float top = ot.y + rectangle.height/2;
        final float bottom = ot.y - rectangle.height/2;

        if(checkPoint(playerTransform, left, top)) return true;
        if(checkPoint(playerTransform, left, bottom)) return true;
        if(checkPoint(playerTransform, right, top)) return true;
        if(checkPoint(playerTransform, right, bottom)) return true;

        return false;
    }

    private boolean checkRingSegment(PlayerComponent pc, TransformComponent playerTransform,
                                     RingSegment ring, TransformComponent ringTransform) {
        float playerAngleToOrigin = playerTransform.position.angle(ringTransform.position);

        float closestRingAngle = playerAngleToOrigin;

        return false;
    }

    private boolean checkPoint(TransformComponent playerTransform, float x, float y) {
        tmp.set(x, y);
        return tmp.dst(playerTransform.position) <= PlayerComponent.COLLISION_RADIUS;
    }
}
