package com.nextbit.colors.game;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.nextbit.colors.game.components.CameraFollowComponent;
import com.nextbit.colors.game.components.GravityComponent;
import com.nextbit.colors.game.components.MovementComponent;
import com.nextbit.colors.game.components.ObstacleComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.TransformComponent;
import com.nextbit.colors.game.obstacles.RingSegment;
import com.nextbit.colors.game.util.Vector2;

import android.graphics.Color;

import java.util.HashMap;

public enum Entities {
    CAMERA,
    PLAYER,
    RING_SEGMENT,
    ;

    private static final HashMap<Entities, Archetype> archetypes = new HashMap<>();

    public int create(World world) {
        if(!archetypes.containsKey(this)) {
            archetypes.put(this, createArchetype(world));
        }

        return world.create(archetypes.get(this));
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
            case RING_SEGMENT:
                return new ArchetypeBuilder()
                        .add(TransformComponent.class,
                                RenderComponent.class,
                                ObstacleComponent.class)
                        .build(world);
        }

        return null;
    }

    public static int createPlayer(World world) {
        int id = PLAYER.create(world);
        ComponentMapper.getFor(RenderComponent.class, world).get(id).sprite = Assets.player;
        ComponentMapper.getFor(PlayerComponent.class, world).get(id).color = GameColor.BLUE;
        return id;
    }

    public static int createCamera(World world, int targetId) {
        int id = CAMERA.create(world);
        ComponentMapper.getFor(CameraFollowComponent.class, world).get(id).target = targetId;
        return id;
    }

    public static int createRingSegment(World world, GameColor color, float x, float y,
                                        float innerRad, float outerRad, float sweep) {
        int id = RING_SEGMENT.create(world);

        RingSegment ring = new RingSegment();
        ring.color = color;
        ring.innerRadius = innerRad;
        ring.outerRadius = outerRad;
        ring.sweep = sweep;
        ComponentMapper.getFor(RenderComponent.class, world).get(id).sprite = ring;

        ObstacleComponent oc = ComponentMapper.getFor(ObstacleComponent.class, world).get(id);
        oc.obstacle = ring;
        oc.color = color;

        Vector2 position = new Vector2(x, y);
        ComponentMapper.getFor(TransformComponent.class, world).get(id).position.set(position);

        return id;
    }
}
