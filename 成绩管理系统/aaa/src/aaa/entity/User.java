package aaa.entity;

/**
 * 用户实体类
 * 
 * @author guoyang
 *
 */
public class User {
	private  String username;
	private String password;
	private int  UserId;
	public User(String username, String password, int userId) {
		super();
		this.username = username;
		this.password = password;
		UserId = userId;
	}


	public String getUsername() {
		return username;
	}
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
