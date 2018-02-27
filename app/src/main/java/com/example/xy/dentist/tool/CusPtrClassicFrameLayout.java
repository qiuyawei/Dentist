package com.example.xy.dentist.tool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * 对于问题一：

 安卓有一个GestureDetector和GestureDetector.OnGestureListener接口，将此类放入
 dispatchTouchEvent方法然后调用detector.onTouchEvent(e) 即可截获，接口里面有一个
 类onScroll方法可以完整滑动的距离，onScroll方法的原型
 distanceX和distanceY告诉的是一次滑动的距离，从两个值的绝对值大小可以知道是水平滑动还是垂直滑动。

 对于问题二：

 这篇文章给我一个思路http://dengyin2000.iteye.com/blog/2232210，当确定触摸到ConvenientBanner
 后调用PtrClassicFrameLayout。requestDisallowInterceptTouchEvent(true)方法，
 让PtrClassicFrameLayout不再处理事件，这样问题就解决了。

 对于问题三：

 我直接加入一个Flag，当第一次滑动是水平的时候，我会给一个标记，之后的所有事件都会分发给ConvenientBanner
 ，结束条件是ActionUp事件被触发后。
 */

public class CusPtrClassicFrameLayout extends PtrClassicFrameLayout {
    public CusPtrClassicFrameLayout(Context context) {
        super(context);
        initGesture();
    }

    public CusPtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGesture();

    }

    public CusPtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initGesture();
    }

    private GestureDetector detector;
    private boolean mIsDisallowIntercept = false;
    private void initGesture() {
        //如何判断垂直事件还是水平事件
   /*     安卓有一个GestureDetector和GestureDetector.OnGestureListener接口，将此类放入
        dispatchTouchEvent方法然后调用detector.onTouchEvent(e) 即可截获，接口里面有一个
        类onScroll方法可以完整滑动的距离，onScroll方法的原型
        onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
          }
distanceX和distanceY告诉的是一次滑动的距离，从两个值的绝对值大小可以知道是水平滑动还是垂直滑动。*/
        detector = new GestureDetector(getContext(),gestureListener);
    }

    private boolean mIsHorizontalMode = false;
    private boolean isFirst = true;
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            isFirst = true;
            mIsHorizontalMode = false;
            mIsDisallowIntercept = false;
            return super.dispatchTouchEvent(e);
        }
        if (detector.onTouchEvent(e) && mIsDisallowIntercept && mIsHorizontalMode){
            return dispatchTouchEventSupper(e);
        }
        if (mIsHorizontalMode) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        this.mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float disX, disY;
            if(distanceX < 0) {
                disX = -distanceX;
            } else {
                disX = distanceX;
            }
            if(distanceY < 0) {
                disY = -distanceY;
            } else {
                disY = distanceY;
            }

            if (disX > disY) {
                if (isFirst) {
                    mIsHorizontalMode = true;
                    isFirst = false;
                }
            } else {
                if (isFirst) {
                    mIsHorizontalMode = false;
                    isFirst = false;
                }
                return false;//垂直滑动会返回false
            }

            return true;//水平滑动会返回true
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };
}
