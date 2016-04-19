package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.TransformComponent;

public class PlayerDeathSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<RenderComponent> renderM;

    public PlayerDeathSystem() {
        super(Aspect.all(PlayerComponent.class, TransformComponent.class, RenderComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent pc = playerM.get(entityId);
        if(pc.isAlive || pc.deathHandled) {
            return;
        }
        pc.deathHandled = true;

        TransformComponent transform = transformM.get(entityId);
        RenderComponent render = renderM.get(entityId);

        // Hide Player
        render.enabled = false;

        // Spawn Death Particles

        // Spawn Game Over
    }
}
