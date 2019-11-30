package com.wxy.nestedscroll.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TextViewBehavior extends CoordinatorLayout.Behavior<TextView> {
private boolean isCanScroll;
    public TextViewBehavior() {
    }

    public TextViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, TextView child, MotionEvent ev) {

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        RecyclerView list = (RecyclerView) target;
        if (list.computeVerticalScrollOffset()==0){
            isCanScroll=true;
        }else {
            isCanScroll=false;
        }
           if (isCanScroll){
              if (dy>0){
                  float finalY=child.getTranslationY()-dy;
                  if (finalY<=-child.getHeight()){
                      finalY=-child.getHeight();
                  }
                  child.setTranslationY(finalY );
                  if (finalY!=-child.getHeight()){
                      consumed[1]=dy;
                  }
              }else {
                  float finalY=child.getTranslationY()-dy;
                  if (finalY>=0){
                      finalY=0;
                  }
                      child.setTranslationY(finalY );
                  if (finalY!=0){
                      consumed[1]=dy;
                  }}
    }}

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }
}