package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Input;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.util.GravityMath;

import org.dyn4j.geometry.Vector2;

public class PlayerTapSystem extends IteratingSystem {

    public static final Vector2 JUMP_VECTOR = new Vector2(0, GravityMath.JUMP_VELOCITY);
    private ComponentMapper<PhysicsComponent> physicsM;
    private ComponentMapper<PlayerComponent> playerM;

    public PlayerTapSystem() {
        super(Aspect.all(PlayerComponent.class, PhysicsComponent.class));
    }

    @Override
    protected void process(int entity) {
        if(Input.justTouched()) {
            PlayerComponent player = playerM.get(entity);

            if(player.isAlive) {
                PhysicsComponent phys = physicsM.get(entity);
                // Jump!
                double factor = 1d;
                if (player.jumpCount == 0) {
                    factor = 2.0d; // First jump is double
                }
                factor *= 100;
                phys.body.setLinearVelocity(JUMP_VECTOR.copy().multiply(factor));
                player.jumpCount++;
            } else {
                // Flag for respawn
                player.respawn();
            }
        }
    }
}
