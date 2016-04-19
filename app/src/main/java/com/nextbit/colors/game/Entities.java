package com.nextbit.colors.game;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.nextbit.colors.game.components.CameraFollowComponent;
import com.nextbit.colors.game.components.GravityComponent;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.TransformComponent;

import java.util.HashMap;

public enum Entities {
    CAMERA,
    PLAYER;

    private static final HashMap<Entities, Archetype> archetypes = new HashMap<>();

    public int create(World world) {
        if(!archetypes.containsKey(this)) {
            archetypes.put(this, createArchetype(world));
        }

        int id = world.create(archetypes.get(this));

        init(world, this, id);

        return id;
    }

    @SuppressWarnings("unchecked")
    private Archetype createArchetype(World world) {
        switch(this) {
            case PLAYER:
                return new ArchetypeBuilder()
                        .add(TransformComponent.class,
                                RenderComponent.class,
                                PlayerComponent.class,
                                GravityComponent.class,
                                MovementComponent.class)
                        .build(world);

            case CAMERA:
                return new ArchetypeBuilder()
                        .add(CameraFollowComponent.class)
                        .build(world);
        }

        return null;
    }

    private void init(World world, Entities type, int id) {
        switch (type) {
            case PLAYER:
                ComponentMapper.getFor(RenderComponent.class, world).get(id).sprite = Assets.player;
        }
    }
}
