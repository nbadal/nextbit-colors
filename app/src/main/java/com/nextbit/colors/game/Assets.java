package com.nextbit.colors.game;

import com.nextbit.colors.R;

import android.content.Context;
import android.content.res.Resources;

public class Assets {
    public static Sprite player;

    public static void load(Context context) {
        final Resources res = context.getResources();
        player = new DrawableSprite(res.getDrawable(R.drawable.player));
    }
}
