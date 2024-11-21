package com.javateam.campProject.repository;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.campProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MemberDAOImpl implements MemberDAO {

	@Autowired
	public SqlSession sqlSession;

	private static final String MAPPER_PATH = "mapper.campMapper.";

	// 주어진 ID로 회원 정보를 조회
	@Override
	public MemberVO selectMemberById(String strId) {

		return sqlSession.selectOne(MAPPER_PATH + "selectMemberById",strId);
	}

	// 새로운 회원을 데이터베이스에 추가
	@Override
	public boolean insertMember(MemberVO objMemberVO) {

		boolean blRetVal = false;

		try {
			int intResult = sqlSession.insert(MAPPER_PATH + "insertMember", objMemberVO);
			blRetVal = intResult == 1 ? true : false;

		} catch (Exception ex) {
			log.error("[MemberDAOImpl][insertMember] Exception : " + ex);
		}
		return blRetVal;
	}

	// 회원정보 중복 점검(회원 가입)
	@Override
	public boolean hasMemberByFld(String strField, String strValue) {

		// 중독 체크하기 위해 맵 생성
		Map<String, String> memberDatabase = new HashMap<>();

		memberDatabase.put("fld", strField);	// 필드 이름 추가
		memberDatabase.put("val", strValue); 	// 필드 값 추가

		// name, gender 중복 허용
		if (strField.equals("NAME") || strField.equals("GENDER")) {

			return false;

		} else {

			return(int) sqlSession.selectOne(MAPPER_PATH + "hasMemberByFld", memberDatabase) == 1 ? true: false;
		}

	}


	// 회원 Role 생성
	@Override
	public boolean insertRole(String strId, String strRole) {

		boolean blRetVal = false;

		try {
			Map<String, String> map = new HashMap<>();

			map.put("id",   strId);
			map.put("role", strRole);

			sqlSession.selectList(MAPPER_PATH + "insertRole", map);
			blRetVal = true;

		} catch (Exception ex) {
			log.error("[MemberDao][insertRole] Exception = " + ex.getMessage(), ex);
			ex.printStackTrace();

		}
		return blRetVal;
	}


	// 회원 Role 수정 (관리자 화면에서)
	public boolean updateRole(String strId, String strRole) {
		boolean blRetVal = false;

		try {
			 Map<String, String> map = new HashMap<>();
		     map.put("id", strId);
		     map.put("role", strRole);

		     int intResult = sqlSession.update(MAPPER_PATH + "updateRole", map);
		     blRetVal = intResult == 1; // 업데이트 성공 여부 확인
		} catch (Exception ex) {
			log.error("[MemberDAOImpl][updateRole] Exception: {}", ex);

		}

		return blRetVal;
	}
}