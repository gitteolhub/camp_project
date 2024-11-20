package com.javateam.campProject.repository;

import java.util.Map;

import com.javateam.campProject.domain.MemberVO;

public interface MemberDAO {

	// 주어진 ID로 회원 정보를 조회
	MemberVO selectMemberById(MemberVO objMemberVO);

	// 이름, 이메일로 아이디 찾기
	String findUserIdByNameEmail(Map<String, String> params);

	// 아이디로 비밀번호 찾기
	String findUserPwByID(String id);

	// 아이디로 이메일 조회
	String findUserEmailByID(String id);
	
	// 이메일로 회원 Id 조회
	String findUserIdByEmail(String strEmail);
	
	// 비밀번호 업데이트
	void updatePw(String strId, String strPw);

}
