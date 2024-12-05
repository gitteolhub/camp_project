package com.javateam.campProject.repository;

import java.util.List;

import com.javateam.campProject.domain.SocialUser;

public interface SocialUserDAO {

	// email로 소셜 회원 정보 조회
	SocialUser selectSocialMemberByEmail(String strEmail);

	// id에 준하는 email, authVendor로 소셜 회원 정보 조회
	SocialUser selectSocialMemberByEmailAndAuthVendor(String strEmail, String strAuthVendor);
	
	// id로 소셜 회원 정보 조회
	SocialUser selectSocialMemberById(int id);

	// social(naver, google) 회원정보 저장
	void insertSocialUser(SocialUser socialUser);

	// social (google) 회원정보 수정
	void updateSocialGoogleUser(SocialUser socialUser);

	// 예약시 예약테이블에 넣을 정보 조회
	SocialUser socialReservationInfo(String strEmail, String authVendor);
	
	// 전체 소셜 회원 정보 조회
	List<SocialUser> selectSocialAllMembers();
	
	// social 회원 role 수정
	void updateRole(int id, String role);
	
	// social 회원 role별 조회
	List<SocialUser> selectSocialAllMembersByRole(String role);

	// social 회원 정보 삭제
	void deleteSocialUser(int id);

}
