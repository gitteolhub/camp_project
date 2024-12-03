package com.javateam.campProject.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialRole {

	ROLE_ADMIN("ROLE_ADMIN", "관리자"),
	ROLE_USER("ROLE_USER", "일반 사용자"),
	ROLE_CEO("ROLE_CEO", "사업자");

	private final String key;
	private final String title;

}
