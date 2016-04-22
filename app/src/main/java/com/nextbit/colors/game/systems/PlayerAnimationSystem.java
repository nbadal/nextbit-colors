package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.graphics.PlayerSprite;

public class PlayerAnimationSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<RenderComponent> renderM;
    private ComponentMapper<PhysicsComponent> physicsM;

    public PlayerAnimationSystem() {
        super(Aspect.all(PlayerComponent.class, PhysicsComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent pc = playerM.get(entityId);
        PhysicsComponent phys = physicsM.get(entityId);
        RenderComponent rc = renderM.get(entityId);

        long now = System.currentTimeMillis();
        final double animPos = (now - pc.jumpTime) / PlayerComponent.JUMP_ANIM_LENGTH;

        double angle = 0;
        if(animPos > 0. && animPos < 1.) {
            angle = Math.sin(2 * Math.PI * animPos) * PlayerComponent.JUMP_WIGGLE_RANGE;
        }
        if(!pc.isAlive) {
            angle = Math.PI; // Dead fish
        }
        phys.body.getTransform().setRotation(angle);

        // Idle
        ((PlayerSprite)rc.sprite).bodyOffset = (float) Math.sin(2 * Math.PI * now / 1000) * 2;
    }
}
