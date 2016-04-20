package com.nextbit.colors.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.nextbit.colors.game.components.PlayerComponent;
import com.nextbit.colors.game.components.RenderComponent;
import com.nextbit.colors.game.components.ScoreComponent;
import com.nextbit.colors.game.graphics.Sprite;
import com.nextbit.colors.game.graphics.TextSprite;

import java.util.Locale;

public class PlayerScoreSystem extends IteratingSystem {

    ComponentMapper<PlayerComponent> playerM;
    ComponentMapper<RenderComponent> renderM;

    public PlayerScoreSystem() {
        super(Aspect.all(PlayerComponent.class));
    }

    @Override
    protected void process(int entityId) {
        int score = playerM.get(entityId).score;

        IntBag scoreRenders = getWorld().getAspectSubscriptionManager().get(
                Aspect.all(ScoreComponent.class, RenderComponent.class)).getEntities();
        for(int i = 0; i < scoreRenders.size(); i++) {
            Sprite s = renderM.get(scoreRenders.get(i)).sprite;
            if(s instanceof TextSprite) {
                ((TextSprite) s).text = String.format(Locale.getDefault(), "%02d", score);
            }
        }
    }
}
