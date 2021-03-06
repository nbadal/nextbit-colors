package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.ColorsGame;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.graphics.Assets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;

public class BackgroundRenderSystem extends BaseSystem {
    private static final int COLOR_SKY = 0xff99d9d9;
    private static final Paint sPaint = new Paint();
    private static final RectF sTempRect = new RectF();
    private static final Path sTempPath = new Path();
    private static final int COLOR_GROUND = 0xff44b425;
    private static final int COLOR_SHADOW = 0x88000000;
    private static final int NUM_SKYLINES = 3;


    private static final int MIN_SKYLINE_BRIGHTNESS = 96;
    private static final int MAX_SKYLINE_BRIGHTNESS = 112;
    private static final int SKYLINE_OFFSET = 200;
    private static final int SKYLINE_START = 100;
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

        // Sky Color
        c.drawColor(COLOR_SKY); // Maybe fade this to black as we get very high?

        final float cameraYPx = (float) (Camera.y * Assets.metersToPx);

        // Skyline
        for(int i = 0; i < mSkylines.size(); i++) {
            Skyline skyline = mSkylines.get(i);
            sPaint.setColor(skyline.color);
            final float parallax = (i * MAX_SKYLINE_PARALLAX) / mSkylines.size();
            final float heightOffset = SKYLINE_START + (mSkylines.size() - i - 1) * SKYLINE_OFFSET;

            int x = 0;
            for(SkylinePart part : skyline) {
                sTempPath.reset();
                sTempPath.moveTo(x, 0);
                sTempPath.lineTo(x, part.height1 + heightOffset - cameraYPx * parallax);
                sTempPath.lineTo(x + part.width, part.height2 + heightOffset - cameraYPx *
                        parallax);
                sTempPath.lineTo(x + part.width, 0);
                c.drawPath(sTempPath, sPaint);


                final float antY = heightOffset + Math.min(part.height1, part.height2)
                        - cameraYPx * parallax;
                c.drawRect(x, antY, x+part.width, 0, sPaint);
                if(part.antennaPos != null) {
                    c.drawRect(x + part.antennaPos - 3, antY + 200,
                            x + part.antennaPos + 3, antY - 200, sPaint);
                }
                x += part.width;
            }
        }

        float cameraWidthPx = (float) (Camera.widthMeters * Assets.metersToPx);

        // Hill
        float hillHeightPx = (float) ((PlayerFloorSystem.FLOOR_POS + 0.5) * Assets.metersToPx);
        sPaint.setColor(COLOR_GROUND);
        sTempRect.set(-30, -hillHeightPx, cameraWidthPx + 30, hillHeightPx);
        c.drawOval(sTempRect, sPaint);

        // Shadow
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
        private static final int SLOPE_MAX = 120;

        public final int width;
        public final int height1;
        public final int height2;
        public final Integer antennaPos;

        public SkylinePart() {
            width = ColorsGame.random.nextInt(MAX_WIDTH - MIN_WIDTH) + MIN_WIDTH;
            height1 = ColorsGame.random.nextInt(MAX_HEIGHT);

            if (ColorsGame.random.nextInt(3) == 0) {
                int slopeAmt = ColorsGame.random.nextInt(SLOPE_MAX);
                if(ColorsGame.random.nextBoolean()) {
                    slopeAmt *= -1;
                }
                height2 = height1 + slopeAmt;
            } else {
                height2 = height1;
            }

            if(ColorsGame.random.nextInt(10) == 0) {
                antennaPos = ColorsGame.random.nextInt(width);
            } else {
                antennaPos = null;
            }
        }
    }

    private static class Skyline extends ArrayList<SkylinePart> {
        public int color;
    }
}
