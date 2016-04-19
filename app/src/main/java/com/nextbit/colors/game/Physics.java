package com.nextbit.colors.game;

import com.nextbit.colors.game.util.EntityBody;
import com.nextbit.colors.game.util.GravityMath;

import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.geometry.Vector2;

import android.util.Pair;

import java.util.HashSet;

public class Physics {
    public static final World WORLD = new World();
    public static final HashSet<Pair<Integer, Integer>> COLLISIONS = new HashSet<>();

    static {
        WORLD.setGravity(new Vector2(0, GravityMath.GRAVITY));
        Settings settings = new Settings();
        settings.setMaximumTranslation(50);
        WORLD.setSettings(settings);
        WORLD.addListener(new ContactAdapter() {
            @Override
            public void sensed(ContactPoint point) {
                COLLISIONS.add(new Pair<>(
                        ((EntityBody)point.getBody1()).entityId,
                        ((EntityBody)point.getBody2()).entityId
                ));
            }
        });
    }
}
