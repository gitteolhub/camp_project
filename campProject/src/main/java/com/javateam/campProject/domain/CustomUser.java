package com.javateam.campProject.domain;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	private String userid;
	private String username;
	private String password;

	private List<Role> authorities;
	private boolean accountNonExpired     = true;
	private boolean accountNonLocked      = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;		// 11월19일 수정

	public CustomUser(Users users) {
		this.userid  = users.getUserid();
		this.username = users.getUsername();
		this.password = users.getPassword();
	}

	public CustomUser(String userid, String username, String password) {
		this.userid  = userid;
		this.username = username;
		this.password = password;
	}

}
