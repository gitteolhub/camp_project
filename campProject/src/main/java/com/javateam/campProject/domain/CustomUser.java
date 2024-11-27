package com.javateam.campProject.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
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
	private String username; // 사용자 ID
	private String name;	 // 사용자 이름	
	private String password; // 사용자 pw

	/* Spring Security 관련 필드 */
	private List<Role> authorities;
	private boolean accountNonExpired     = true;
	private boolean accountNonLocked      = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;		// 11월19일 수정

	// Users 객체를 받아 초기화
	public CustomUser(Users users) {
		this.username = users.getUserid();
		this.name     = users.getUsername();
		this.password = users.getPassword();
	}

	public CustomUser(String username, String name, String password, boolean enabled) {
		this.username = username;
		this.name     = name;
		this.password = password;
	}
//    // 권한 목록 반환
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities.stream()
//                          .map(role -> (GrantedAuthority) role::getAuthority)  // Role 객체를 GrantedAuthority로 변환
//                          .collect(Collectors.toList());
//    }
}

