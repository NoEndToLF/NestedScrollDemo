package com.wxy.nestedscroll.nested;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.wxy.nestedscroll.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class NestedScrollParentView2 extends FrameLayout implements NestedScrollingParent2 {
    private boolean isCanScroll;
    private ValueAnimator anim;
    private boolean isNestedFling;
    private float mTotalDy;
    private float mLastScale;
    private   float TARGET_HEIGHT = 1500;
    private boolean isRecovering ;//是否正在自动回弹中
    private View headView;
    private View floatingView;
    private View toolbar;
    private View contentView;
    private View recyclerView;
    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private TypedArray mTypedArray;
    private boolean isfirstLayout;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findView();
        initAnim();
        nestedScrollingParentHelper=new NestedScrollingParentHelper(this);
        isfirstLayout=true;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isfirstLayout){
        ViewGroup.LayoutParams layoutParams=recyclerView.getLayoutParams();
        layoutParams.height=recyclerView.getHeight()-floatingView.getHeight()-toolbar.getHeight();
        recyclerView.setLayoutParams(layoutParams);
        recyclerView.setY(headView.getHeight()+floatingView.getHeight());
        isfirstLayout=false;
        }
    }
    private void findView() {
        final int N = mTypedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = mTypedArray.getIndex(i);
            switch (attr){
                case R.styleable.NestedScrollChildView_head_view_id:
                    headView=(View) findViewById(mTypedArray.getResourceId(attr,0));
                    break;
                case R.styleable.NestedScrollChildView_floating_view_id:
                    floatingView=findViewById(mTypedArray.getResourceId(attr,0));
                    break;
                case R.styleable.NestedScrollChildView_toolbar_id:
                    toolbar=findViewById(mTypedArray.getResourceId(attr,0));
                    break;
                case R.styleable.NestedScrollChildView_content_view_id:
                    contentView=findViewById(mTypedArray.getResourceId(attr,0));
                    break;
                case R.styleable.NestedScrollChildView_recycle_view_id:
                    recyclerView=findViewById(mTypedArray.getResourceId(attr,0));
                    break;

            }
        }
        mTypedArray.recycle();
    }
    private void initAnim() {
        anim = new ValueAnimator();
        anim.setFloatValues(headView.getScaleX(),1f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                headView.setScaleX(currentValue);
                headView.setScaleY(currentValue);
                resetRecycleViewScale();
                resetViewScale(floatingView);

            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTotalDy=0;
                headView.setScaleX(1);
                headView.setScaleY(1);
                resetRecycleViewScale();
                resetViewScale(floatingView);
                isRecovering=false;
                isNestedFling=false;
                //重置recyclerView的高度
                ViewGroup.LayoutParams layoutParams=recyclerView.getLayoutParams();
                layoutParams.height= (int) (getMeasuredHeight()-floatingView.getHeight()-toolbar.getHeight()-(headView.getHeight()-toolbar.getHeight())+Math.abs(headView.getTranslationY()));
                recyclerView.setLayoutParams(layoutParams);
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public NestedScrollParentView2(Context context) {
        this(context,null);
    }

    public NestedScrollParentView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NestedScrollParentView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.NestedScrollChildView);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;

    }
    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child,target,axes,type);
        TARGET_HEIGHT=headView.getHeight()*1.5f;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        isNestedFling=true;
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        Log.v("heihei=","onStopNestedScroll");
        //重置recyclerView的高度

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
        if (headView.getScaleX()>1f){
            if (isRecovering) return;
            isRecovering = true;
            anim.setFloatValues(headView.getScaleX(),1f);
            anim.start();
        }

    }



    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (isRecovering) return;

        if (target instanceof RecyclerView){
        if (((RecyclerView)target).computeVerticalScrollOffset()==0){
            isCanScroll=true;
        }else {
            isCanScroll=false;
        }
        }else {
            isCanScroll=true;

        }
        if (isCanScroll){
            //下拉是放大图片
            if (dy<0) {
                if (headView.getTranslationY()<0){
                    translate(dy);
                    if (headView.getTranslationY()!=-(headView.getHeight()-toolbar.getHeight())){
                        consumed[1] = dy;
                    }
                }else {
                    scale(target,type, dy);
                    if (headView.getScaleX() != 1) {
                        consumed[1] = dy;
                    }
                }
            }
            //上滑是把图片顶上去
            else {
                if (headView.getScaleX()>1){
                    scale(target,type, dy);
                    if (headView.getScaleX() >= 1) {
                        consumed[1] = dy;
                    }
                }else {
                    translate(dy);
                    if (headView.getTranslationY() != -(headView.getHeight()-toolbar.getHeight())) {
                        consumed[1] = dy;
                    }
                }
            }
            ViewGroup.LayoutParams layoutParams=recyclerView.getLayoutParams();
            layoutParams.height= (int) (getMeasuredHeight()-floatingView.getHeight()-toolbar.getHeight()-(headView.getHeight()-toolbar.getHeight())+Math.abs(headView.getTranslationY()));
            recyclerView.setLayoutParams(layoutParams);
        }
    }
    private void translate(int dy) {
       float tranlateY=headView.getTranslationY()-dy;
       if (tranlateY<-(headView.getHeight()-toolbar.getHeight())){
           tranlateY=-(headView.getHeight()-toolbar.getHeight());
       }else if (tranlateY>0){
           tranlateY=0;
       }
       floatingView.setTranslationY(tranlateY);
       headView.setTranslationY(tranlateY);
        resetRecyclerViewTranslate();
    }

    private void scale( View target,int type, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
        mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
        ViewCompat.setScaleX(headView, mLastScale);
        ViewCompat.setScaleY(headView, mLastScale);
        resetRecycleViewScale();
        resetViewScale(floatingView);
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
        float y = headView.getHeight() +headView.getTranslationY()+floatingView.getHeight();
        if (y < 0) {
            y = 0;
        }
        recyclerView.setY(y);
    }
    private void resetRecycleViewScale() {
        float y = floatingView.getHeight() +headView.getHeight()+headView.getHeight()*(headView.getScaleY()-1)/2;
        if (y < 0) {
            y = 0;
        }
        recyclerView.setY(y);
    }
    private void resetViewScale(View view) {
        float y = headView.getHeight() +headView.getHeight()*(headView.getScaleY()-1)/2;
        if (y < 0) {
            y = 0;
        }
        view.setY(y);
    }

}
