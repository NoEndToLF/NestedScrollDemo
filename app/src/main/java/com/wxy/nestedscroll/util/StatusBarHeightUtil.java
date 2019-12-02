package com.wxy.nestedscroll.util;

import android.content.res.Resources;

public class StatusBarHeightUtil {
    public static int getStatusBarHeight(Resources resources) {
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}
