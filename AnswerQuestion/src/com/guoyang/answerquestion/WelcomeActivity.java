package com.guoyang.answerquestion;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

/**
 * ���ǿ�������ĵ�һ������ҳ��
 */
public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ҳ������Ϊ�ޱ���
		setContentView(R.layout.welcome);
			//����һ����ʱ�����ڴ�ҳ����ͣ��3��Ȼ����ת����ҳ��,
			 (new Timer()).schedule(new TimerTask() {
			 public void run() {
			 Intent intent = new Intent(WelcomeActivity.this,StartActivity.class);
			 startActivity(intent);
			 finish();
			 }
			 }, 1500);
		}
	}

