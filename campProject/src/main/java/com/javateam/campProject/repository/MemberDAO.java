package com.javateam.campProject.repository;

import java.util.List;
import java.util.Map;
import com.javateam.campProject.domain.MemberVO;

public interface MemberDAO {

	// 주어진 ID로 회원 정보를 조회
	MemberVO selectMemberById(String strId);

	// 새로운 회원을 데이터베이스에 추가
	boolean insertMember(MemberVO objMemberVO);

	// 회원정보 중복 점검(회원 가입)
	boolean hasMemberByFld (String strField, String strValue);

	// 회원 Role 생성
	boolean insertRole(String strId, String strRole);

}
