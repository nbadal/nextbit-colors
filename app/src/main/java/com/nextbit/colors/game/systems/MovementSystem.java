package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.TransformComponent;
import com.nextbit.colors.game.util.Vector2;

public class MovementSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<MovementComponent> movementM;

    private Vector2 tmp = new Vector2();

    public MovementSystem() {
        super(Aspect.all(TransformComponent.class, MovementComponent.class));
    }

    @Override
    protected void process(int entity) {
        float delta = getWorld().getDelta();

        TransformComponent pos = transformM.get(entity);
        MovementComponent mov = movementM.get(entity);

        tmp.set(mov.accel).scl(delta);
        mov.velocity.add(tmp);

        tmp.set(mov.velocity).scl(delta);
        pos.position.add(tmp);
        pos.position.y = Math.max(0, pos.position.y);

        pos.rotation += mov.rotationSpeed * delta;
    }
}
