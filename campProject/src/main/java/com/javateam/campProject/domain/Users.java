package com.javateam.campProject.domain;

public class Users {
	//TODO 확인 후 어떻게 할 건 지 선택
<<<<<<< HEAD

	private String userid;
	private String username;
	private String password;

	public Users(String userid, String username, String password) {
		this.userid = userid;
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return String.format("Users [userid=%s, username=%s, password=%s]", userid, username, password);
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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
=======
	
	private String username;
	private String password;
	private int enabled;
	
	public Users() {}
	
	public Users(String username, String password, int enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return String.format("Users [username=%s, password=%s, enabled=%s]", username, password, enabled);
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
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
>>>>>>> refs/heads/join
}
