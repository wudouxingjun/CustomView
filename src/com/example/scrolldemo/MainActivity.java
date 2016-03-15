package com.example.scrolldemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private RelativeLayout layout;

	private Button scrollToBtn;

	private Button scrollByBtn;
    
	private TimerButton  myButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		layout = (RelativeLayout) findViewById(R.id.layout);
		scrollToBtn = (Button) findViewById(R.id.scroll_to_btn);
		scrollByBtn = (Button) findViewById(R.id.scroll_by_btn);
		scrollToBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layout.scrollTo(-60, -100);
			}
		});
		scrollByBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layout.scrollBy(-60, -100);
			}
		});
		myButton=(TimerButton)findViewById(R.id.mybtn);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
