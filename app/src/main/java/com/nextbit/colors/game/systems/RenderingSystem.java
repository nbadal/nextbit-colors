package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.components.ColorComponent;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.RenderComponent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RenderingSystem extends BaseEntitySystem {
    public static final Paint DEBUG_PAINT = new Paint() {{
        setColor(Color.RED);
        setTextSize(100);
    }};

    private ComponentMapper<RenderComponent> renderM;
    private ComponentMapper<PhysicsComponent> transformM;
    private ComponentMapper<ColorComponent> colorM;

    private final Comparator<Integer> mLayerComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer lhs, Integer rhs) {
            return 0;
//            return Integer.compare(renderM.get(lhs).layer, renderM.get(rhs).layer);
        }
    };

    private ArrayList<Integer> renderQueue = new ArrayList<>();

    public RenderingSystem() {
        super(Aspect.all(PhysicsComponent.class, RenderComponent.class));
    }

    @Override
    protected void processSystem() {
        Canvas c = Camera.canvas;
        if(c == null) {
            return;
        }
        c.save();
        // Offset for camera, Y at bottom of screen
        c.translate(0, Camera.height);
        c.translate(-(float)Camera.x, (float)Camera.y);

        Collections.sort(renderQueue, mLayerComparator);
        for(int entity : renderQueue) {
            RenderComponent render = renderM.get(entity);
            if(!render.enabled) {
                continue;
            }
            PhysicsComponent transform = transformM.get(entity);
            GameColor color = null;
            if(colorM.has(entity)) {
                color = colorM.get(entity).color;
            }

            c.save();
            c.translate((float) transform.body.getTransform().getTranslationX(),
                    (float) -transform.body.getTransform().getTranslationY());
            c.rotate((float) -Math.toDegrees(transform.body.getTransform().getRotation()));

            render.sprite.render(c, color);

            c.restore();

        }
        c.restore();
    }

    @Override
    protected void inserted(int entityId) {
        super.inserted(entityId);

        renderQueue.add(entityId);
    }

    @Override
    protected void removed(int entityId) {
        super.removed(entityId);

        renderQueue.remove(entityId);
    }
}
