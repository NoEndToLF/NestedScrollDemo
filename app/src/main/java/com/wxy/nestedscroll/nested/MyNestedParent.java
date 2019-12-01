package com.wxy.nestedscroll.nested;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MyNestedParent extends LinearLayout implements NestedScrollingParent2 {
    private boolean isCanScroll;
    private int ivHeight;
    private ImageView imageView;
    private ValueAnimator anim;
    private RecyclerView recyclerView ;
    private boolean isNestedFling;
    private boolean isDealFling;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView=(ImageView)getChildAt(0);
        recyclerView=(RecyclerView)getChildAt(1);
        ivHeight=imageView.getHeight();
        anim = new ValueAnimator();
    }

    public MyNestedParent(Context context) {
        super(context);
    }

    public MyNestedParent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return target instanceof RecyclerView;

    }
    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
    }


    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.v("onNestedFling=","onNestedFling");
        isNestedFling=true;
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        switch (type){
            case ViewCompat.TYPE_NON_TOUCH:
                if (imageView.getScaleX()>1f){
                    if (!anim.isRunning()){
                        anim.setFloatValues(imageView.getScaleX(),1f);
                        anim.setInterpolator(new DecelerateInterpolator());//添加减速插值器
                        anim.setDuration(300);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float currentValue = (float) animation.getAnimatedValue();
                                imageView.setScaleX(currentValue);
                                imageView.setScaleY(currentValue);
                                resetRecyclerView();
                            }
                        });
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                imageView.setScaleX(1);
                                imageView.setScaleY(1);
                                resetRecyclerView();
                                isNestedFling=false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        anim.start();
                    }
                }
                break;
            case ViewCompat.TYPE_TOUCH:
                if (isNestedFling)return;
                if (imageView.getScaleX()>1f){
                    if (!anim.isRunning()){
                        anim.setFloatValues(imageView.getScaleX(),1f);
                        anim.setInterpolator(new DecelerateInterpolator());//添加减速插值器
                        anim.setDuration(300);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float currentValue = (float) animation.getAnimatedValue();
                                imageView.setScaleX(currentValue);
                                imageView.setScaleY(currentValue);
                                resetRecyclerView();
                            }
                        });
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                imageView.setScaleX(1);
                                imageView.setScaleY(1);
                                resetRecyclerView();
                                isNestedFling=false;

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        anim.start();
                    }
                }
                break;
        }

    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
//        Log.v("heihei=","onNestedPreScroll");
        if (recyclerView.computeVerticalScrollOffset()==0){
            isCanScroll=true;
        }else {
            isCanScroll=false;
        }

        if (isCanScroll){
            if (dy<0){
                float scaleX=imageView.getScaleX()*1.02f;
                imageView.setScaleX(scaleX);
                imageView.setScaleY(scaleX);
                resetRecyclerView();
            }else {
                float scaleX=imageView.getScaleX()/1.02f;
                if (scaleX<1f){
                    scaleX=1f;
                }
                imageView.setScaleX(scaleX);
                imageView.setScaleY(scaleX);
                resetRecyclerView();
            }
            if (imageView.getScaleX()!=1){
                consumed[1]=dy;
            }

        }
    }

    private void resetRecyclerView() {
        float y = imageView.getHeight() +imageView.getHeight()*(imageView.getScaleY()-1)/2;
        if (y < 0) {
            y = 0;
        }
        recyclerView.setY(y);
    }
}
