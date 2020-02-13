package com.xingjun.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

public class DragView extends View {

	private int lastX;
	private int lastY;


	public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public DragView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DragView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = x;
			lastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int offX = x - lastX;
			int offY = y - lastY;
//			layout(getLeft() + offX, getTop() + offY, getRight() + offX,
//					getBottom() + offY);
//            offsetLeftAndRight(offX);
//            offsetTopAndBottom(offY);
//			ViewGroup.MarginLayoutParams  mlp=(MarginLayoutParams)getLayoutParams();
//			mlp.leftMargin=getLeft()+offX;
//			mlp.topMargin=getTop()+offY;
//			setLayoutParams(mlp);
			((View)getParent()).scrollBy(-offX, -offY);
			break;
		case MotionEvent.ACTION_UP:

			break;
		default:
			break;
		}
		return true;
		// return super.onTouchEvent(event);
	}
}
