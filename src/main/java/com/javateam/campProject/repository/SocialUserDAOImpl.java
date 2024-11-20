package com.javateam.campProject.repository;

import java.util.HashMap;
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

}
