package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Input;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.util.GravityMath;

import android.util.Log;


public class PlayerJumpSystem extends IteratingSystem {

    public static final float JUMP_VELOCITY = GravityMath.JUMP_VELOCITY;
    private ComponentMapper<MovementComponent> movementM;

    public PlayerJumpSystem() {
        super(Aspect.all(PlayerComponent.class, MovementComponent.class));
    }

    @Override
    protected void process(int entity) {
        if(Input.justTouched()) {
            MovementComponent mov = movementM.get(entity);

            mov.velocity.y = JUMP_VELOCITY;
        }
    }
}
