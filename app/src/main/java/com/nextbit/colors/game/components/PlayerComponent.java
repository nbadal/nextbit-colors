package com.nextbit.colors.game.components;

import com.badlogic.ashley.core.Component;
import com.nextbit.colors.game.GameColor;

public class PlayerComponent implements Component {
    public static final float COLLISION_RADIUS = 45;

    public GameColor color;
}
