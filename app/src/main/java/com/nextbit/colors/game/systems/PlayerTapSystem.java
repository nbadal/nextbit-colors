package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Input;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.util.GravityMath;

public class PlayerTapSystem extends IteratingSystem {

    public static final float JUMP_VELOCITY = GravityMath.JUMP_VELOCITY;
    private ComponentMapper<MovementComponent> movementM;
    private ComponentMapper<PlayerComponent> playerM;

    public PlayerTapSystem() {
        super(Aspect.all(PlayerComponent.class, MovementComponent.class));
    }

    @Override
    protected void process(int entity) {
        if(Input.justTouched()) {
            MovementComponent mov = movementM.get(entity);
            PlayerComponent player = playerM.get(entity);

            if(player.isAlive) {
                // Jump!
                mov.velocity.y = JUMP_VELOCITY;

                if (player.jumpCount == 0) {
                    mov.velocity.y *= 2.0f; // First jump is double
                }
                player.jumpCount++;
            } else {
                // Flag for respawn
                player.respawn();
            }
        }
    }
}
