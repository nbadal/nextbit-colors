package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.components.GravityComponent;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.util.GravityMath;
import com.nextbit.colors.game.util.Vector2;

public class GravitySystem extends IteratingSystem {
    private static final Vector2 GRAVITY = new Vector2(0, GravityMath.GRAVITY);

    private ComponentMapper<MovementComponent> movementM;

    private final Vector2 tmp = new Vector2();

    public GravitySystem() {
        super(Aspect.all(GravityComponent.class, MovementComponent.class));
    }

    @Override
    protected void process(int entityId) {
        MovementComponent mov = movementM.get(entityId);
        tmp.set(GRAVITY).scl(getWorld().getDelta());
        mov.velocity.add(tmp);

    }
}
