package com.javateam.campProject.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.campProject.domain.SocialUser;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class SocialUserDAOImpl implements SocialUserDAO{

	@Autowired
	SqlSession sqlSession;

	private static final String MAPPER_PATH = "mapper.campSocialUserMapper.";

	// email로 소셜 회원 정보 조회
	@Override
	public SocialUser selectSocialMemberByEmail(String strEmail) {

		return sqlSession.selectOne(MAPPER_PATH + "selectSocialMemberByEmail", strEmail);
	}

	// social(naver, google) 회원정보 저장
	@Override
	public void insertSocialUser(SocialUser socialUser) {

		sqlSession.insert(MAPPER_PATH + "insertSocialUser", socialUser);
	}

	// social (google) 회원정보 수정
	@Override
	public void updateSocialGoogleUser(SocialUser socialUser) {

		sqlSession.update(MAPPER_PATH + "updateSocialGoogleUser", socialUser);
	}

	// 예약시 예약테이블에 넣을 정보 조회
	@Override
	public SocialUser socialReservationInfo(String strEmail, String authVendor) {
		Map<String, String> map = new HashMap<>();
		map.put("email", strEmail);
		map.put("authVendor", authVendor);
		return sqlSession.selectOne(MAPPER_PATH + "socialReservationInfo", map);
	}

	@Override
	public SocialUser selectSocialMemberByEmailAndAuthVendor(String strEmail, String strAuthVendor) {
		Map<String, String> map = new HashMap<>();
		map.put("email", strEmail);
		map.put("authVendor", strAuthVendor);
		return sqlSession.selectOne(MAPPER_PATH + "selectSocialMemberByEmailAndAuthVendor", map);
	}

	@Override
	public List<SocialUser> selectSocialAllMembers() {
		return sqlSession.selectList(MAPPER_PATH + "selectSocialAllMembers");
	}

	@Override
	public SocialUser selectSocialMemberById(int id) {
		return sqlSession.selectOne(MAPPER_PATH + "selectSocialMemberById", id);
	}

	@Override
	public void updateRole(int id, String role) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("role", role);
		sqlSession.update(MAPPER_PATH + "updateRole", map);
	}

	@Override
	public List<SocialUser> selectSocialAllMembersByRole(String role) {
		return sqlSession.selectList(MAPPER_PATH + "selectSocialAllMembersByRole", role);
	}

	@Override
	public void deleteSocialUser(int id) {
		sqlSession.update(MAPPER_PATH + "deleteSocialUser", id);
	}

}
