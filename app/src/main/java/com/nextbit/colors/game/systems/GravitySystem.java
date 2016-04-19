package com.nextbit.colors.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.nextbit.colors.game.components.GravityComponent;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.util.GravityMath;

public class GravitySystem extends IteratingSystem {
    private static final Vector2 GRAVITY = new Vector2(0, GravityMath.GRAVITY);

    private final ComponentMapper<MovementComponent> movementM;

    private final Vector2 tmp = new Vector2();

    public GravitySystem() {
        super(Family.all(GravityComponent.class, MovementComponent.class).get());

        movementM = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent mov = movementM.get(entity);
        tmp.set(GRAVITY).scl(deltaTime);
        mov.velocity.add(tmp);
    }
}
