package com.answer.entity;

public class QuestBean {

	private String title;
	private String OptionA;
	private String OptionB;
	private String OptionC;
	private String OptionD;
	private String MyAnswer = "";
	private String RightAnswer;
	public String getMyAnswer() {
		return MyAnswer;
	}

	public void setMyAnswer(String myAnswer) {
		MyAnswer = myAnswer;
	}

	public String getRightAnswer() {
		return RightAnswer;
	}

	public void setRightAnswer(String rightAnswer) {
		RightAnswer = rightAnswer;
	}

	

	public void setTitle(String title) {
		this.title = title;
	}

	public void setOptionA(String optionA) {
		OptionA = optionA;
	}

	public void setOptionB(String optionB) {
		OptionB = optionB;
	}

	public void setOptionC(String optionC) {
		OptionC = optionC;
	}

	public void setOptionD(String optionD) {
		OptionD = optionD;
	}

	public String getTitle() {
		
		return this.title;
	}

	public String getOptionA() {
		return this.OptionA;
	}
	public String getOptionB() {
		return this.OptionB;
	}
	public String getOptionC() {
		return this.OptionC;
	}
	public String getOptionD() {
		return this.OptionD;
	}

}
