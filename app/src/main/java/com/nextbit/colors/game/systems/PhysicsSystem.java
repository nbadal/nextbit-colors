package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.components.ColorComponent;
import com.nextbit.colors.game.components.ObstacleComponent;
import com.nextbit.colors.game.components.PhoneComponent;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.ScoreComponent;
import com.nextbit.colors.game.components.SwitchComponent;
import com.nextbit.colors.game.util.EntityBody;
import com.nextbit.colors.game.util.GravityMath;

import org.dyn4j.collision.broadphase.Sap;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.ContinuousDetectionMode;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.geometry.Vector2;

import android.util.Pair;

import java.util.HashSet;

public class PhysicsSystem extends BaseEntitySystem {
    public static final long CAT_PLAYER = 1 << 0;
    public static final long CAT_OBSTACLE = 1 << 1;
    public static final long CAT_SWITCH = 1 << 2;
    public static final long CAT_PHONE = 1 << 3;

    private final World mWorld = new World();
    public final HashSet<Pair<Integer, Integer>> COLLISIONS = new HashSet<>();

    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<ObstacleComponent> obstacleM;
    private ComponentMapper<SwitchComponent> switchM;
    private ComponentMapper<PhoneComponent> phoneM;
    private ComponentMapper<RenderComponent> renderM;
    private ComponentMapper<ColorComponent> colorM;
    private ComponentMapper<PhysicsComponent> physicsM;

    public PhysicsSystem() {
        super(Aspect.all(PhysicsComponent.class));

        mWorld.setGravity(new Vector2(0, GravityMath.GRAVITY));
        Settings settings = new Settings();
        settings.setContinuousDetectionMode(ContinuousDetectionMode.BULLETS_ONLY);
        mWorld.setSettings(settings);
        mWorld.setBroadphaseDetector(new Sap<Body, BodyFixture>());
        mWorld.addListener(new ContactAdapter() {
            @Override
            public void sensed(ContactPoint point) {
                COLLISIONS.add(new Pair<>(
                        ((EntityBody)point.getBody1()).entityId,
                        ((EntityBody)point.getBody2()).entityId
                ));
            }
        });
    }

    // bodies must be added outside of this system because we usually set the body after creation
    public void addBody(Body body) {
        mWorld.addBody(body);
    }

    @Override
    protected void removed(int entityId) {
        super.removed(entityId);
        mWorld.removeBody(physicsM.get(entityId).body);
    }

    @Override
    protected void processSystem() {
        mWorld.updatev(getWorld().getDelta());
        for(Pair<Integer, Integer> collision : COLLISIONS) {
            final int player, collided;
            if(playerM.has(collision.first)) {
                player = collision.first;
                collided = collision.second;
            } else {
                player = collision.second;
                collided = collision.first;
            }

            PlayerComponent pc = playerM.get(player);
            ColorComponent playerCc = colorM.get(player);

            // Obstacle Collisions
            if(obstacleM.has(collided)) {
                ObstacleComponent oc = obstacleM.get(collided);
                ColorComponent obstacleCC = colorM.get(collided);

                if(playerCc.color != obstacleCC.color) {
                    pc.kill();
                }
            }

            // Color Switches
            if(switchM.has(collided)) {
                RenderComponent rc = renderM.get(collided);
                SwitchComponent sw = switchM.get(collided);
                if(rc.enabled) {
                    rc.enabled = false;

                    playerCc.color = sw.color;
                }
            }

            // Phones
            if(phoneM.has(collided)) {
                RenderComponent rc = renderM.get(collided);
                if(rc.enabled) {
                    rc.enabled = false;

                    pc.score++;
                }
            }
        }
        COLLISIONS.clear();
    }
}
