package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.nextbit.colors.game.Physics;
import com.nextbit.colors.game.components.ObstacleComponent;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;

import android.util.Pair;

public class PhysicsSystem extends BaseEntitySystem {

    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<ObstacleComponent> obstacleM;

    public PhysicsSystem() {
        super(Aspect.all(PhysicsComponent.class));
    }

    @Override
    protected void processSystem() {
        Physics.WORLD.update(getWorld().getDelta());
        for(Pair<Integer, Integer> collision : Physics.COLLISIONS) {
            final int player, obstacle;
            if(playerM.has(collision.first)) {
                player = collision.first;
                obstacle = collision.second;
            } else {
                player = collision.second;
                obstacle = collision.first;
            }

            PlayerComponent pc = playerM.get(player);
            ObstacleComponent oc = obstacleM.get(obstacle);

            if(pc.color != oc.color) {
                pc.kill();
            }
        }
        Physics.COLLISIONS.clear();
    }
}
