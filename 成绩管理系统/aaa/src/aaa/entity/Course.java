package aaa.entity;

public class Course {

	private String coursename;
	private int courseID;
	public String getCoursename() {
		return coursename;
	}
	public Course(String coursename, int courseID) {
		super();
		this.coursename = coursename;
		this.courseID = courseID;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public int getCourseID() {
		return courseID;
	}
	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}
}
