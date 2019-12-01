package com.wxy.nestedscroll.nested;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import static androidx.core.view.ViewCompat.TYPE_TOUCH;

public class NestedScrollChildView extends LinearLayout implements NestedScrollingChild2 {
    private int lastY = -1;
    private int lastX = -1;
    private int[] offset = new int[2];
    private int[] consumed = new int[2];
    protected VelocityTracker mVelocityTracker;
    private  NestedScrollingChildHelper nestedScrollingChildHelper;
    protected Scroller scroller;
    private int mLastFlingX;
    private int mLastFlingY;
    private final int[] mScrollConsumed = new int[2];
    private int mMaxFlingVelocity;
    private float downX,downY;
    public NestedScrollChildView(Context context) {
        this(context,null);
    }

    public NestedScrollChildView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NestedScrollChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewConfiguration vc = ViewConfiguration.get(context);
        setNestedScrollingEnabled(true);
        getNestedScrollingChildHelper().setNestedScrollingEnabled(true);
        scroller=new Scroller(context);
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onFinishInflate() {
        addOnclickListner(this);
        super.onFinishInflate();
    }

    private void addOnclickListner(View view) {
        if (view instanceof ViewGroup){
            for (int i=0;i<getChildCount();i++){
                View mView=getChildAt(i);
                mView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }else {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept=false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=event.getRawX();
                downY=event.getRawY();
                intercept=false;
                cancleFling();//停止惯性滑动
                lastY = (int) event.getRawY();
                lastX = (int) event.getRawX();
                //即将开始滑动，支持垂直方向的滑动
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, TYPE_TOUCH);
                break;
            case MotionEvent.ACTION_MOVE:
                intercept=true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float clickX = downX - event.getRawX();
                float clickY = downY -  event.getRawY();
                if (Math.abs(clickX)<= ViewConfiguration.get(getContext()).getScaledTouchSlop()&&
                        Math.abs(clickY)<= ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    intercept=false;
                }else {
                    intercept=true;

                }
                break;
        }
        return intercept;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            int dx = mLastFlingX - x;
            int dy = mLastFlingY - y;
            mLastFlingX = x;
            mLastFlingY = y;
            startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL, ViewCompat.TYPE_NON_TOUCH);
            if (dispatchNestedPreScroll(dx, dy, mScrollConsumed, null, ViewCompat.TYPE_NON_TOUCH)) {
                //计算父控件消耗后，剩下的距离
                dx -= mScrollConsumed[0];
                dy -= mScrollConsumed[1];
            }
            //因为之前默认向父控件传递的竖直方向，所以这里子控件也消耗剩下的竖直方向
            int hResult = 0;
            int vResult = 0;
            int leaveDx = 0;//子控件水平fling 消耗的距离
            int leaveDy = 0;//父控件竖直fling 消耗的距离

            //在父控件消耗完之后，子控件开始消耗
            if (dx != 0) {
                leaveDx = 0;
                hResult = dx - leaveDx;//得到子控件消耗后剩下的水平距离
            }
            if (dy != 0) {
                leaveDy = 0;//得到子控件消耗后剩下的竖直距离
                vResult = dy - leaveDy;
            }
            //将最后剩余的部分，再次还给父控件
            dispatchNestedScroll(leaveDx, leaveDy, hResult, vResult, null, ViewCompat.TYPE_NON_TOUCH);
            invalidate();
        }else {
                stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
            cancleFling();
        }
    }

    public NestedScrollingChildHelper getNestedScrollingChildHelper(){
        if (nestedScrollingChildHelper==null){
            nestedScrollingChildHelper=new NestedScrollingChildHelper(this);

        }
        return nestedScrollingChildHelper;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //添加速度检测器，用于处理fling效果
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                int currentY = (int) (event.getRawY());
                int currentX = (int) (event.getRawX());
                int dy = lastY - currentY;
                int dx = lastX - currentX;
                if (dispatchNestedPreScroll(dx, dy, consumed, offset, TYPE_TOUCH)) {
                    //如果父控件需要消耗，则处理父控件消耗的部分数据
                    dy -= consumed[1];
                    dx -= consumed[0];
                }
                //剩余的自己再次消耗，
                int consumedX = 0, consumedY = 0;
                    consumedY = childConsumedY(dy);
                //子控件的滑动事件处理完成之后，剩余的再次传递给父控件，让父控件进行消耗
                //因为没有滑动事件，因此次数自己滑动距离为0，剩余的再次全部还给父控件
                dispatchNestedScroll(consumedX, consumedY, dx - consumedX, dy - consumedY, null, TYPE_TOUCH);
                lastY = currentY;
                lastX = currentX;
                break;
            case MotionEvent.ACTION_UP:  //当手指抬起的时，结束嵌套滑动传递,并判断是否产生了fling效果
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                int yvel = (int) mVelocityTracker.getYVelocity();
                int xvel = (int) mVelocityTracker.getXVelocity();
                dispatchNestedFling(xvel,yvel,false);
                stopNestedScroll(TYPE_TOUCH);
                scroller.fling(0,0,0,yvel,-(getWidth()-getPaddingLeft()-getPaddingRight())*10,(getWidth()-getPaddingLeft()-getPaddingRight())*10,-(getHeight()-getPaddingBottom()-getPaddingTop())*10,(getHeight()-getPaddingBottom()-getPaddingTop())*10);
                invalidate();
                if (mVelocityTracker != null) {
                    mVelocityTracker.clear();
                }
                lastY = -1;
                lastX = -1;
                break;
        }
        return true;
    }
public void stopScroll(){
    if (scroller.computeScrollOffset()) {
        scroller.abortAnimation();
    }
    stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
}

    private void cancleFling() {
        mLastFlingX = 0;
        mLastFlingY = 0;
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }
    }
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getNestedScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getNestedScrollingChildHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getNestedScrollingChildHelper().startNestedScroll(axes);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return getNestedScrollingChildHelper().startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll() {
        getNestedScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public void stopNestedScroll(int type) {
        getNestedScrollingChildHelper().stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getNestedScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return getNestedScrollingChildHelper().hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return getNestedScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow, int type) {
        return getNestedScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }



    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return getNestedScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow,
                                           int type) {
        return getNestedScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow,
                type);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getNestedScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getNestedScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY);
    }
    /**
     * 触摸滑动时候子控件消耗多少竖直方向上的 ,由子控件自己决定
     *
     * @param dy 父控件消耗部分竖直fling后,剩余的距离
     * @return 子控件竖直fling，消耗的距离
     */
    private int childConsumedY(int dy) {

        return 0;
    }

    /**
     * 触摸滑动子控件消耗多少竖直方向上的,由子控件自己决定
     *
     * @param dx 父控件消耗部分水平fling后,剩余的距离
     * @return 子控件水平fling，消耗的距离
     */
    private int childConsumeX(int dx) {
        return 0;
    }

}
