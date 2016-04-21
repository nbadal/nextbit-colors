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

        if(trans.body.getTransform().getTranslationY() <= FLOOR_POS) {
            trans.body.getTransform().setTranslationY(FLOOR_POS);
            trans.body.setLinearVelocity(0, 0);
        }
    }
}
