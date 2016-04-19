package com.nextbit.colors.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.components.CameraFollowComponent;
import com.nextbit.colors.game.components.TransformComponent;

public class CameraFollowSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<CameraFollowComponent> cameraM;

    public CameraFollowSystem() {
        super(Family.all(CameraFollowComponent.class).get());

        transformM = ComponentMapper.getFor(TransformComponent.class);
        cameraM = ComponentMapper.getFor(CameraFollowComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
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
