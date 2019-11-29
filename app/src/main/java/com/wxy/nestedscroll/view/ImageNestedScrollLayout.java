package com.wxy.nestedscroll.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ImageNestedScrollLayout extends LinearLayout implements NestedScrollingParent2 {
    private ImageView imageView;
    private RecyclerView recyclerView;
    private int imageHeight;
    private float a=1.01f;
    private NestedScrollingParentHelper mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    public ImageNestedScrollLayout(Context context) {
        super(context);
    }

    public ImageNestedScrollLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageNestedScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView=(ImageView) getChildAt(0);
        recyclerView=(RecyclerView)getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = getMeasuredHeight();
        recyclerView.setLayoutParams(layoutParams);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        imageHeight=imageView.getMeasuredHeight();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return target instanceof RecyclerView;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mNestedScrollingParentHelper.onStopNestedScroll(target, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyUnconsumed<0){
            //对于向下滑动
        }else {
            if (target == imageView){
                recyclerView.scrollBy(0, dyUnconsumed);
            }
        }
    }
    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        //对滑动距离进行修正
        if (recyclerView.canScrollVertically(1)) {
            //可以向上滑栋
            if (y > imageHeight) {
                y = imageHeight;
            }
        } else if ((recyclerView.canScrollVertically(-1))) {
            if (y < imageHeight) {
                y = imageHeight;
            }
        }

        super.scrollTo(x, y);
    }
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //这里不管手势滚动还是fling都处理
        boolean hideTop = dy > 0 && getScrollY() < imageHeight ;
        boolean showTop = dy < 0
                && getScrollY() >= 0
                && !target.canScrollVertically(-1)
                ;
        boolean cunsumedTop = hideTop || showTop;
//           if (cunsumedTop){
//
//            Log.v("heihei=","dy="+dy);
//            Log.v("heihei=","getScrollY="+getScrollY());
//            scrollBy(0, dy);
//            consumed[1] = dy;
//            }
           if (getScrollX()==0){
               ViewCompat.setScaleX(imageView,2);
               ViewCompat.setScaleY(imageView,2);
           }
    }
}
