package com.javateam.campProject.service;

import java.util.List;

import com.javateam.campProject.domain.SocialUser;

public interface SocialUserService {

	// social (google) 회원정보 수정
	boolean updateSocialGoogleUser(SocialUser socialUser);

	// social 회원 조회
	SocialUser selectSocialUser(String email, String authVendor);
	
	// social 회원 조회(id)
	SocialUser selectSocialUserById(int id);
	
	// 전체 social 회원 정보 조회
	List<SocialUser> selectSocialAllMembers();
	
	// social 회원 role 수정
	void updateRole(int id, String role);
	
	// social 회원 role별 조회
	List<SocialUser> selectSocialAllMembersByRole(String role);
	
	// social 회원 정보 삭제
	boolean deleteSocialUser(int id);

}
