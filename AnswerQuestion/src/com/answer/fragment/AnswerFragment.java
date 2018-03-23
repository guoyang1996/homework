package com.answer.fragment;

import com.answer.entity.QuestBean;
import com.guoyang.answerquestion.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AnswerFragment extends Fragment implements
		RadioGroup.OnCheckedChangeListener {
	private RadioButton rb_option_a;
	private RadioButton rb_option_b;
	private RadioButton rb_option_c;
	private RadioButton rb_option_d;
	private String option = "";
	private RadioGroup rg_base;
	private TextView tv_title;
	QuestBean questBean = null;

	public AnswerFragment(QuestBean quest){
		this.questBean=quest;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_quest, container, false);
		tv_title = (TextView) view.findViewById(R.id._tv_title);
		rg_base = (RadioGroup) view.findViewById(R.id._rg_base);

		// 找id,设置监听事件

		rb_option_a = (RadioButton) view.findViewById(R.id._rb_option_a);
		rb_option_b = (RadioButton) view.findViewById(R.id._rb_option_b);
		rb_option_c = (RadioButton) view.findViewById(R.id._rb_option_c);
		rb_option_d = (RadioButton) view.findViewById(R.id._rb_option_d);
		rg_base.setOnCheckedChangeListener(this);
		
		initData();

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
	}

	public AnswerFragment() {

	}

	public void initData() {
		tv_title.setText("" + questBean.getTitle());
		// 如果没有传递数据，则退出
		if (questBean == null) {
			Log.i("zwc", "initData: questBean==null");
			return;
		}
		// 对应选项赋值
		rb_option_a.setText("A. " + questBean.getOptionA());
		rb_option_b.setText("B. " + questBean.getOptionB());
		rb_option_c.setText("C. " + questBean.getOptionC());
		rb_option_d.setText("D. " + questBean.getOptionD());

	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == rb_option_a.getId()) {
			option = "A";
		} else if (checkedId == rb_option_b.getId()) {
			option = "B";
		} else if (checkedId == rb_option_c.getId()) {
			option = "C";
		} else if (checkedId == rb_option_d.getId()) {
			option = "D";
		}
		// 设置答案
		questBean.setMyAnswer(option);
	}
}
