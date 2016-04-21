package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.AspectSubscriptionManager;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.nextbit.colors.game.Camera;
import com.nextbit.colors.game.Entities;
import com.nextbit.colors.game.GameColor;
import com.nextbit.colors.game.components.ColorComponent;
import com.nextbit.colors.game.components.GameOverComponent;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.PhysicsComponent;
import com.nextbit.colors.game.components.SwitchComponent;

public class PlayerRespawnSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<PhysicsComponent> transformM;
    private ComponentMapper<RenderComponent> renderM;
    private ComponentMapper<ColorComponent> colorM;

    public PlayerRespawnSystem() {
        super(Aspect.all(PlayerComponent.class, PhysicsComponent.class, RenderComponent.class,
                ColorComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent pc = playerM.get(entityId);
        if(!pc.isAlive || pc.respawnHandled) {
            return;
        }
        pc.respawnHandled = true;

        PhysicsComponent transform = transformM.get(entityId);
        RenderComponent render = renderM.get(entityId);
        ColorComponent color = colorM.get(entityId);

        // Reset Positions
        transform.body.getTransform().setTranslation(0, PlayerFloorSystem.FLOOR_POS);
        Camera.y = 0;
        transform.body.setLinearVelocity(0, 0);

        // New Color
        color.color = null;


        AspectSubscriptionManager manager = getWorld().getAspectSubscriptionManager();

        // Respawn all switches
        IntBag switches = manager.get(Aspect.all(SwitchComponent.class)).getEntities();
        for(int i = 0; i < switches.size(); i++) {
            int colorSwitch = switches.get(i);
            renderM.get(colorSwitch).enabled = true;
        }

        // Respawn all stars

        // Remove Death Particles

        // Hide Game Over
        IntBag gameOvers = getWorld().getAspectSubscriptionManager().get(
                Aspect.all(GameOverComponent.class)).getEntities();
        for(int i = 0; i < gameOvers.size(); i++) {
            getWorld().delete(gameOvers.get(i));
        }

        // Reset Score
        pc.score = 0;

        // Show Player
        render.enabled = true;
    }
}
