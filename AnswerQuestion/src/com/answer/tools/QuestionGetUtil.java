package com.answer.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.answer.entity.QuestBean;

public class QuestionGetUtil {
	private static List<QuestBean> quests;

	@SuppressWarnings("resource")
	public static List<QuestBean> getQuestions(Context context) {
		if (quests == null) {
			quests = new ArrayList<QuestBean>();
			try {
				InputStream is = context.getAssets().open("question.txt");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line = "";
				while ((line = reader.readLine()) != null) {
					String[] fields = line.split("#");
					QuestBean quest = new QuestBean();
					quest.setTitle(fields[0]);
					quest.setOptionA(fields[1]);
					quest.setOptionB(fields[2]);
					quest.setOptionC(fields[3]);
					quest.setOptionD(fields[4]);
					quest.setRightAnswer(fields[5]);
					quest.setMyAnswer("empty");
					quests.add(quest);
					Log.i("quest", quest.getTitle());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return quests;

	}
}
