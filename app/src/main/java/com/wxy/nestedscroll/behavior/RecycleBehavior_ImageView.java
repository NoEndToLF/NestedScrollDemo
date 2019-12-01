package com.wxy.nestedscroll.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleBehavior_ImageView extends CoordinatorLayout.Behavior<RecyclerView> {

    public RecycleBehavior_ImageView() {
    }

    public RecycleBehavior_ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        return dependency instanceof ImageView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {
        //计算列表y坐标，最小为0
        float y = dependency.getHeight() +dependency.getHeight()*(dependency.getScaleY()-1)/2;
        if (y < 0) {
            y = 0;
        }
        child.setY(y);
        return true;
    }


}
