package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.PhysicsComponent;

public class PlayerFloorSystem extends IteratingSystem {

    public static final double FLOOR_POS = 2.0;

    private ComponentMapper<PhysicsComponent> transformM;

    public PlayerFloorSystem() {
        super(Aspect.all(PlayerComponent.class, PhysicsComponent.class));
    }

    @Override
    protected void process(int entity) {
        PhysicsComponent trans = transformM.get(entity);

        double playerY = trans.body.getTransform().getTranslationY();
        double playerFootY = playerY - 0.5;
        double distToFloor = playerFootY - FLOOR_POS;

        if(distToFloor < 0) {
            trans.body.translate(0, -distToFloor);
            trans.body.setLinearVelocity(0, 0);
        }
    }
}
