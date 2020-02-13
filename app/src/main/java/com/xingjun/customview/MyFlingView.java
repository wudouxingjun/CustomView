package com.xingjun.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

public class MyFlingView extends View {

	private  Scroller mScroller;
	private  VelocityTracker  mVelocityTracker;
	private  int  DISTANCE=300;
	private  int RATE=5;//Ĭ������
	private  boolean  isScrolling=false;
	public MyFlingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyFlingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyFlingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	
    	return super.onTouchEvent(event);
    }
    /**
    * @ClassName: Flinger 
    * @Description: TODO(�̹߳�����) 
    * @author A18ccms a18ccms_gmail_com 
    * @date 2016-3-11 ����3:43:30 
    *
     */
    private  class  Flinger  implements  Runnable{

    	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
    	
    }
}
