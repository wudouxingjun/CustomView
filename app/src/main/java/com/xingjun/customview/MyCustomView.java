package com.xingjun.customview;

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
	private VelocityTracker mVelocityTracker;// ������������
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
		// ��ʼ��Scrollerʵ��
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
		// ƽ���ٶ�
		mVelocityTracker.computeCurrentVelocity(1000);
		mVelocityTracker.getXVelocity();
		mVelocityTracker.getYVelocity();
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ������һ�ζ���
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
			// ����velocityXˮƽ�����ϵ�����
			int velocityX = (int) velocityTracker.getXVelocity();
			// ���Ϊ���һ���,�ж������ڵ�һҳ
			if (velocityX > SNAP_VELOCITY && curScreen > 0) {
				snapToScreen(curScreen - 1);// ������һ��
			} else if (velocityX < -SNAP_VELOCITY
					&& curScreen < getChildCount() - 1) {
				// ���󻬶�
				snapToScreen(curScreen + 1);
			} else {// �����ƶ����ж��Ǳ����ڱ���Ļ�������л�����һ����Ļ
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
	 * @Description: TODO(�ж�ƫ��ֵ��ȷ���ƶ�����һ����Ļ)
	 * @param �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	private void snapToDestination() {
		// TODO Auto-generated method stub
		int scrollX = getScrollX();
		int scrollY = getScrollY();
		// �ж��Ƿ񳬹���һ�����м�λ�ã�����ﵽ�͵ִ���һ�������򱣳���ԭ��Ļ
		// ֱ��ʹ�������ʽ�ж�����һ����Ļ ǰ������Լ�
		// �ж��Ƿ񳬹���һ�����м�λ�ã�����ﵽ�͵ִ���һ�������򱣳���ԭ��Ļ
		// ������һ���򵥹�ʽ��˼�ǣ����赱ǰ����ƫ��ֵ�� scrollCurX ����ÿ����Ļһ��Ŀ�ȣ�����ÿ����Ļ�Ŀ�Ⱦ���
		// ����Ŀ��������λ���ˡ� ����ÿ����Ļ���Ϊ320dip, ���ǻ�����500dip��������Ȼ����Ӧ�õ���ڶ���
		int destScreen = (getScrollX() + getWidth() / 2) / getWidth();
		snapToScreen(destScreen);// �жϻ�����һ��
	}

	/***
	 * @Title: snapToScreen
	 * @Description: TODO(������Ŀ��λ����Ļ)
	 * @param @param i �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	private void snapToScreen(int screen) {
		// TODO Auto-generated method stub
		curScreen = screen;
		if (curScreen > getChildCount() - 1) {
			curScreen = getChildCount() - 1;
		}
		// Ϊ�˴ﵽ��һ��Ļ���ߵ�ǰ��Ļ��������Ҫ���������ľ���.����dxֵ���������󻬶���Ҳ�������ֻ���
		int dx = curScreen * getWidth() - getScrollX();
		mScroller.startScroll(getScrollX(), 0, dx, 0, Math.abs(dx) * 2);
		invalidate();// ���»���
	}

	/***
	 * ��������
	 */
	@Override
	public void computeScroll() {
		// �������true����ʾ������û�н���
		// ��Ϊǰ��startScroll������ֻ����startScroll���ʱ �Ż�Ϊfalse
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		} else {

		}
	}

	private int curScreen = 0;
	// ����״̬:�Ƿ��ڻ���״̬
	private static final int TOUCH_STATE_RESET = 0;// ʲô��û������״̬
	private static final int TOUCH_STATE_SCROLLING = 1;// ��ʼ������״̬
	private int mTouchState = TOUCH_STATE_RESET;// Ĭ����ʲô��û������״̬
	// �������¼�
	public static int SNAP_VELOCITY = 600;
	private int mTouchSlop = 0;
	private float mLastMotionX = 0;// ��ס�ϴδ�������λ��
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
			// ������С�������룬�Ϳ�ʼ����
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
			mTouchState = TOUCH_STATE_RESET;// �޸�״̬Ϊ������
			break;
		default:
			break;
		}
		return mTouchState != TOUCH_STATE_RESET;
	}

	/**
	 * @Title: startMove
	 * @Description: TODO(��ʼ�ƶ�)
	 * @param �趨�ļ�
	 * @return void ��������
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
