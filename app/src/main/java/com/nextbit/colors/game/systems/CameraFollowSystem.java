package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.components.CameraFollowComponent;
import com.nextbit.colors.game.components.TransformComponent;

public class CameraFollowSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<CameraFollowComponent> cameraM;

    public CameraFollowSystem() {
        super(Aspect.all(CameraFollowComponent.class));
    }

    @Override
    protected void process(int entity) {
        CameraFollowComponent cfc = cameraM.get(entity);
        TransformComponent target = transformM.get(cfc.target);

        float distanceAboveCam = target.position.y - Camera.y;
        float maxY = Camera.height / 2;
        if(distanceAboveCam > maxY) {
            float amountToMove = distanceAboveCam - maxY;
            Camera.y += amountToMove;
        }
    }
}
