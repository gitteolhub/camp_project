package com.javateam.campProject.service;

import java.util.List;
import java.util.Map;

import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.Role;

public interface MemberService {

	// 아이디로 회원 정보를 조회
	MemberVO selectMemberById(String strId);

	// 이름, 이메일로 아이디 찾기
	String findUserIdByNameEmail(String strName, String strEmail);

	// 아이디로 비밀번호 찾기
	String findUserPwByID(String id);

	// 아이디로 이메일 조회
	String findUserEmailByID(String id);
	
	// 이메일로 회원 Id 조회
	String findUserIdByEmail(String strEmail);
	
	// 비밀번호 업데이트
	boolean updatePw(String strId, String strPw);

	// 중복 아이디 확인후 새로운 회원 추가
	boolean insertMember(MemberVO objMemberVO);

	// 회원정보 중복 점검(회원 가입)
	boolean hasMemberByFld (String strField, String strValue);

	// 회원 role 생성
	boolean insertRole(Role role);
	
	// 회원 role 수정
	void updateRole (String id, String role);
	
	// 회원정보 수정
	boolean updateMember (MemberVO objMemberVO);
	
	// 관리자 모드에서 사용자 조회 및 관리
	List<Map<String, String>> selectAllUsersWithRole();
	
	// 회원 role별 조회
	List<Map<String, String>> selectAllUsersByRole(String role);
}
