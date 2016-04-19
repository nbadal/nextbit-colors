package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.PhysicsComponent;

public class PlayerRespawnSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<PhysicsComponent> transformM;
    private ComponentMapper<RenderComponent> renderM;

    public PlayerRespawnSystem() {
        super(Aspect.all(PlayerComponent.class, PhysicsComponent.class, RenderComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent pc = playerM.get(entityId);
        if(!pc.isAlive || pc.respawnHandled) {
            return;
        }
        pc.respawnHandled = true;

        PhysicsComponent transform = transformM.get(entityId);
        RenderComponent render = renderM.get(entityId);

        // Reset Positions
        transform.body.getTransform().setTranslation(0, 0);
        Camera.y = 0;
        transform.body.setLinearVelocity(0, 0);

        // Respawn all switches

        // Respawn all stars

        // Remove Death Particles

        // Hide Game Over

        // Show Player
        render.enabled = true;
    }
}
