package com.javateam.campProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.campProject.domain.MemberVO;
import com.javateam.campProject.domain.SocialRole;
import com.javateam.campProject.domain.SocialUser;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class MemberDAOTest {

	@Autowired
	MemberDAO memberDAO;

	@Autowired
	SocialUserDAO socialUserDAO;

	// 아이디와 회원명이 같은지 테스트
	@Test
	void testSelectMemberByIdName() {
		MemberVO objMemberVO = new MemberVO();

		objMemberVO.setId("abcd1111");

		//objMemberVO = memberDAO.selectMemberById(objMemberVO);
		log.info("[objMemberVO]: " + objMemberVO);

		assertEquals("홍길동", objMemberVO.getName());
	}

	// 아이디와 비밀번호가 같은지 테스트
	@Test
	void testSelectMemberByIdPw() {
		MemberVO objMemberVO = new MemberVO();

		objMemberVO.setId("abcd1111");

	//	objMemberVO = memberDAO.selectMemberById(objMemberVO);
		assertEquals("#Abcd1111", objMemberVO.getPw());
	}

	@Test
	@Transactional
	@Rollback(false)
	void testInsertSocialUser() {
		SocialUser socialUser = SocialUser.builder()
											.id(1).name("소원").email("tgdcom7@naver.com").gender("여자")
											.birthday("2000-06-02").mobile("010-8389-1259").memberType("Type")
											.authVendor("naver").role(SocialRole.USER).build();

		socialUserDAO.insertSocialUser(socialUser);

	}
}
