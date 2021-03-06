package com.nextbit.colors.game;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.nextbit.colors.game.components.CameraFollowComponent;
import com.nextbit.colors.game.components.ColorComponent;
import com.nextbit.colors.game.components.GameOverComponent;
import com.nextbit.colors.game.components.ObstacleComponent;
import com.nextbit.colors.game.components.PhoneComponent;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.ScoreComponent;
import com.nextbit.colors.game.components.SwitchComponent;
import com.nextbit.colors.game.components.UIComponent;
import com.nextbit.colors.game.graphics.Assets;
import com.nextbit.colors.game.graphics.PinArmSprite;
import com.nextbit.colors.game.graphics.RingSegmentSprite;
import com.nextbit.colors.game.graphics.SwitchSprite;
import com.nextbit.colors.game.graphics.TextSprite;
import com.nextbit.colors.game.obstacles.ObstacleGeometry;
import com.nextbit.colors.game.obstacles.PinArm;
import com.nextbit.colors.game.obstacles.RingSegment;
import com.nextbit.colors.game.systems.PhysicsSystem;
import com.nextbit.colors.game.systems.PlayerFloorSystem;
import com.nextbit.colors.game.util.EntityBody;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Vector2;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum Entities {
    CAMERA,
    PLAYER,
    SCORE,
    GAME_OVER,
    RING_SEGMENT,
    PIN_ARM,
    COLOR_SWITCH,
    PHONE,
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
            case SCORE:
                return new ArchetypeBuilder()
                        .add(ScoreComponent.class,
                                UIComponent.class,
                                RenderComponent.class)
                        .build(world);
            case GAME_OVER:
                return new ArchetypeBuilder()
                        .add(GameOverComponent.class,
                                UIComponent.class,
                                RenderComponent.class)
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
            case PHONE:
                return new ArchetypeBuilder()
                        .add(PhysicsComponent.class,
                                RenderComponent.class,
                                PhoneComponent.class)
                        .build(world);
        }

        return null;
    }

    public static int createPlayer(World world) {
        int id = PLAYER.create(world);
        ComponentMapper.getFor(RenderComponent.class, world).get(id).sprite = Assets.sheep;

        // Start white
        ComponentMapper.getFor(ColorComponent.class, world).get(id).color = null;

        Body playerBody = new EntityBody(id);
        playerBody.setMass(new Mass(new Vector2(), 1d, 0d));
        BodyFixture bf = playerBody.addFixture(Geometry.createCircle(PlayerComponent.SIZE * 0.5));
        bf.setSensor(true);
        bf.setFilter(new CategoryFilter(PhysicsSystem.CAT_PLAYER,
                PhysicsSystem.CAT_OBSTACLE | PhysicsSystem.CAT_SWITCH | PhysicsSystem.CAT_PHONE));
        world.getSystem(PhysicsSystem.class).addBody(playerBody);
        ComponentMapper.getFor(PhysicsComponent.class, world).get(id).body = playerBody;

        return id;
    }

    public static int createCamera(World world, int targetId) {
        int id = CAMERA.create(world);
        ComponentMapper.getFor(CameraFollowComponent.class, world).get(id).target = targetId;
        return id;
    }
    
    @SuppressLint("DefaultLocale")
    public static int createGameOver(World world) {
        int id = GAME_OVER.create(world);
        UIComponent ui = ComponentMapper.getFor(UIComponent.class, world).get(id);
        ui.isWorldPosition = false;
        ui.position.x = Camera.widthMeters / 2;
        ui.position.y = Camera.heightMeters / 2;

        final String message = String.format("GAME OVER! HIGHSCORE: %d", ColorsGame.highScore);
        ComponentMapper.getFor(RenderComponent.class, world).get(id).sprite = new TextSprite
                (message, true);
        return id;
    }

    public static int createScore(World world) {
        int id = SCORE.create(world);
        UIComponent ui = ComponentMapper.getFor(UIComponent.class, world).get(id);
        ui.isWorldPosition = false;
        ui.position.x = 1;
        ui.position.y = PlayerFloorSystem.FLOOR_POS / 2;

        ComponentMapper.getFor(RenderComponent.class, world).get(id).sprite = new TextSprite("00");
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
        world.getSystem(PhysicsSystem.class).addBody(phys.body);

        return id;
    }

    public static Set<Integer> createRing(World world, double speed,
                                          double x, double y, double innerRad, double outerRad) {
        return createRing(world, GameColor.random(), speed, x, y, innerRad, outerRad);
    }

    public static Set<Integer> createRing(World world, GameColor startColor, double speed,
                                          double x, double y, double innerRad, double outerRad) {
        HashSet<Integer> ids = new HashSet<>();
        int speedSig = (int) Math.signum(speed);
        for(int i = 0; i < 4; i++) {
            final int id = Entities.createRingSegment(world, startColor, speed, x, y,
                    innerRad, outerRad, (float) (Math.PI / 2), (float) (Math.PI * (0.5*i - 0.75)));
            ids.add(id);
            startColor = startColor.offset(speedSig);
        }
        return ids;
    }

    public static int createSwitch(World world, double y) {
        return createSwitch(world, y, GameColor.random());
    }

    public static int createSwitch(World world, double y, GameColor color) {
        int id = COLOR_SWITCH.create(world);

        PhysicsComponent pc = ComponentMapper.getFor(PhysicsComponent.class, world).get(id);
        pc.body = new EntityBody(id);
        pc.body.translate(0, y);

        BodyFixture bf = pc.body.addFixture(Geometry.createCircle(SwitchComponent.RADIUS));
        bf.setSensor(true);
        bf.setFilter(new CategoryFilter(PhysicsSystem.CAT_SWITCH, PhysicsSystem.CAT_PLAYER));

        world.getSystem(PhysicsSystem.class).addBody(pc.body);

        RenderComponent render = ComponentMapper.getFor(RenderComponent.class, world).get(id);
        render.zRotate = true;
        render.sprite = new SwitchSprite();
        return id;
    }

    public static int createPhone(World world, double y) {
        int id = PHONE.create(world);

        PhysicsComponent pc = ComponentMapper.getFor(PhysicsComponent.class, world).get(id);
        pc.body = new EntityBody(id);
        pc.body.translate(0, y);
        BodyFixture bf = pc.body.addFixture(Geometry.createCircle(ScoreComponent.RADIUS));
        bf.setSensor(true);
        bf.setFilter(new CategoryFilter(PhysicsSystem.CAT_PHONE, PhysicsSystem.CAT_PLAYER));
        world.getSystem(PhysicsSystem.class).addBody(pc.body);

        RenderComponent render = ComponentMapper.getFor(RenderComponent.class, world).get(id);
        render.zRotate = true;
        render.sprite = Assets.phone;
        return id;
    }

    public static Set<Integer> createPinwheel(World world, double speed, GameColor startColor,
                                          double x, double y, double thickness, double pinRadius) {
        HashSet<Integer> ids = new HashSet<>();

        int speedSig = (int) Math.signum(speed);

        for(int i = 0; i < 4; i++) {
            ids.add(createPinArm(world, speed, startColor, i, x, y, thickness, pinRadius));
            startColor = startColor.offset(speedSig);
        }

        return ids;
    }

    private static int createPinArm(World world, double speed, GameColor color, int position,
                                    double x, double y, double thickness, double length) {
        int id = RING_SEGMENT.create(world);

        PinArm arm = new PinArm();
        arm.length = length;
        arm.thickness = thickness;

        ComponentMapper.getFor(ColorComponent.class, world).get(id).color = color;

        PinArmSprite armSprite = new PinArmSprite();
        armSprite.info = arm;
        ComponentMapper.getFor(RenderComponent.class, world).get(id).sprite = armSprite;

        PhysicsComponent phys = ComponentMapper.getFor(PhysicsComponent.class, world).get(id);
        phys.body = ObstacleGeometry.createPinArm(id, arm);
        phys.body.rotate(position * Math.PI / 2);
        phys.body.translate(x, y);
        phys.body.setAngularVelocity(speed);
        world.getSystem(PhysicsSystem.class).addBody(phys.body);

        return id;
    }
}
