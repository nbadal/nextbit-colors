package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Entities;
import com.nextbit.colors.game.components.ColorComponent;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.util.GravityMath;

public class PlayerDeathSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<PhysicsComponent> physM;
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

        // Little death hop
        physM.get(entityId).body.setLinearVelocity(0, GravityMath.JUMP_VELOCITY);

        // Lose the magic color
        colorM.get(entityId).color = null;

        // Spawn Game Over
        Entities.createGameOver(getWorld());
    }
}
