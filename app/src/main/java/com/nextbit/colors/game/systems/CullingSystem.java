package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.RenderComponent;

public class CullingSystem extends IteratingSystem {

    private ComponentMapper<PhysicsComponent> physicsM;
    private ComponentMapper<RenderComponent> renderM;

    public CullingSystem() {
        super(Aspect.all(PhysicsComponent.class, RenderComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PhysicsComponent phys = physicsM.get(entityId);
        RenderComponent render = renderM.get(entityId);


        double y = phys.body.getTransform().getTranslationY();
        double height = render.sprite.getHeightMeters();
        boolean onscreen = !(y - height > Camera.y + Camera.heightMeters || y + height < Camera.y);

        render.onScreen = onscreen;
        phys.body.setActive(onscreen);
    }
}
