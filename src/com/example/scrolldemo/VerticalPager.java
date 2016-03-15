package com.example.scrolldemo;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class VerticalPager extends ViewGroup {

	private Scroller mScroller;
	private Context mContext;
	private final static int RATE = 5;// 速率标准
	private final static int DISTANCE = 300;// 速率标准
	private VelocityTracker mVelocityTracker;// 计算速度


	public VerticalPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public VerticalPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 完成一些初始化的工作
		this.mContext = context;
		mScroller = new Scroller(context);
	}

	public VerticalPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int totalHeight = 0;
		int childCount = getChildCount();
		// 确定子控件的位置
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);
			childView.layout(l, totalHeight + t, r, totalHeight + b);
			totalHeight += b;
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).measure(width, height);
		}
		setMeasuredDimension(width, height);
	}

	private int mLastMotionY;

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mVelocityTracker == null)
			mVelocityTracker = VelocityTracker.obtain();
		// 监听滑动监听
		mVelocityTracker.addMovement(event);
		int action = event.getAction();
		float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();// 开启动画
			}
			mLastMotionY = (int) y;// 获得按下的手指位置
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = (int) (mLastMotionY - y);
			scrollBy(0, deltaY);
			invalidate();
			mLastMotionY = (int) y;
			break;
		case MotionEvent.ACTION_UP:
			mVelocityTracker.computeCurrentVelocity(1, 1000);
			float vy = mVelocityTracker.getYVelocity();// 获得Y方向上的速率
			if (getScrollY() < 0) {
				mScroller.startScroll(0, -DISTANCE, 0, DISTANCE);
			} else if (getScrollY() > getHeight() * (getChildCount() - 1)) {
				View lastView = getChildAt(getChildCount() - 1);
				mScroller.startScroll(0, lastView.getTop() + DISTANCE, 0,
						-DISTANCE);
			} else {
				int position = getScrollY() / getHeight();
				View positionView = null;
				if (vy < -RATE) {// 下滑
					positionView = getChildAt(position + 1);// 得到当前View
					mScroller.startScroll(0, positionView.getTop() - DISTANCE,
							0, DISTANCE);
				} else if (vy > RATE) {// 上滑
					positionView = getChildAt(position);
					mScroller.startScroll(0, positionView.getTop() - DISTANCE,
							0, DISTANCE);
				} else {
					int mod = getScrollY() % getHeight();
					if (mod > getHeight() / 2) {
						positionView = getChildAt(position + 1);
						mScroller.startScroll(0, positionView.getTop()
								- DISTANCE, 0, DISTANCE);
					} else {
						positionView = getChildAt(position);
						mScroller.startScroll(0, positionView.getTop()
								- DISTANCE, 0, DISTANCE);
					}
				}
			}
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollTo(0, mScroller.getCurrY());
		}
	}
}
