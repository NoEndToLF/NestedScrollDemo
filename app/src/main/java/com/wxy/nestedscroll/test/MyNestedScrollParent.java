package com.wxy.nestedscroll.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

public class MyNestedScrollParent extends FrameLayout implements NestedScrollingParent2 {
    private static final String TAG = "MyNestedScrollParent";

    private NestedScrollingParentHelper mParentHelper;
    public MyNestedScrollParent(@NonNull Context context) {
        super(context);
        init();
    }

    public MyNestedScrollParent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public MyNestedScrollParent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        mParentHelper = new NestedScrollingParentHelper(this);
    }
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.d(TAG, "----父布局onStartNestedScroll----------------target:" + target + ",----+++this:" + this);
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }
    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }
    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mParentHelper.onNestedScrollAccepted(child,target,axes,type);
        Log.v("heihei=","onNestedScrollAccepted");

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mParentHelper.onStopNestedScroll(target,type);
        Log.v("heihei=","onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        Log.v("heihei=","onNestedScroll");

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
       Log.v("heihei=","onNestedPreScroll");
    }
}
