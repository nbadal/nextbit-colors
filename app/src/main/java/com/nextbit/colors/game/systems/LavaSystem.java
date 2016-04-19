package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.PhysicsComponent;

public class LavaSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<PhysicsComponent> transformM;

    public LavaSystem() {
        super(Aspect.all(PlayerComponent.class, PhysicsComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent pc = playerM.get(entityId);
        if(!pc.isAlive) {
            return;
        }
        PhysicsComponent t = transformM.get(entityId);

        if(t.body.getTransform().getTranslationY() < Camera.y) {
            pc.kill();
        }
    }
}
