package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.ColorsGame;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.obstacles.ObstacleType;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;

public class LevelGeneratorSystem extends IteratingSystem {

    private static final double GENERATE_BEGIN = 16;

    private ArrayList<ObstacleType> obstacleOrder = new ArrayList<>();

    private ArrayList<ObstacleSet> obstacles = new ArrayList<>();

    public LevelGeneratorSystem() {
        super(Aspect.all(PlayerComponent.class, PhysicsComponent.class));

        // Consistent beginning of generation.
        obstacleOrder.add(ObstacleType.CIRCLE_PHONE);
        obstacleOrder.add(ObstacleType.SPACE_SWITCH);
        obstacleOrder.add(ObstacleType.CIRCLE_EMPTY);
        obstacleOrder.add(ObstacleType.SPACE);
        obstacleOrder.add(ObstacleType.CIRCLE_2X_PHONE);
        obstacleOrder.add(ObstacleType.SPACE);
        obstacleOrder.add(ObstacleType.TOUCHING_CIRCLES);
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
        for(int i = obstacleOrder.size(); i <= position; i++) {
            if(i % 2 == 0) {
                obstacleOrder.add(randomFrom(ObstacleType.OBSTACLES));
            } else {
                obstacleOrder.add(randomFrom(ObstacleType.SPACES));
            }
        }
        return obstacleOrder.get(position);
    }

    private ObstacleType randomFrom(ObstacleType[] list) {
        return list[ColorsGame.random.nextInt(list.length)];
    }

    /** Generate up to upToY, starting with existing set or from beginning of generation */
    private void generateUp(@Nullable ObstacleSet above, double upToY) {
        double y = GENERATE_BEGIN;
        int position = 0;

        if(above != null) {
            position = above.position + 1;
            y = above.topY;
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
        }
    }

    /** Generate down from existing bottom set */
    private void generateDown(@NonNull ObstacleSet below, double downToY) {
        double y = below.bottomY;
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
