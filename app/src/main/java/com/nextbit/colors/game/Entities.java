package com.nextbit.colors.game;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.nextbit.colors.game.components.CameraFollowComponent;
import com.nextbit.colors.game.components.ColorComponent;
import com.nextbit.colors.game.components.ObstacleComponent;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.SwitchComponent;
import com.nextbit.colors.game.obstacles.ObstacleGeometry;
import com.nextbit.colors.game.obstacles.RingSegment;
import com.nextbit.colors.game.obstacles.RingSegmentSprite;
import com.nextbit.colors.game.systems.PhysicsSystem;
import com.nextbit.colors.game.util.EntityBody;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Vector2;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum Entities {
    CAMERA,
    PLAYER,
    RING_SEGMENT,
    COLOR_SWITCH,
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
                        .add(PhysicsComponent.class,
                                RenderComponent.class,
                                ColorComponent.class,
                                PlayerComponent.class)
                        .build(world);

            case CAMERA:
                return new ArchetypeBuilder()
                        .add(CameraFollowComponent.class)
                        .build(world);
            case RING_SEGMENT:
                return new ArchetypeBuilder()
                        .add(PhysicsComponent.class,
                                RenderComponent.class,
                                ColorComponent.class,
                                ObstacleComponent.class)
                        .build(world);
            case COLOR_SWITCH:
                return new ArchetypeBuilder()
                        .add(PhysicsComponent.class,
                                RenderComponent.class,
                                SwitchComponent.class)
                        .build(world);
        }

        return null;
    }

    public static int createPlayer(World world) {
        int id = PLAYER.create(world);
        ComponentMapper.getFor(RenderComponent.class, world).get(id).sprite = Assets.sheep;

        ComponentMapper.getFor(ColorComponent.class, world).get(id).color = GameColor.random();

        Body playerBody = new EntityBody(id);
//        playerBody.setBullet(true);
        playerBody.setMass(new Mass(new Vector2(), 1d, 0d));
        BodyFixture bf = playerBody.addFixture(Geometry.createCircle(PlayerComponent.SIZE));
        bf.setSensor(true);
        bf.setFilter(new CategoryFilter(PhysicsSystem.CAT_PLAYER,
                PhysicsSystem.CAT_OBSTACLE | PhysicsSystem.CAT_SWITCH));
        world.getSystem(PhysicsSystem.class).WORLD.addBody(playerBody);
        ComponentMapper.getFor(PhysicsComponent.class, world).get(id).body = playerBody;

        return id;
    }

    public static int createCamera(World world, int targetId) {
        int id = CAMERA.create(world);
        ComponentMapper.getFor(CameraFollowComponent.class, world).get(id).target = targetId;
        return id;
    }

    public static int createRingSegment(World world, GameColor color, double speed,
                                        double x, double y, double innerRad, double outerRad,
                                        double sweep, double startAngle) {
        int id = RING_SEGMENT.create(world);

        RingSegment ring = new RingSegment();
        ring.innerRadius = innerRad;
        ring.outerRadius = outerRad;
        ring.sweep = sweep;

        ComponentMapper.getFor(ColorComponent.class, world).get(id).color = color;

        RingSegmentSprite ringSprite = new RingSegmentSprite();
        ringSprite.info = ring;
        ComponentMapper.getFor(RenderComponent.class, world).get(id).sprite = ringSprite;

        PhysicsComponent phys = ComponentMapper.getFor(PhysicsComponent.class, world).get(id);
        phys.body = ObstacleGeometry.createRingSegment(id, ring);
        phys.body.rotateAboutCenter(startAngle);
        phys.body.translate(x, y);
        phys.body.setAngularVelocity(speed);
        world.getSystem(PhysicsSystem.class).WORLD.addBody(phys.body);

        return id;
    }

    public static Set<Integer> createRing(World world, double speed, double x, double y,
                                          double innerRad, double outerRad) {
        HashSet<Integer> ids = new HashSet<>();
        int color = ColorsGame.random.nextInt(4);
        for(int i = 0; i < 4; i++) {
            final int id = Entities.createRingSegment(world, GameColor.values[color], speed, x, y,
                    innerRad, outerRad, (float) (Math.PI / 2), (float) (Math.PI * i / 2) );
            ids.add(id);
            color = (color + 1) % 4;
        }
        return ids;
    }

    public static int createSwitch(World world, double y) {
        int id = COLOR_SWITCH.create(world);
        PhysicsComponent pc = ComponentMapper.getFor(PhysicsComponent.class, world).get(id);
        pc.body = new EntityBody(id);
        pc.body.translate(0, y);
        BodyFixture bf = pc.body.addFixture(Geometry.createCircle(SwitchComponent.RADIUS));
        bf.setSensor(true);
        bf.setFilter(new CategoryFilter(PhysicsSystem.CAT_SWITCH, PhysicsSystem.CAT_PLAYER));
        world.getSystem(PhysicsSystem.class).WORLD.addBody(pc.body);
        ComponentMapper.getFor(RenderComponent.class, world).get(id).sprite = new SwitchSprite();
        return id;
    }
}
