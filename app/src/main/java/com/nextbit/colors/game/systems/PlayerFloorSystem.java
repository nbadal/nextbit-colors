package com.nextbit.colors.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.TransformComponent;

public class PlayerFloorSystem extends IteratingSystem {

    public static final float FLOOR_POS = 100;

    private final ComponentMapper<MovementComponent> movementM;
    private final ComponentMapper<TransformComponent> transformM;

    public PlayerFloorSystem() {
        super(Family.all(PlayerComponent.class, TransformComponent.class, MovementComponent
                .class).get());

        movementM = ComponentMapper.getFor(MovementComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent move = movementM.get(entity);
        TransformComponent trans = transformM.get(entity);

        if(trans.position.y <= FLOOR_POS) {
            trans.position.y = FLOOR_POS;
            move.velocity.y = 0;
        }
    }
}
