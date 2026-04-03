package com.example.android_bt2.ui;

import android.content.Context;

public final class DrawableUtils {
    private DrawableUtils() {
    }

    public static int resolve(Context context, String drawableName, int fallbackResId) {
        if (drawableName == null || drawableName.trim().isEmpty()) {
            return fallbackResId;
        }
        int resId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        return resId != 0 ? resId : fallbackResId;
    }
}

