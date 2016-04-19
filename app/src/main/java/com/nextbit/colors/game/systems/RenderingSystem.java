package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.components.TransformComponent;
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
    private ComponentMapper<TransformComponent> transformM;

    private final Comparator<Integer> mLayerComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer lhs, Integer rhs) {
            return 0;
//            return Integer.compare(renderM.get(lhs).layer, renderM.get(rhs).layer);
        }
    };

    private ArrayList<Integer> renderQueue = new ArrayList<>();

    public RenderingSystem() {
        super(Aspect.all(TransformComponent.class, RenderComponent.class));
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
        c.translate(-Camera.x, Camera.y);

        Collections.sort(renderQueue, mLayerComparator);
        for(int entity : renderQueue) {
            RenderComponent render = renderM.get(entity);
            if(!render.enabled) {
                continue;
            }
            TransformComponent transform = transformM.get(entity);

            c.save();
            c.translate(transform.position.x, -transform.position.y);
            c.rotate(-transform.rotation);

            render.sprite.render(c);

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
