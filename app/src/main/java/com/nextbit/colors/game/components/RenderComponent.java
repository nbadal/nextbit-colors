package com.nextbit.colors.game.components;

import com.artemis.Component;
import com.nextbit.colors.game.graphics.Sprite;

public class RenderComponent extends Component {
    public boolean enabled = true;
    public boolean onScreen = true;
    public Sprite sprite;
    public boolean zRotate;
}
