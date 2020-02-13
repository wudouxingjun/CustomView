package com.xingjun.customview;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TimerButton extends Button implements OnClickListener {

	private int countDown = 60;
	private Timer  timer;
	private TimerTask  timerTask;
	private Context  mContext;
	private String  textAfter="������»�ȡ";
	private String  textbefore="�����ȡ��֤��";
	private OnClickListener mOnClickListener;
	public OnClickListener getmOnClickListener() {
		return mOnClickListener;
	}

	public void setmOnClickListener(OnClickListener mOnClickListener) {
		if(mOnClickListener instanceof TimerButton){
			super.setOnClickListener(mOnClickListener);
		}else{
			this.mOnClickListener=mOnClickListener;
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	public  void  onDestory(){
    	 
    }
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x1011:
				TimerButton.this.setText(countDown-- + textAfter);
				if(countDown<0){
					setEnabled(true);
					setText(textbefore);
					clearTime();
				}
				break;
			default:
				break;
			}
		};
	};

	public TimerButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public TimerButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setOnClickListener(this);
		mContext=context;
	}

	public TimerButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mOnClickListener!=null){
			mOnClickListener.onClick(v);
		}
		initTimer();
		setEnabled(false);
		setText(countDown+textAfter);
		timer.schedule(timerTask, 0, 1000);
	}
    public void  clearTime(){
    	if(timer!=null){
    		timer.cancel();
    		timer=null;
    	}
    	if(timerTask!=null){
    		timerTask.cancel();
    		timerTask=null;
    	}
    }
	private void initTimer() {
		// TODO Auto-generated method stub
		countDown=60;
		timer=new Timer();
		timerTask=new  TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0x1011);
			}
		};
	}
	
}
