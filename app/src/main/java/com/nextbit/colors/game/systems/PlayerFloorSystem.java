package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.TransformComponent;

public class PlayerFloorSystem extends IteratingSystem {

    public static final float FLOOR_POS = 275;

    private ComponentMapper<MovementComponent> movementM;
    private ComponentMapper<TransformComponent> transformM;

    public PlayerFloorSystem() {
        super(Aspect.all(PlayerComponent.class, TransformComponent.class, MovementComponent
                .class));
    }

    @Override
    protected void process(int entity) {
        MovementComponent move = movementM.get(entity);
        TransformComponent trans = transformM.get(entity);

        if(trans.position.y <= FLOOR_POS) {
            trans.position.y = FLOOR_POS;
            move.velocity.y = 0;
        }
    }
}
