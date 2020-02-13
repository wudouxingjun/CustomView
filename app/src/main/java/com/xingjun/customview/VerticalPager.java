package com.xingjun.customview;

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
	private final static int RATE = 5;// ���ʱ�׼
	private final static int DISTANCE = 300;// ���ʱ�׼
	private VelocityTracker mVelocityTracker;// �����ٶ�


	public VerticalPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public VerticalPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// ���һЩ��ʼ���Ĺ���
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
		// ȷ���ӿؼ���λ��
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
		// ������������
		mVelocityTracker.addMovement(event);
		int action = event.getAction();
		float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();// ��������
			}
			mLastMotionY = (int) y;// ��ð��µ���ָλ��
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = (int) (mLastMotionY - y);
			scrollBy(0, deltaY);
			invalidate();
			mLastMotionY = (int) y;
			break;
		case MotionEvent.ACTION_UP:
			mVelocityTracker.computeCurrentVelocity(1, 1000);
			float vy = mVelocityTracker.getYVelocity();// ���Y�����ϵ�����
			if (getScrollY() < 0) {
				mScroller.startScroll(0, -DISTANCE, 0, DISTANCE);
			} else if (getScrollY() > getHeight() * (getChildCount() - 1)) {
				View lastView = getChildAt(getChildCount() - 1);
				mScroller.startScroll(0, lastView.getTop() + DISTANCE, 0,
						-DISTANCE);
			} else {
				int position = getScrollY() / getHeight();
				View positionView = null;
				if (vy < -RATE) {// �»�
					positionView = getChildAt(position + 1);// �õ���ǰView
					mScroller.startScroll(0, positionView.getTop() - DISTANCE,
							0, DISTANCE);
				} else if (vy > RATE) {// �ϻ�
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
