package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Entities;
import com.nextbit.colors.game.components.ColorComponent;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;

public class PlayerDeathSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<ColorComponent> colorM;

    public PlayerDeathSystem() {
        super(Aspect.all(PlayerComponent.class, PhysicsComponent.class, RenderComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent pc = playerM.get(entityId);
        if(pc.isAlive || pc.deathHandled) {
            return;
        }
        pc.deathHandled = true;

        // Lose the magic color
        colorM.get(entityId).color = null;

        // Spawn Death Particles

        // Spawn Game Over
        Entities.createGameOver(getWorld());
    }
}
