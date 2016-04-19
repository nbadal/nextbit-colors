package com.nextbit.colors.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.TransformComponent;

public class MovementSystem extends IteratingSystem {
    private final ComponentMapper<TransformComponent> transformM;
    private final ComponentMapper<MovementComponent> movementM;

    private Vector2 tmp = new Vector2();

    public MovementSystem() {
        super(Family.all(TransformComponent.class, MovementComponent.class).get());

        transformM = ComponentMapper.getFor(TransformComponent.class);
        movementM = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = transformM.get(entity);
        MovementComponent mov = movementM.get(entity);

        tmp.set(mov.accel).scl(deltaTime);
        mov.velocity.add(tmp);

        tmp.set(mov.velocity).scl(deltaTime);
        pos.position.add(tmp);
        pos.position.y = Math.max(0, pos.position.y);

        pos.rotation += mov.rotationSpeed;
    }
}
