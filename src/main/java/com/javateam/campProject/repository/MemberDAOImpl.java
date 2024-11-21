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
	public MemberVO selectMemberById(MemberVO objMemberVO) {

		return sqlSession.selectOne(MAPPER_PATH + "selectMemberById", objMemberVO);
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

}
