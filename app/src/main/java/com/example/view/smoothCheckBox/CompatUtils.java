package com.example.view.smoothCheckBox;

import android.content.Context;

/**
 * github开源的checkbox项目
 */
public class CompatUtils {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
