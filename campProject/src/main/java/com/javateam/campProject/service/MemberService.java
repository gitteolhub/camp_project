 package com.javateam.campProject.service;

import java.util.List;
import java.util.Map;

import com.javateam.campProject.domain.MemberJsonVO;
import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.Role;

public interface MemberService {

	// 아이디로 회원 정보를 조회
	MemberVO selectMemberById(String strId);

	// 중복 아이디 확인후 새로운 회원 추가
	boolean insertMember(MemberVO objMemberVO);

	// 회원정보 중복 점검(회원 가입)
	boolean hasMemberByFld (String strField, String strValue);

	// 회원 role 생성
	boolean insertRole(Role role);
}
