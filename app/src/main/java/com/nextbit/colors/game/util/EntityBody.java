package com.nextbit.colors.game.util;

import org.dyn4j.dynamics.Body;

public class EntityBody extends Body {
    public final int entityId;

    public EntityBody(int entityId) {
        this.entityId = entityId;
        setAutoSleepingEnabled(false);
    }

    public EntityBody(int entityId, int fixtureCount) {
        super(fixtureCount);
        this.entityId = entityId;
        setAutoSleepingEnabled(false);
    }

    @Override
    public boolean isStatic() {
        return false;
    }
}
