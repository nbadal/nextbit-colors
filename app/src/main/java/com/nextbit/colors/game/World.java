package com.nextbit.colors.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.nextbit.colors.game.components.CameraFollowComponent;
import com.nextbit.colors.game.components.GravityComponent;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.TransformComponent;
import com.nextbit.colors.game.systems.PlayerJumpSystem;

import java.util.Random;

public class World {
    private final PooledEngine engine;
    private final Random random;

    public World(PooledEngine engine) {
        this.engine = engine;
        this.random = new Random(System.currentTimeMillis());
    }

    public void create() {
        Entity player = createPlayer();
        createCamera(player);
    }

    private Entity createPlayer() {
        Entity entity = engine.createEntity();

        TransformComponent transform = engine.createComponent(TransformComponent.class);
        RenderComponent render = engine.createComponent(RenderComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        GravityComponent gravity = engine.createComponent(GravityComponent.class);
        MovementComponent movement = engine.createComponent(MovementComponent.class);

        render.sprite = Assets.player;

//        transform.position.y = 400;
        movement.velocity.y = PlayerJumpSystem.JUMP_VELOCITY;

        entity.add(transform);
        entity.add(render);
        entity.add(player);
        entity.add(gravity);
        entity.add(movement);

        engine.addEntity(entity);
        return entity;
    }

    private void createCamera(Entity target) {
        Entity entity = engine.createEntity();

        CameraFollowComponent cfc = engine.createComponent(CameraFollowComponent.class);
        cfc.target = target;

        entity.add(cfc);

        engine.addEntity(entity);
    }
}
