package com.guoyang.answerquestion;

import java.util.List;

import com.answer.entity.QuestBean;
import com.answer.tools.QuestionGetUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends Activity{
	@Override  
	protected void onStop() {  
	    Intent intent = new Intent(EndActivity.this,MusicService.class);  
	    stopService(intent);  
	    super.onStop();  
	}  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);//页面设置为无标题
		setContentView(R.layout.end);
		
		
		Button start_btn = (Button)findViewById(R.id._btn_end);
		start_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				 Intent intent = new Intent(EndActivity .this,StartActivity.class);
				 startActivity(intent);
				 finish();
				
			}});
		List<QuestBean> questBeans = QuestionGetUtil.getQuestions(this);
		int grade = 0;
		for(QuestBean questBean:questBeans){
			if(questBean.getMyAnswer().equals(questBean.getRightAnswer())){
				grade++;
				questBean.setMyAnswer("");
			}
		}
		TextView tv = (TextView)findViewById(R.id._text_grade);
		if(grade<4||(grade>5&&grade<8)){
			//设置音效
			Intent intent = new Intent(EndActivity.this,MusicService.class);  
			intent.putExtra("state", "failure");
			startService(intent); 
			tv.setText("很遗憾！您一共答对了"+grade+"道题！\n未满足过关要求！");
		}else{
			//设置音效
			Intent intent = new Intent(EndActivity.this,MusicService.class);  
			intent.putExtra("state", "amazing");
			startService(intent); 
			//设置音效
			tv.setText("恭喜过关！您一共答对了"+grade+"道题！");
		}
		
	}
}
