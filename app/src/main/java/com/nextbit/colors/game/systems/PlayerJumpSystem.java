package com.nextbit.colors.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nextbit.colors.game.Input;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.util.GravityMath;

import android.util.Log;


public class PlayerJumpSystem extends IteratingSystem {

    public static final float JUMP_VELOCITY = GravityMath.JUMP_VELOCITY;
    private final ComponentMapper<MovementComponent> movementM;

    public PlayerJumpSystem() {
        super(Family.all(PlayerComponent.class, MovementComponent.class).get());

        movementM = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(Input.justTouched()) {
            Log.d("I", "JUMP!");
            MovementComponent mov = movementM.get(entity);

            mov.velocity.y = JUMP_VELOCITY;
        }
    }
}
