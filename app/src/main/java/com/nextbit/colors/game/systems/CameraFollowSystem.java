package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.components.CameraFollowComponent;
import com.nextbit.colors.game.components.PhysicsComponent;

public class CameraFollowSystem extends IteratingSystem {

    private static final double SPEED = 3;

    private ComponentMapper<PhysicsComponent> transformM;
    private ComponentMapper<CameraFollowComponent> cameraM;

    public CameraFollowSystem() {
        super(Aspect.all(CameraFollowComponent.class));
    }

    @Override
    protected void process(int entity) {
        CameraFollowComponent cfc = cameraM.get(entity);
        PhysicsComponent target = transformM.get(cfc.target);

        double distanceAboveCam = target.body.getTransform().getTranslationY() - Camera.y;
        float maxY = Camera.height / 2;
        if(distanceAboveCam > maxY) {
            double amountToMove = distanceAboveCam - maxY;
            Camera.y += amountToMove * Math.min(1.0, getWorld().getDelta() * SPEED);
        }
    }
}
