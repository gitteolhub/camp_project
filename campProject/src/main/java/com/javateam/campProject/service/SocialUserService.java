package com.javateam.campProject.service;

import com.javateam.campProject.domain.SocialUser;

public interface SocialUserService {

	// social (google) 회원정보 수정
	boolean updateSocialGoogleUser(SocialUser socialUser);

	// social 회원 조회
	SocialUser selectSocialUser(String email, String authVendor);
}
