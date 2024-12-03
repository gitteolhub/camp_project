package com.javateam.campProject.repository;

import java.util.List;
import java.util.Map;

import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.SocialUser;

public interface MemberDAO {

	// 주어진 ID로 회원 정보를 조회
	MemberVO selectMemberById(String strId);

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

	// 새로운 회원을 데이터베이스에 추가
	boolean insertMember(MemberVO objMemberVO);

	// 회원정보 중복 점검(회원 가입)
	boolean hasMemberByFld (String strField, String strValue);

	// 회원 Role 생성
	boolean insertRole(String strId, String strRole);
	
	// 회원정보 수정
	boolean updateMember (MemberVO objMemberVO);

	// 예약시 예약테이블에 넣을 정보 조회
	MemberVO memberReservationInfo(String strId);
	
	// 관리자 모드에서 사용자 조회 및 관리
	List<Map<String, String>> selectAllUsersWithRole();

}
