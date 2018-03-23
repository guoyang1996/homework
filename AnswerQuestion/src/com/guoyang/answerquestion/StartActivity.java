package com.guoyang.answerquestion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ҳ������Ϊ�ޱ���
		setContentView(R.layout.start);
		Button start_btn = (Button)findViewById(R.id._btn_start);
		start_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				 Intent intent = new Intent(StartActivity .this,MainActivity.class);
				 startActivity(intent);
				 finish();
				
			}});
	}

	
	

}
