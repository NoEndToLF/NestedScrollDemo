package com.wxy.nestedscroll.nested;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class NestedScrollParentView extends LinearLayout implements NestedScrollingParent2 {
    private boolean isCanScroll;
    private int ivHeight;
    private ImageView imageView;
    private ValueAnimator anim;
    private RecyclerView recyclerView ;
    private boolean isNestedFling;
    private float mTotalDy;
    private float mLastScale;
    private   float TARGET_HEIGHT = 1500;
    private boolean isRecovering ;//是否正在自动回弹中
    private View view;
    private View contentView;
    private NestedScrollingParentHelper nestedScrollingParentHelper;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView=findViewWithTag("imageView");
        view=findViewWithTag("view");
        contentView=findViewWithTag("contentView");
        ivHeight=imageView.getHeight();
        initAnim();
        nestedScrollingParentHelper=new NestedScrollingParentHelper(this);
    }

    private void initAnim() {
        anim = new ValueAnimator();
        anim.setFloatValues(imageView.getScaleX(),1f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                imageView.setScaleX(currentValue);
                imageView.setScaleY(currentValue);
                resetRecycleViewScale();
                resetViewScale(view);

            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTotalDy=0;
                imageView.setScaleX(1);
                imageView.setScaleY(1);
                resetRecycleViewScale();
                resetViewScale(view);
                isRecovering=false;
                isNestedFling=false;

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public NestedScrollParentView(Context context) {
        super(context);
    }

    public NestedScrollParentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollParentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;

    }
    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child,target,axes,type);
        TARGET_HEIGHT=imageView.getHeight()*1.5f;
    }


    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        isNestedFling=true;
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        nestedScrollingParentHelper.onStopNestedScroll(target,type);
        switch (type){
            case ViewCompat.TYPE_NON_TOUCH:
                    doAnim();
                break;
            case ViewCompat.TYPE_TOUCH:
                if (isNestedFling)return;
                    doAnim();
                break;
        }

    }
    private void doAnim() {
        if (imageView.getScaleX()>1f){
            if (isRecovering) return;
            isRecovering = true;
            anim.setFloatValues(imageView.getScaleX(),1f);
            anim.start();
        }
    }



    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (isRecovering) return;
        if (recyclerView==null){
            recyclerView=(RecyclerView)findViewWithTag("recyclerView");
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = getMeasuredHeight()-view.getHeight();
            recyclerView.setLayoutParams(params);
        }
        if (recyclerView.computeVerticalScrollOffset()==0){
            isCanScroll=true;
        }else {
            isCanScroll=false;
        }
        if (isCanScroll){
            //下拉是放大图片
            if (dy<0) {
                if (imageView.getTranslationY()<0){
                    translate(dy);
                    if (imageView.getTranslationY()!=-imageView.getHeight()){
                        consumed[1] = dy;
                    }
                }else {
                    scale(target,type, dy);
                    if (imageView.getScaleX() != 1) {
                        consumed[1] = dy;
                    }
                }

            }
            //上滑是把图片顶上去
            else {
                if (imageView.getScaleX()>1){
                    scale(target,type, dy);
                    if (imageView.getScaleX() >= 1) {
                        consumed[1] = dy;
                    }
                }else {
                    translate(dy);
                    if (imageView.getTranslationY() != -imageView.getHeight()) {
                        consumed[1] = dy;

                    }
                }
            }

        }
    }

    private void translate(int dy) {
       float tranlateY=imageView.getTranslationY()-dy;
       if (tranlateY<-imageView.getHeight()){
           tranlateY=-imageView.getHeight();
       }else if (tranlateY>0){
           tranlateY=0;
       }
        view.setTranslationY(tranlateY);
       imageView.setTranslationY(tranlateY);
        resetRecyclerViewTranslate();

    }

    private void scale( View target,int type, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
        mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
        ViewCompat.setScaleX(imageView, mLastScale);
        ViewCompat.setScaleY(imageView, mLastScale);
        resetRecycleViewScale();
        resetViewScale(view);
        if (type==ViewCompat.TYPE_NON_TOUCH){
            if (mLastScale==2){
                if (target instanceof NestedScrollChildView){
                    ((NestedScrollChildView) target).stopScroll();

                }else if (target instanceof RecyclerView){
                    ((RecyclerView) target).stopScroll();
                }
        }
    }
    }
    private void resetRecyclerViewTranslate(){
        float y = imageView.getHeight() +imageView.getTranslationY()+view.getHeight()+view.getTranslationX();
        if (y < 0) {
            y = 0;
        }
        recyclerView.setY(y);
    }
    private void resetRecycleViewScale() {
        float y = imageView.getHeight() +imageView.getHeight()*(imageView.getScaleY()-1)/2+view.getHeight();
        if (y < 0) {
            y = 0;
        }
        recyclerView.setY(y);
    }
    private void resetViewScale(View view) {
        float y = imageView.getHeight() +imageView.getHeight()*(imageView.getScaleY()-1)/2;
        if (y < 0) {
            y = 0;
        }
        view.setY(y);
    }

}
