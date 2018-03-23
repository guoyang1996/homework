package com.example.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {
	private static final long DELAY_MINUTE=3000; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				goMain();
			}
		}, DELAY_MINUTE);
	}
	/**
	 * 跳转
	 */
	private void goMain() {
		Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);
		WelcomeActivity.this.startActivity(intent);
		//必须手动结束
		WelcomeActivity.this.finish();
	}
}
