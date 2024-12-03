package com.javateam.campProject.service;

import java.util.List;

import com.javateam.campProject.domain.SocialUser;

public interface SocialUserService {

	// social (google) 회원정보 수정
	boolean updateSocialGoogleUser(SocialUser socialUser);

	// social 회원 조회
	SocialUser selectSocialUser(String email, String authVendor);
	
	// 전체 소셜 회원 정보 조회
	List<SocialUser> selectSocialAllMembers();
}
