package com.nextbit.colors.game.components;


import com.artemis.Component;
import com.nextbit.colors.game.util.Vector2;

public class MovementComponent extends Component {
    public final Vector2 accel = new Vector2();
    public final Vector2 velocity = new Vector2();
    public float rotationSpeed;
}
