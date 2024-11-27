package com.javateam.campProject.domain;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 7464267597005842862L;

//	private String userId;
	private String username;
	private String role;

	@Override
	public String getAuthority() {
		return this.role;
	}

}
