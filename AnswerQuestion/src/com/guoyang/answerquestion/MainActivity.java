package com.guoyang.answerquestion;

import java.util.ArrayList;
import java.util.List;

import com.answer.entity.QuestBean;
import com.answer.fragment.AnswerFragment;
import com.answer.tools.QuestionGetUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		View.OnClickListener {
	private ViewPager vp_answer;
	private ArrayList<Fragment> fragmentlists;
	private int guanNum = 0;
	private AlertDialog.Builder builder;
	private Button btn_previous;
	private Button btn_next;
	private Button btn_submit;
	private TextView txt_guan;
	private int nowpager = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 页面设置为无标题
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			String guan = bundle.getString("guanNum");
			if (guan != null && guan.equals("1")) {
				this.guanNum = 1;
				//设置音效
				Intent intent = new Intent(MainActivity.this,MusicService.class);  
				intent.putExtra("state", "success");
				startService(intent); 
			}
			if (guan != null && guan.equals("2")) {
				this.guanNum = 2;
				//设置音效
				Intent intent = new Intent(MainActivity.this,MusicService.class);  
				intent.putExtra("state", "success");
				startService(intent); 
			}
		}

		// 得到布局文件
		setContentView(R.layout.main);
		// 初始化View
		initView();

	}

	// 弹出是否确认交卷的对话框
	private void initAlertDialog() {
		// 新建对话框
		builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("提示");
		builder.setMessage("是否确定交卷?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				List<QuestBean> questBeans = QuestionGetUtil
						.getQuestions(MainActivity.this);
				int grade = 0;
				switch (guanNum) {
				case 0:
					grade = 0;
					for (int i = 0; i < 5; i++) {
						QuestBean questBean = questBeans.get(i);
						if (questBean.getMyAnswer().equals(
								questBean.getRightAnswer())) {
							grade++;
						}
					}
					if (grade >= 4) {
						Intent intent = new Intent(MainActivity.this,
								MainActivity.class);
						intent.putExtra("guanNum", "" + 1);
						startActivity(intent);
						finish();
					} else {
						Intent intent = new Intent(MainActivity.this,
								EndActivity.class);
						startActivity(intent);
						finish();
					}
					break;
				case 1:
					grade = 0;
					for (int i = 5; i < 10; i++) {
						QuestBean questBean = questBeans.get(i);
						if (questBean.getMyAnswer().equals(
								questBean.getRightAnswer())) {
							grade++;
						}
					}
					if (grade >= 4) {
						Intent intent = new Intent(MainActivity.this,
								MainActivity.class);
						intent.putExtra("guanNum", "" + 2);
						startActivity(intent);
						finish();
					} else {
						Intent intent = new Intent(MainActivity.this,
								EndActivity.class);
						startActivity(intent);
						finish();
					}
					break;
				case 2:
					Intent intent = new Intent(MainActivity.this,
							EndActivity.class);
					startActivity(intent);
					finish();
					break;
				default:
					break;
				}

			}

		});
		builder.setNegativeButton("取消", null);
	}

	/**
	 * 初始化布局
	 */
	void initView() {

		vp_answer = (ViewPager) findViewById(R.id.vp_answer);
		btn_previous = (Button) findViewById(R.id._btn_previous);
		btn_next = (Button) findViewById(R.id._btn_next);
		btn_submit = (Button) findViewById(R.id._btn_submit);
		txt_guan =  (TextView) findViewById(R.id._txt_guan);
		btn_previous.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_submit.setOnClickListener(this);

		fragmentlists = new ArrayList<>();
		List<QuestBean> questBeans = QuestionGetUtil.getQuestions(this);
		switch (guanNum) {
		case 0:
			for (int i=0;i<5;i++) {
				QuestBean questBean = questBeans.get(i);
				fragmentlists.add(new AnswerFragment(questBean));
			}
			txt_guan.setText("第一关");
			break;
		case 1:
			for (int i=5;i<10;i++) {
				QuestBean questBean = questBeans.get(i);
				fragmentlists.add(new AnswerFragment(questBean));
			}
			txt_guan.setText("第二关");
			break;
		case 2:
			for (int i=10;i<13;i++) {
				QuestBean questBean = questBeans.get(i);
				fragmentlists.add(new AnswerFragment(questBean));
			}
			txt_guan.setText("第三关");
			break;
		default:
			break;
		}
		

		// 设置适配器
		vp_answer.setAdapter(new MainAdapter(getSupportFragmentManager()));
		vp_answer.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * viewpager适配器
	 */
	class MainAdapter extends FragmentPagerAdapter {
		public MainAdapter(FragmentManager fm) {
			super(fm);
		}

		// 获取条目
		@Override
		public Fragment getItem(int position) {
			return fragmentlists.get(position);
		}

		// 数目
		@Override
		public int getCount() {
			return fragmentlists.size();
		}
	}

	/**
	 * viewpager监听事件
	 */
	private class MyOnPageChangeListener implements
			ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			nowpager = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// 点击上一题按钮
		case R.id._btn_previous:
			// 如果是第一题，则谈吐司提醒，否则上移一道题
			if (nowpager == 0) {
				Toast.makeText(MainActivity.this, "已经到头啦!", Toast.LENGTH_SHORT)
						.show();
			} else {
				vp_answer.setCurrentItem(--nowpager);
			}
			break;
		// 点击下一题按钮
		case R.id._btn_next:
			// 如果是最后一题，则谈吐司提醒，否则下移一道题
			if (nowpager == fragmentlists.size()) {
				Toast.makeText(MainActivity.this,
						"已经是最后一题了!" , Toast.LENGTH_SHORT)
						.show();
			} else {
				vp_answer.setCurrentItem(++nowpager);
			}
			break;
		case R.id._btn_submit:
			initAlertDialog();
			builder.show();
			break;
		default:
			break;
		}
	}

}