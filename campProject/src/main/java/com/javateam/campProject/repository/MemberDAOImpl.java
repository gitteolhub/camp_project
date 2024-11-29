package com.javateam.campProject.repository;

import java.util.HashMap;
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

	private static final String MAPPER_PATH="mapper.campUserMapper.";

	// 주어진 ID로 회원 정보를 조회
	@Override
	public MemberVO selectMemberById(String strId) {

		return sqlSession.selectOne(MAPPER_PATH + "selectMemberById", strId);
	}

	@Override
	public String findUserIdByNameEmail(Map<String, String> params) {

		return sqlSession.selectOne(MAPPER_PATH + "findUserIdByNameEmail", params);
	}

	@Override
	public String findUserPwByID(String id) {

		return sqlSession.selectOne(MAPPER_PATH + "findUserPwByID", id);
	}

	@Override
	public String findUserEmailByID(String id) {
		return sqlSession.selectOne(MAPPER_PATH + "findUserEmailByID", id);
	}

	// 이메일로 회원 Id 조회
	@Override
	public String findUserIdByEmail(String strEmail) {
		return sqlSession.selectOne(MAPPER_PATH + "findUserIdByEmail", strEmail);
	}

	// 비밀번호 업데이트
	@Override
	public void updatePw(String strId, String strPw) {
		try {
			 Map<String, String> map = new HashMap<>();
		     map.put("id", strId);
		     map.put("pw", strPw);

		     sqlSession.update(MAPPER_PATH + "updatePw", map);
		} catch (Exception ex) {
			log.error("[MemberDAO][updatePw] Exception: {}", ex);

		}
		
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

	@Override
	public boolean updateMember(MemberVO objMemberVO) {
		boolean blRetVal = false;

		try {
			//기존 회원정보 존재 여부 점검
			int intResult = this.selectMemberById(objMemberVO.getId()) != null ? 1 : 0;

		if(intResult == 0) {
			throw new Exception("회원정보가 존재하지 않습니다.");
		}
		sqlSession.update(MAPPER_PATH + "updateMember",objMemberVO);

		blRetVal = true;
		} catch (Exception ex) {
			log.error("[MemberDao][updateMember] Exception : {}", ex);
			ex.printStackTrace();
		}
		return blRetVal;
	}
}

