package com.wxy.nestedscroll.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ImageViewBehavior extends CoordinatorLayout.Behavior<ImageView> {
private boolean isCanScroll;
private int mDy;
    private float mLastScale;
private ValueAnimator anim;
private boolean isAnim;
    private static final float TARGET_HEIGHT = 1500;
    public ImageViewBehavior() {
    }



    public ImageViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, ImageView child, MotionEvent ev) {

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ImageView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ImageView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        mDy=child.getHeight();
        anim = new ValueAnimator();
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ImageView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        RecyclerView list = (RecyclerView) target;
        if (list.computeVerticalScrollOffset()==0){
            isCanScroll=true;
        }else {
            isCanScroll=false;
        }
           if (isCanScroll){
               if (dy<0){
                   float scaleX=child.getScaleX()*1.02f;
//                   if (scaleX>1.5f){
//                       scaleX=1.5f;
//                   }
               child.setScaleX(scaleX);
               child.setScaleY(scaleX);
               }else {
                   float scaleX=child.getScaleX()/1.02f;
                   if (scaleX<1f){
                       scaleX=1f;
                   }
                   child.setScaleX(scaleX);
                   child.setScaleY(scaleX);
               }
               if (child.getScaleX()!=1){
               consumed[1]=dy;

               }

    }
    }
    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull ImageView child, @NonNull MotionEvent ev) {
        Log.v("heihei=","onStopNestedScroll");
        return super.onTouchEvent(parent, child, ev);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ImageView child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        if (child.getScaleX()>1f){
            if (!anim.isRunning()){
                anim.setFloatValues(child.getScaleX(),1f);
            anim.setInterpolator(new DecelerateInterpolator());//添加减速插值器
            anim.setDuration(300);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float currentValue = (float) animation.getAnimatedValue();
                        child.setScaleX(currentValue);
                        child.setScaleY(currentValue);
                        float y = child.getHeight() + child.getHeight() * (child.getScaleY() - 1) / 2;
                        if (y < 0) {
                            y = 0;
                        }
                        target.setY(y);
                    }
                });
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnim=true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnim=false;
                    }
                });
                anim.start();
        }
        }
        Log.v("heihei=","onStopNestedScroll");
    }
}