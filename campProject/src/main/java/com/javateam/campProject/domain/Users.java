package com.javateam.campProject.domain;

public class Users {

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
}
