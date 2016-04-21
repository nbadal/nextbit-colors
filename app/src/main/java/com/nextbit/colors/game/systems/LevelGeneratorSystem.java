package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.obstacles.ObstacleType;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;

public class LevelGeneratorSystem extends IteratingSystem {

    private static final double GENERATE_BEGIN = 16;

    private ArrayList<ObstacleSet> obstacles = new ArrayList<>();

    public LevelGeneratorSystem() {
        super(Aspect.all(PlayerComponent.class, PhysicsComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final double generateStartY = Math.max(Camera.y, GENERATE_BEGIN);
        final double generateEndY = Camera.y + Camera.heightMeters;

        // Find already-generated range
        ObstacleSet generatedFirst = null;
        ObstacleSet generatedLast = null;
        if(!obstacles.isEmpty()) {
            generatedFirst = obstacles.get(0);
            generatedLast = obstacles.get(obstacles.size() - 1);
        }

        // Generate above existing obstacles
        if(generatedLast == null || generatedLast.topY < generateEndY) {
            generateUp(generatedLast, generateEndY);
        }

        // Generate below existing obstacles
        if(generatedFirst != null && generateStartY < generatedFirst.bottomY) {
            generateDown(generatedFirst, generateStartY);
        }

        // Remove invalid obstacles
        ArrayList<ObstacleSet> validObstacles = new ArrayList<>();
        for(ObstacleSet os : obstacles) {
            if(os.topY >= generateStartY && os.bottomY <= generateEndY) {
                validObstacles.add(os);
            } else {
                removeObstacles(os);
            }
        }
        obstacles.clear();
        obstacles.addAll(validObstacles);
    }

    private ObstacleType getTypeForPosition(int position) {
        // TODO cache calculated types in a lookup
        return ObstacleType.values()[position % ObstacleType.values().length];
    }

    /** Generate up to upToY, starting with existing set or from beginning of generation */
    private void generateUp(@Nullable ObstacleSet above, double upToY) {
        double y = GENERATE_BEGIN;
        int position = 0;

        if(above != null) {
            position = above.position + 1;
            y = above.topY + ObstacleType.SPACING;
        }

        while(y < upToY) {
            ObstacleType type = getTypeForPosition(position);

            ObstacleSet set = new ObstacleSet();
            set.position = position;
            set.topY = y + type.height();
            set.bottomY = y;
            set.entities.addAll(type.create(getWorld(), set.bottomY, 1.0));
            obstacles.add(set);

            position++;
            y = set.topY;

            if(y >= upToY) {
                break;
            }
            y += ObstacleType.SPACING;
        }
    }

    /** Generate down from existing bottom set */
    private void generateDown(@NonNull ObstacleSet below, double downToY) {
        double y = below.bottomY - ObstacleType.SPACING;
        int position = below.position - 1;

        while(position >= 0 && y > downToY) {
            ObstacleType type = getTypeForPosition(position);

            ObstacleSet set = new ObstacleSet();
            set.position = position;
            set.topY = y;
            set.bottomY = y - type.height();
            set.entities.addAll(type.create(getWorld(), set.bottomY, 1.0));
            obstacles.add(0, set);

            position--;
            y = set.bottomY;

            if(y <= downToY) {
                break;
            }
            y -= ObstacleType.SPACING;
        }
    }

    private void removeObstacles(ObstacleSet os) {
        for(int entityId : os.entities) {
            getWorld().delete(entityId);
        }
    }

    private static class ObstacleSet {
        public HashSet<Integer> entities = new HashSet<>();
        public Integer position;
        public double bottomY;
        public double topY;
    }
}
