package aaa.entity;

public class Score {
	private String coursename;
	private int scoreId;
	private String username;
	public Score(String coursename, int scoreId, String username, int score) {
		super();
		this.coursename = coursename;
		this.scoreId = scoreId;
		this.username = username;
		this.score = score;
	}
	public int getScoreId() {
		return scoreId;
	}
	public void setScoreId(int scoreId) {
		this.scoreId = scoreId;
	}
	private int score;
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	
}
