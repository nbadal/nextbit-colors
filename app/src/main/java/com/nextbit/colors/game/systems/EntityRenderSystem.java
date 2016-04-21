package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.components.ColorComponent;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.UIComponent;
import com.nextbit.colors.game.graphics.Assets;
import com.nextbit.colors.game.graphics.TwoSideSprite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EntityRenderSystem extends BaseEntitySystem {
    public static final Paint DEBUG_PAINT = new Paint() {{
        setColor(Color.RED);
        setTextSize(100);
    }};

    private float zRotation = 1f;

    private ComponentMapper<RenderComponent> renderM;
    private ComponentMapper<PhysicsComponent> transformM;
    private ComponentMapper<UIComponent> uiM;
    private ComponentMapper<ColorComponent> colorM;

    private final Comparator<Integer> mLayerComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer lhs, Integer rhs) {
            return 0;
//            return Integer.compare(renderM.get(lhs).layer, renderM.get(rhs).layer);
        }
    };

    private ArrayList<Integer> renderQueue = new ArrayList<>();

    public EntityRenderSystem() {
        super(Aspect.all(RenderComponent.class).one(PhysicsComponent.class, UIComponent.class));
    }

    @Override
    protected void processSystem() {
        Canvas c = Camera.canvas;
        if(c == null) {
            return;
        }

        zRotation = (float) Math.cos(2 * Math.PI * (System.currentTimeMillis() % 2000) / 2000.);

        c.save();
        // Offset for camera, Y at bottom of screen
        c.translate(0, (float) Camera.heightMeters * Assets.metersToPx);
        c.translate(-(float)Camera.x * Assets.metersToPx , (float)Camera.y * Assets.metersToPx);

        Collections.sort(renderQueue, mLayerComparator);
        for(int entity : renderQueue) {
            RenderComponent render = renderM.get(entity);
            if(render == null || !render.enabled || !render.onScreen) {
                continue;
            }

            GameColor color = null;
            if(colorM.has(entity)) {
                color = colorM.get(entity).color;
            }

            final double worldX, worldY, rotation;
            if(transformM.has(entity)) {
                PhysicsComponent transform = transformM.get(entity);
                worldX = transform.body.getTransform().getTranslationX();
                worldY = transform.body.getTransform().getTranslationY();
                rotation = transform.body.getTransform().getRotation();
            } else {
                UIComponent ui = uiM.get(entity);
                worldX = ui.isWorldPosition ? ui.position.x : ui.position.x + Camera.x;
                worldY = ui.isWorldPosition ? ui.position.y : ui.position.y + Camera.y;
                rotation = 0;
            }

            c.save();
            c.translate((float) worldX * Assets.metersToPx, (float) -worldY * Assets.metersToPx);
            c.rotate((float) -Math.toDegrees(rotation));
            if(render.zRotate) {
                if(render.sprite instanceof TwoSideSprite) {
                    ((TwoSideSprite) render.sprite).setFront(zRotation > 0);
                    c.scale(Math.abs(zRotation), 1f);
                } else {
                    c.scale(zRotation, 1f);
                }
            }

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

        renderQueue.remove((Integer) entityId);
    }
}
