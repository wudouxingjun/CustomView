package com.example.scrolldemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;
import android.widget.Scroller;

public class MyCustomView extends ViewGroup {

	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;// 处理触摸的速率
	private int DISTANCE = 300;
	private Context mContext;
	private int mScaledTouchSlop;
    private OverScroller  overScroller;

	public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MyCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attr) {
		// 初始化Scroller实例
		mScroller = new Scroller(context);
		mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		//

	}

	public MyCustomView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		mVelocityTracker = VelocityTracker.obtain();
		mVelocityTracker.addMovement(event);
		// 平移速度
		mVelocityTracker.computeCurrentVelocity(1000);
		mVelocityTracker.getXVelocity();
		mVelocityTracker.getYVelocity();
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 结束上一次动画
			if (mScroller != null) {
				if (mScroller.isFinished())
					mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);
			scrollTo(deltaX, 0);
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_UP:
			VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			// 计算velocityX水平方向上的速率
			int velocityX = (int) velocityTracker.getXVelocity();
			// 如果为向右滑屏,判断屏大于第一页
			if (velocityX > SNAP_VELOCITY && curScreen > 0) {
				snapToScreen(curScreen - 1);// 滑到上一屏
			} else if (velocityX < -SNAP_VELOCITY
					&& curScreen < getChildCount() - 1) {
				// 向左滑动
				snapToScreen(curScreen + 1);
			} else {// 缓慢移动，判断是保留在本屏幕，还是切换到下一个屏幕
				snapToDestination();
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			mTouchState = TOUCH_STATE_RESET;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_RESET;
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * @Title: snapToDestination
	 * @Description: TODO(判断偏移值，确定移动到哪一个屏幕)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void snapToDestination() {
		// TODO Auto-generated method stub
		int scrollX = getScrollX();
		int scrollY = getScrollY();
		// 判断是否超过下一屏的中间位置，如果达到就抵达下一屏，否则保持在原屏幕
		// 直接使用这个公式判断是哪一个屏幕 前后或者自己
		// 判断是否超过下一屏的中间位置，如果达到就抵达下一屏，否则保持在原屏幕
		// 这样的一个简单公式意思是：假设当前滑屏偏移值即 scrollCurX 加上每个屏幕一半的宽度，除以每个屏幕的宽度就是
		// 我们目标屏所在位置了。 假如每个屏幕宽度为320dip, 我们滑到了500dip处，很显然我们应该到达第二屏
		int destScreen = (getScrollX() + getWidth() / 2) / getWidth();
		snapToScreen(destScreen);// 判断滑到哪一屏
	}

	/***
	 * @Title: snapToScreen
	 * @Description: TODO(滑动到目标位置屏幕)
	 * @param @param i 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void snapToScreen(int screen) {
		// TODO Auto-generated method stub
		curScreen = screen;
		if (curScreen > getChildCount() - 1) {
			curScreen = getChildCount() - 1;
		}
		// 为了达到下一屏幕或者当前屏幕，我们需要继续滑动的距离.根据dx值，可能想左滑动，也可能像又滑动
		int dx = curScreen * getWidth() - getScrollX();
		mScroller.startScroll(getScrollX(), 0, dx, 0, Math.abs(dx) * 2);
		invalidate();// 重新绘制
	}

	/***
	 * 滑动控制
	 */
	@Override
	public void computeScroll() {
		// 如果返回true，表示动画还没有结束
		// 因为前面startScroll，所以只有在startScroll完成时 才会为false
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		} else {

		}
	}

	private int curScreen = 0;
	// 两种状态:是否处于滑屏状态
	private static final int TOUCH_STATE_RESET = 0;// 什么都没有做的状态
	private static final int TOUCH_STATE_SCROLLING = 1;// 开始滑屏的状态
	private int mTouchState = TOUCH_STATE_RESET;// 默认是什么都没有做的状态
	// 处理触摸事件
	public static int SNAP_VELOCITY = 600;
	private int mTouchSlop = 0;
	private float mLastMotionX = 0;// 记住上次触摸屏的位置
	private float mLastMotionY = 0;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		if (action == MotionEvent.ACTION_MOVE
				&& mTouchState != TOUCH_STATE_RESET) {
			return true;
		}
		float x = ev.getX();
		float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			int xDiff = (int) (mLastMotionX - x);
			// 超过最小滑动距离，就开始滑动
			if (xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESET
					: TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_RESET;// 修改状态为不滚动
			break;
		default:
			break;
		}
		return mTouchState != TOUCH_STATE_RESET;
	}

	/**
	 * @Title: startMove
	 * @Description: TODO(开始移动)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void startMove() {

	}

	public void stopMove() {

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}
}
