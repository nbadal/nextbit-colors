package com.nextbit.colors.game.components;

import com.badlogic.ashley.core.Component;
import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.Obstacle;

public class ObstacleComponent implements Component {
    public GameColor color;
    public Obstacle obstacle;
}
