package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.ColorsGame;
import com.nextbit.colors.game.components.PhoneComponent;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.graphics.Assets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.AbstractSequentialList;
import java.util.ArrayList;

public class BackgroundRenderSystem extends BaseSystem {
    private static final int COLOR_SKY = 0xff99d9d9;
    private static final Paint sPaint = new Paint();
    private static final RectF sTempRect = new RectF();
    private static final int COLOR_GROUND = 0xff44b425;
    private static final int COLOR_SHADOW = 0x88000000;
    private static final int NUM_SKYLINES = 3;


    private static final int MIN_SKYLINE_BRIGHTNESS = 96;
    private static final int MAX_SKYLINE_BRIGHTNESS = 112;
    private static final int SKYLINE_OFFSET = 200;
    private static final int SKYLINE_START = 300;
    private static final float MAX_SKYLINE_PARALLAX = 0.5f;

    private ComponentMapper<PhysicsComponent> physM;
    private ArrayList<Skyline> mSkylines = new ArrayList<>();

    @Override
    protected void processSystem() {
        final Canvas c = Camera.canvas;

        if(mSkylines.isEmpty()) {
            generateSkylines();
        }

        c.save();

        c.translate(0, (float) Camera.heightMeters * Assets.metersToPx);
        c.translate(0, (float) Camera.y * Assets.metersToPx);
        c.scale(1f, -1f);

        c.drawColor(COLOR_SKY); // Maybe fade this to black as we get very high?

        for(int i = 0; i < mSkylines.size(); i++) {
            Skyline skyline = mSkylines.get(i);
            sPaint.setColor(skyline.color);
            final float parallax = (i * MAX_SKYLINE_PARALLAX) / mSkylines.size();
            final float heightOffset = SKYLINE_START + (mSkylines.size() - i - 1) * SKYLINE_OFFSET;

            int x = 0;
            for(SkylinePart part : skyline) {
                c.drawRect(x, (float) (heightOffset + part.height
                        - Camera.y * Assets.metersToPx * parallax), x+part.width, 0, sPaint);
                x += part.width;
            }
        }

        float cameraWidthPx = (float) (Camera.widthMeters * Assets.metersToPx);

        float hillHeightPx = (float) ((PlayerFloorSystem.FLOOR_POS + 0.5) * Assets.metersToPx);
        sPaint.setColor(COLOR_GROUND);
        sTempRect.set(-30, -hillHeightPx, cameraWidthPx + 30, hillHeightPx);
        c.drawOval(sTempRect, sPaint);

        IntBag players = getWorld().getAspectSubscriptionManager().get(
                Aspect.all(PlayerComponent.class, PhysicsComponent.class)).getEntities();
        if(!players.isEmpty()) {
            double playerYMeters = physM.get(players.get(0)).body.getTransform().getTranslationY();
            double floorMeters = PlayerFloorSystem.FLOOR_POS;

            float distToFloorPx = (float) ((playerYMeters - floorMeters) * Assets.metersToPx);
            distToFloorPx /= 8f;

            float ovalWidthPx = Assets.metersToPx;
            float ovalHeightPx = (Assets.metersToPx / 2);

            float heightFactor = 1f - distToFloorPx / hillHeightPx;

            c.save();
            c.translate(cameraWidthPx / 2, (float) (floorMeters * Assets.metersToPx) -
                    distToFloorPx);
            c.scale(3f - 2f * heightFactor, 1f);

            sPaint.setColor(multAlpha(COLOR_SHADOW, heightFactor));
            sTempRect.set(-ovalWidthPx / 2, - ovalHeightPx / 2, ovalWidthPx / 2, ovalHeightPx / 2);
            c.drawOval(sTempRect, sPaint);

            c.restore();
        }

        c.restore();
    }

    public void generateSkylines() {
        mSkylines.clear();
        int cameraWidth = (int) (Camera.widthMeters * Assets.metersToPx);
        for(int i = 0; i < NUM_SKYLINES; i++) {
            Skyline skyline = new Skyline();
            int x = 0;
            while(x < cameraWidth) {
                SkylinePart part = new SkylinePart();
                skyline.add(part);
                x += part.width;
            }
            skyline.color = getGray(i / (float) NUM_SKYLINES);
            mSkylines.add(skyline);
        }
    }

    private int multAlpha(int color, float factor) {
        return Color.argb((int) (Color.alpha(color) * factor),
                Color.red(color), Color.green(color), Color.blue(color));
    }

    private int getGray(float blend) {
        final int val = (int) (MIN_SKYLINE_BRIGHTNESS + MAX_SKYLINE_BRIGHTNESS * blend);
        return Color.argb(255, val, val, val);
    }

    private static class SkylinePart {
        private static final int MIN_WIDTH = 75 ;
        private static final int MAX_WIDTH = 300;
        private static final int MAX_HEIGHT = 1000;

        public final int width;
        public final int height;

        public SkylinePart() {
            width = ColorsGame.random.nextInt(MAX_WIDTH - MIN_WIDTH) + MIN_WIDTH;
            height = ColorsGame.random.nextInt(MAX_HEIGHT);
        }
    }

    private static class Skyline extends ArrayList<SkylinePart> {
        public int color;
    }
}
