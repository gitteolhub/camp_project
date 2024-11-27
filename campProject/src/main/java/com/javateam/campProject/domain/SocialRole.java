package com.javateam.campProject.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialRole {

	SUPERADMIN("ROLE_SUPER_ADMIN", "최고 관리자"),
	ADMIN("ROLE_ADMIN", "관리자"),
	USER("ROLE_USER", "일반 사용자"),
	CEO("ROLE_CEO", "사업자");

	private final String key;
	private final String title;

}
