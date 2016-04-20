package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.obstacles.Obstacles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class LevelGeneratorSystem extends IteratingSystem {

    private static final double GENERATE_BEGIN = 8;
    private static final Comparator<? super ObstacleSet> OBSTACLE_COMPARATOR = new ObstacleSet.Comparator();

    private ArrayList<ObstacleSet> obstacles = new ArrayList<>();

    public LevelGeneratorSystem() {
        super(Aspect.all(PlayerComponent.class, PhysicsComponent.class));
    }

    @Override
    protected void process(int entityId) {
        final double generateStart = Math.max(Camera.y, GENERATE_BEGIN);
        final double generateEnd = Camera.y + Camera.heightMeters;

        // Remove invalid obstacles
        ArrayList<ObstacleSet> validObstacles = new ArrayList<>();
        for(ObstacleSet os : obstacles) {
            if(os.topY >= generateStart && os.bottomY <= generateEnd) {
                validObstacles.add(os);
            } else {
                removeObstacles(os);
            }
        }
        obstacles.clear();
        obstacles.addAll(validObstacles);

        // Find already-generated range
        final double generatedBottom, generatedTop;
        if(!obstacles.isEmpty()) {
            generatedBottom = obstacles.get(0).bottomY;
            generatedTop = obstacles.get(obstacles.size() - 1).topY;
        } else {
            generatedBottom = generateStart;
            generatedTop = generateStart;
        }

        // Generate below existing obstacles
        if(generateStart < generatedBottom) {
            generate(generateStart, generatedBottom);
        }

        // Generate above existing obstacles
        if(generatedTop < generateEnd) {
            generate(generatedTop, generateEnd);
        }

        // Re-sort
        Collections.sort(obstacles, OBSTACLE_COMPARATOR);
    }

    private void generate(double start, double end) {
        double y = start;
        while(y < end) {
            y += Obstacles.SPACING;

            if(y >= end) {
                break;
            }

            ObstacleSet set = new ObstacleSet();
            set.entities.addAll(Obstacles.BIG_CIRCLE.create(getWorld(), y, 1.0));
            set.bottomY = y;
            set.topY = y + Obstacles.BIG_CIRCLE.height();
            obstacles.add(set);

            y = set.topY;
        }
    }

    private void removeObstacles(ObstacleSet os) {
        for(int entityId : os.entities) {
            getWorld().delete(entityId);
        }
    }

    private static class ObstacleSet {
        public HashSet<Integer> entities = new HashSet<>();
        public double bottomY;
        public double topY;

        public static class Comparator implements java.util.Comparator<ObstacleSet> {
            @Override
            public int compare(ObstacleSet lhs, ObstacleSet rhs) {
                return Double.compare(lhs.bottomY, rhs.bottomY);
            }
        }
    }
}
