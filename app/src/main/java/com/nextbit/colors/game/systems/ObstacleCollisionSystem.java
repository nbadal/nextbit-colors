package com.nextbit.colors.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.nextbit.colors.game.Obstacle;
import com.nextbit.colors.game.components.ObstacleComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.TransformComponent;
import com.nextbit.colors.game.obstacles.Rectangle;
import com.nextbit.colors.game.obstacles.RingSegment;

import android.util.Log;

public class ObstacleCollisionSystem extends EntitySystem {
    private final ComponentMapper<PlayerComponent> playerM;
    private final ComponentMapper<ObstacleComponent> obstacleM;
    private final ComponentMapper<TransformComponent> transformM;

    private ImmutableArray<Entity> obstacles;
    private ImmutableArray<Entity> players;

    private static final Vector2 tmp = new Vector2();

    public ObstacleCollisionSystem(/* TODO: pass listener in here? */) {
        playerM = ComponentMapper.getFor(PlayerComponent.class);
        obstacleM = ComponentMapper.getFor(ObstacleComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        // TODO: make sure these arrays aren't being accessed to early..
        obstacles = engine.getEntitiesFor(Family.all(ObstacleComponent.class, TransformComponent
                .class).get());
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class, TransformComponent
                .class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);


        for (Entity player : players) {
            PlayerComponent pc = playerM.get(player);
            TransformComponent playerTransform = transformM.get(player);

            for (Entity obstacle : obstacles) {
                ObstacleComponent oc = obstacleM.get(obstacle);
                TransformComponent obstacleTransform = transformM.get(obstacle);

                if(checkCollision(pc, playerTransform, oc, obstacleTransform)) {
                    // TODO: notify listener
                    Log.d("Collision", "COLLIDED");
                    return;
                }
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
