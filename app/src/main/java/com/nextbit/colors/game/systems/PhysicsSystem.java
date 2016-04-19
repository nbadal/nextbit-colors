package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.nextbit.colors.game.Physics;
import com.nextbit.colors.game.components.PhysicsComponent;

public class PhysicsSystem extends BaseEntitySystem {

    public PhysicsSystem() {
        super(Aspect.all(PhysicsComponent.class));
    }

    @Override
    protected void processSystem() {
        Physics.world.update(getWorld().getDelta());
    }
}
