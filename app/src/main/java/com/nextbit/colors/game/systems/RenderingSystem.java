package com.nextbit.colors.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.components.TransformComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.util.LayerComparator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RenderingSystem extends IteratingSystem {
    public static final Paint DEBUG_PAINT = new Paint() {{
        setColor(Color.RED);
        setTextSize(100);
    }};

    private static final LayerComparator LAYER_COMPARATOR = new LayerComparator();

    private final ComponentMapper<RenderComponent> renderM = ComponentMapper.getFor(RenderComponent.class);
    private final ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);

    private Array<Entity> renderQueue = new Array<>();

    public RenderingSystem() {
        super(Family.all(TransformComponent.class, RenderComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Canvas c = Camera.canvas;
        if(c == null) {
            return;
        }
        c.save();
        // Offset for camera, Y at bottom of screen
        c.translate(0, Camera.height);
        c.translate(-Camera.x, Camera.y);


        renderQueue.sort(LAYER_COMPARATOR);
        for(Entity entity : renderQueue) {
            RenderComponent render = renderM.get(entity);
            TransformComponent transform = transformM.get(entity);

            c.save();
            c.translate(transform.position.x, -transform.position.y);

            render.sprite.render(c);

            c.restore();

        }
        c.restore();

        renderQueue.clear();
    }
}
